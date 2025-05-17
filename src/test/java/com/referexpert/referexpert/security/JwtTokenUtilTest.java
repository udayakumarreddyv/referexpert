package com.referexpert.referexpert.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetails userDetails;
    private static final String SECRET = "testSecret123456789testSecret123456789testSecret123456789";
    private static final long JWT_TOKEN_VALIDITY = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtTokenValidity", JWT_TOKEN_VALIDITY);
        
        userDetails = new User("test@example.com", "password", new ArrayList<>());
    }

    @Test
    void whenGenerateToken_thenTokenIsValid() {
        String token = jwtTokenUtil.generateToken(userDetails);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals("test@example.com", jwtTokenUtil.getUsernameFromToken(token));
        assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void whenTokenExpired_thenValidationFails() {
        // Set token validity to -1 hour for testing expiration
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtTokenValidity", -3600000L);
        
        String token = jwtTokenUtil.generateToken(userDetails);
        
        assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenUtil.validateToken(token, userDetails);
        });
    }

    @Test
    void whenTokenModified_thenValidationFails() {
        String token = jwtTokenUtil.generateToken(userDetails);
        String modifiedToken = token.substring(0, token.length() - 1) + "X";
        
        assertThrows(SignatureException.class, () -> {
            jwtTokenUtil.validateToken(modifiedToken, userDetails);
        });
    }

    @Test
    void whenGenerateTokenFromUsername_thenTokenIsValid() {
        String token = jwtTokenUtil.generateTokenFromUsername("test@example.com");
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals("test@example.com", jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    void whenGetExpirationDate_thenReturnsCorrectDate() {
        String token = jwtTokenUtil.generateToken(userDetails);
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        
        assertNotNull(expiration);
        long expectedTime = System.currentTimeMillis() + JWT_TOKEN_VALIDITY;
        // Allow 1 second tolerance for test execution time
        assertTrue(Math.abs(expectedTime - expiration.getTime()) < 1000);
    }
}
