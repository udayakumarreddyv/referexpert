package com.referexpert.referexpert.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.referexpert.referexpert.constant.Constants;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service("smsSenderService")
public class SMSSenderService {

	private final static Logger logger = LoggerFactory.getLogger(SMSSenderService.class);

	@Autowired
	Environment env;

	public void sendSMS(String toPhoneNumber, String textMessage) {
		Message message = Message.creator(new PhoneNumber(toPhoneNumber),
				new PhoneNumber(env.getProperty(Constants.TWILIO_FROM_PHONE_NUMBER)), textMessage).create();

		logger.info("SMS Sent to phone :: " + toPhoneNumber + " and response code is :: " + message.getSid());
	}
}