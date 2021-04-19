package com.referexpert.referexpert.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.maps.GeoApiContext;
import com.referexpert.referexpert.constant.Constants;

@Configuration
public class GoogleAPIContext {
    
    private final static Logger logger = LoggerFactory.getLogger(GoogleAPIContext.class);
    
    @Autowired
    Environment env;
    
    @Bean
    @ConfigurationProperties(prefix = "google")
    public GeoApiContext geoApiContext() {
        logger.info("GoogleAPIContext :: Creating GeoApiContext");
        return new GeoApiContext.Builder().apiKey(env.getProperty(Constants.GOOGLE_API_KEY)).build();
    }
}
