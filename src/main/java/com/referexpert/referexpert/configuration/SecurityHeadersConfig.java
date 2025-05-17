package com.referexpert.referexpert.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityHeadersConfig {

    @Bean
    public HeaderWriter securityHeadersWriter() {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/**");
        
        StaticHeadersWriter writer = new StaticHeadersWriter(
            "X-Content-Type-Options", "nosniff",
            "X-Frame-Options", "DENY",
            "X-XSS-Protection", "1; mode=block",
            "Strict-Transport-Security", "max-age=31536000; includeSubDomains",
            "Cache-Control", "no-cache, no-store, max-age=0, must-revalidate",
            "Pragma", "no-cache",
            "Expires", "0",
            "Content-Security-Policy", "default-src 'self'; frame-ancestors 'none';"
        );
        
        return new DelegatingRequestMatcherHeaderWriter(matcher, writer);
    }
}
