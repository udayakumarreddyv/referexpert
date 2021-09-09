package com.referexpert.referexpert.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.referexpert.referexpert.constant.Constants;
import com.twilio.Twilio;

@Configuration
public class TwilioAPIContext {
    
    private final static Logger logger = LoggerFactory.getLogger(TwilioAPIContext.class);
    
    @Autowired
    Environment env;
    
    @Bean
    public void twilioApiContext() {
        logger.info("TwilioAPIContext :: Creating twilioApiContext");
        Twilio.init(env.getProperty(Constants.TWILIO_ACCOUNT_ID), env.getProperty(Constants.TWILIO_ACCOUNT_AUTH));
        logger.info("TwilioAPIContext ::Twilio Context created successfully");
    }
}
