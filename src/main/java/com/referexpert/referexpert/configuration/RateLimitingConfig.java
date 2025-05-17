package com.referexpert.referexpert.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.RateLimiter;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitingConfig {

    @Value("${security.rate-limit.enabled:true}")
    private boolean enabled;

    @Value("${security.rate-limit.max-requests:100}")
    private int maxRequests;

    @Value("${security.rate-limit.time-window:3600000}")
    private long timeWindow;

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    @Bean
    public RateLimiter authRateLimiter() {
        // Allow maxRequests per hour
        return RateLimiter.create(maxRequests / (timeWindow / 1000.0));
    }

    public boolean tryConsume(HttpServletRequest request) {
        if (!enabled) {
            return true;
        }

        String key = getClientIp(request);
        return buckets.computeIfAbsent(key, k -> new TokenBucket(maxRequests, timeWindow))
                     .tryConsume();
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class TokenBucket {
        private final int capacity;
        private final long refillTime;
        private int tokens;
        private long lastRefill;

        TokenBucket(int capacity, long refillTime) {
            this.capacity = capacity;
            this.refillTime = refillTime;
            this.tokens = capacity;
            this.lastRefill = System.currentTimeMillis();
        }

        synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            if (now - lastRefill >= refillTime) {
                tokens = capacity;
                lastRefill = now;
            }
        }
    }
}
