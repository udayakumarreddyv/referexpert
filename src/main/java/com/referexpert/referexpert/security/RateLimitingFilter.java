package com.referexpert.referexpert.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.configuration.RateLimitingConfig;
import com.referexpert.referexpert.advice.ErrorMessage;

import java.util.Date;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    @Autowired
    private RateLimitingConfig rateLimitingConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if (isRateLimitedEndpoint(request)) {
            if (!rateLimitingConfig.tryConsume(request)) {
                logger.warn("Rate limit exceeded for IP: {}", request.getRemoteAddr());
                
                ErrorMessage errorMessage = new ErrorMessage(
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    new Date(),
                    "Rate limit exceeded. Please try again later.",
                    request.getRequestURI()
                );

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimitedEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Apply rate limiting to authentication and user registration endpoints
        return path.contains("/validateuser") || 
               path.contains("/register") || 
               path.contains("/refreshtoken") ||
               path.contains("/resetpassword");
    }
}
