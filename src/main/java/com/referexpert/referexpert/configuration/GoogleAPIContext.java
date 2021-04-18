package com.referexpert.referexpert.configuration;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.maps.GeoApiContext;
import com.referexpert.referexpert.constant.Constants;

@Configuration
public class GoogleAPIContext {
    @Autowired
    Environment env;
    
    @Bean
    @ConfigurationProperties(prefix = "google")
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(env.getProperty(Constants.GOOGLE_API_KEY)).build();
    }
}
