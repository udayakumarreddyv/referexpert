package com.referexpert.referexpert.configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RateLimitingConfigTest {

    private RateLimitingConfig rateLimitingConfig;
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        rateLimitingConfig = new RateLimitingConfig();
        ReflectionTestUtils.setField(rateLimitingConfig, "enabled", true);
        ReflectionTestUtils.setField(rateLimitingConfig, "maxRequests", 2);
        ReflectionTestUtils.setField(rateLimitingConfig, "timeWindow", 1000L); // 1 second for testing
        
        mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    void whenUnderLimit_thenAllowRequests() {
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "First request should be allowed");
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "Second request should be allowed");
        assertFalse(rateLimitingConfig.tryConsume(mockRequest), "Third request should be blocked");
    }

    @Test
    void whenDisabled_thenAllowAllRequests() {
        ReflectionTestUtils.setField(rateLimitingConfig, "enabled", false);
        
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimitingConfig.tryConsume(mockRequest), 
                "Request should be allowed when rate limiting is disabled");
        }
    }

    @Test
    void whenDifferentIPs_thenSeparateLimits() {
        HttpServletRequest mockRequest2 = mock(HttpServletRequest.class);
        when(mockRequest2.getRemoteAddr()).thenReturn("127.0.0.2");
        
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "IP1 first request");
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "IP1 second request");
        assertFalse(rateLimitingConfig.tryConsume(mockRequest), "IP1 third request blocked");
        
        assertTrue(rateLimitingConfig.tryConsume(mockRequest2), "IP2 first request");
        assertTrue(rateLimitingConfig.tryConsume(mockRequest2), "IP2 second request");
        assertFalse(rateLimitingConfig.tryConsume(mockRequest2), "IP2 third request blocked");
    }

    @Test
    void whenXForwardedForPresent_thenUseFirstIP() {
        when(mockRequest.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 10.0.0.2");
        
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "First request should be allowed");
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "Second request should be allowed");
        assertFalse(rateLimitingConfig.tryConsume(mockRequest), "Third request should be blocked");
        
        verify(mockRequest, times(3)).getHeader("X-Forwarded-For");
    }

    @Test
    void whenWaitingForRefill_thenAllowNewRequests() throws InterruptedException {
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "First request should be allowed");
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "Second request should be allowed");
        assertFalse(rateLimitingConfig.tryConsume(mockRequest), "Third request should be blocked");
        
        // Wait for refill
        Thread.sleep(1100);
        
        assertTrue(rateLimitingConfig.tryConsume(mockRequest), "Request after refill should be allowed");
    }
}
