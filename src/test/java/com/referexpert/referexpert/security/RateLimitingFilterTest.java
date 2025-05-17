package com.referexpert.referexpert.security;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.configuration.RateLimitingConfig;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

    @Mock
    private RateLimitingConfig rateLimitingConfig;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void whenNotRateLimitedEndpoint_thenProceed() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/someEndpoint");
        
        rateLimitingFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
        verify(rateLimitingConfig, never()).tryConsume(any());
    }

    @Test
    void whenRateLimitedEndpoint_andUnderLimit_thenProceed() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/validateuser");
        when(rateLimitingConfig.tryConsume(request)).thenReturn(true);
        
        rateLimitingFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void whenRateLimitedEndpoint_andOverLimit_thenBlock() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/validateuser");
        when(rateLimitingConfig.tryConsume(request)).thenReturn(false);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"error\":\"Rate limit exceeded\"}");
        
        rateLimitingFilter.doFilterInternal(request, response, filterChain);
        
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(response).setContentType("application/json");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void whenMultipleAuthEndpoints_thenApplyRateLimit() throws ServletException, IOException {
        String[] authEndpoints = {"/validateuser", "/register", "/refreshtoken", "/resetpassword"};
        
        for (String endpoint : authEndpoints) {
            when(request.getRequestURI()).thenReturn(endpoint);
            when(rateLimitingConfig.tryConsume(request)).thenReturn(true);
            
            rateLimitingFilter.doFilterInternal(request, response, filterChain);
            
            verify(rateLimitingConfig).tryConsume(request);
        }
    }
}
