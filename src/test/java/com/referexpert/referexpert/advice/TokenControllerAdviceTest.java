package com.referexpert.referexpert.advice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.context.request.WebRequest;

import com.referexpert.referexpert.exception.TokenRefreshException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@ExtendWith(MockitoExtension.class)
class TokenControllerAdviceTest {

    @InjectMocks
    private TokenControllerAdvice tokenControllerAdvice;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn("test-request");
    }

    @Test
    void whenTokenRefreshException_thenReturnForbidden() {
        TokenRefreshException exception = new TokenRefreshException("test-token", "Token expired");
        
        ErrorMessage errorMessage = tokenControllerAdvice.handleTokenRefreshException(exception, webRequest);
        
        assertEquals(403, errorMessage.getStatusCode());
        assertEquals("Token expired", errorMessage.getMessage());
        assertEquals("test-request", errorMessage.getDescription());
    }

    @Test
    void whenBadCredentialsException_thenReturnUnauthorized() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
        
        ErrorMessage errorMessage = tokenControllerAdvice.handleBadCredentialsException(exception, webRequest);
        
        assertEquals(401, errorMessage.getStatusCode());
        assertEquals("Invalid username or password", errorMessage.getMessage());
        assertEquals("test-request", errorMessage.getDescription());
    }

    @Test
    void whenExpiredJwtException_thenReturnUnauthorized() {
        ExpiredJwtException exception = mock(ExpiredJwtException.class);
        
        ErrorMessage errorMessage = tokenControllerAdvice.handleExpiredJwtException(exception, webRequest);
        
        assertEquals(401, errorMessage.getStatusCode());
        assertEquals("JWT token has expired", errorMessage.getMessage());
        assertEquals("test-request", errorMessage.getDescription());
    }

    @Test
    void whenSignatureException_thenReturnUnauthorized() {
        SignatureException exception = new SignatureException("Invalid signature");
        
        ErrorMessage errorMessage = tokenControllerAdvice.handleJwtValidationException(exception, webRequest);
        
        assertEquals(401, errorMessage.getStatusCode());
        assertEquals("Invalid JWT token", errorMessage.getMessage());
        assertEquals("test-request", errorMessage.getDescription());
    }

    @Test
    void whenMalformedJwtException_thenReturnUnauthorized() {
        MalformedJwtException exception = new MalformedJwtException("Malformed token");
        
        ErrorMessage errorMessage = tokenControllerAdvice.handleJwtValidationException(exception, webRequest);
        
        assertEquals(401, errorMessage.getStatusCode());
        assertEquals("Invalid JWT token", errorMessage.getMessage());
        assertEquals("test-request", errorMessage.getDescription());
    }
}
