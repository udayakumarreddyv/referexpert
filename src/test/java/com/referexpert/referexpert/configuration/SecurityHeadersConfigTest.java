package com.referexpert.referexpert.configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;

class SecurityHeadersConfigTest {

    private SecurityHeadersConfig securityHeadersConfig;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        securityHeadersConfig = new SecurityHeadersConfig();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void whenSecurityHeadersWritten_thenAllHeadersPresent() {
        HeaderWriter headerWriter = securityHeadersConfig.securityHeadersWriter();
        headerWriter.writeHeaders(request, response);

        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("DENY", response.getHeader("X-Frame-Options"));
        assertEquals("1; mode=block", response.getHeader("X-XSS-Protection"));
        assertNotNull(response.getHeader("Strict-Transport-Security"));
        assertNotNull(response.getHeader("Cache-Control"));
        assertNotNull(response.getHeader("Content-Security-Policy"));
    }

    @Test
    void whenHSTSHeaderWritten_thenContainsCorrectValues() {
        HeaderWriter headerWriter = securityHeadersConfig.securityHeadersWriter();
        headerWriter.writeHeaders(request, response);

        String hstsHeader = response.getHeader("Strict-Transport-Security");
        assertTrue(hstsHeader.contains("max-age=31536000"));
        assertTrue(hstsHeader.contains("includeSubDomains"));
    }

    @Test
    void whenCSPHeaderWritten_thenContainsCorrectDirectives() {
        HeaderWriter headerWriter = securityHeadersConfig.securityHeadersWriter();
        headerWriter.writeHeaders(request, response);

        String cspHeader = response.getHeader("Content-Security-Policy");
        assertTrue(cspHeader.contains("default-src 'self'"));
        assertTrue(cspHeader.contains("frame-ancestors 'none'"));
    }

    @Test
    void whenCacheControlHeadersWritten_thenContainsCorrectValues() {
        HeaderWriter headerWriter = securityHeadersConfig.securityHeadersWriter();
        headerWriter.writeHeaders(request, response);

        assertEquals("no-cache, no-store, max-age=0, must-revalidate", 
                    response.getHeader("Cache-Control"));
        assertEquals("no-cache", response.getHeader("Pragma"));
        assertEquals("0", response.getHeader("Expires"));
    }
}
