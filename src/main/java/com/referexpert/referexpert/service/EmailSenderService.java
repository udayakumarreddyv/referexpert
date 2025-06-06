package com.referexpert.referexpert.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.referexpert.referexpert.constant.Constants;

@Service("emailSenderService")
public class EmailSenderService {

	public static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
	
    private JavaMailSender javaMailSender;
    
    @Autowired
    Environment env;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    
	public void sendReferralEMail(String email, String referralId) {
		logger.info("Sending Referral email to :: " + email);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject(Constants.WELCOME_SUBJECT);
		mailMessage.setFrom(env.getProperty("spring.mail.username"));
		mailMessage.setFrom(env.getProperty("spring.mail.replyto"));
		mailMessage.setText(Constants.WELCOME_MESSAGE + env.getProperty("referexpert.register.url") + "?email=" + email
				+ "&token=" + referralId);
		sendEmail(mailMessage);
	}
}