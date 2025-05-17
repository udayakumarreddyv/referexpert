package com.referexpert.referexpert.advice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.referexpert.referexpert.exception.TokenRefreshException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestControllerAdvice
public class TokenControllerAdvice {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenControllerAdvice.class);

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        logger.error("Token refresh failed: {}", ex.getMessage());
        return new ErrorMessage(
            HttpStatus.FORBIDDEN.value(),
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        logger.error("Authentication failed: {}", ex.getMessage());
        return new ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            new Date(),
            "Invalid username or password",
            request.getDescription(false));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        logger.error("JWT token expired: {}", ex.getMessage());
        return new ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            new Date(),
            "JWT token has expired",
            request.getDescription(false));
    }

    @ExceptionHandler({SignatureException.class, MalformedJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleJwtValidationException(AuthenticationException ex, WebRequest request) {
        logger.error("JWT validation failed: {}", ex.getMessage());
        return new ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            new Date(),
            "Invalid JWT token",
            request.getDescription(false));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("Authentication failed: {}", ex.getMessage());
        return new ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            new Date(),
            "Authentication failed",
            request.getDescription(false));
    }
}
