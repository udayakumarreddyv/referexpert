package com.referexpert.referexpert.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.referexpert.referexpert.beans.UserNotification;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.EmailSenderService;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.service.SMSSenderService;

@Component
public class CommonUtils {

	private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	@Autowired
    Environment env;
	
	@Autowired
    private SMSSenderService smsSenderService;
	
	@Autowired
    private EmailSenderService emailSenderService;
	
	@Autowired
    private MySQLService mySQLService;
	
	@Async
	public void sendNotification(String toEmail, String subject, String body, UserNotification userNotification) {
		logger.info("ReferExpertController :: In sendNotification to : " + toEmail);
		
		if (userNotification != null) {
			// Email Notification goes here.
			String notificationEmail = userNotification.getNotificationEmail();
			if (notificationEmail != null) {
				String[] emailArray = notificationEmail.split(",");
				for (String email : emailArray) {
					sendEmail(email, subject, body);
				}
			} else {
				sendEmail(toEmail, subject, body);
			}

			// SMS notification goes here
			String notificationMobile = userNotification.getNotificationMobile();
			if (notificationMobile != null) {
				String[] mobileArray = notificationMobile.split(",");
				for (String mobile : mobileArray) {
					try {
						smsSenderService.sendSMS(Constants.US_CODE + mobile, body);
					} catch (Exception e) {
						logger.error("Failed to send apppointment notification to :: " + mobile);
					}
				}
			} 
		}
	}
	
	private void sendEmail(String toEmail, String subject, String body) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(toEmail);
			mailMessage.setSubject(subject);
			mailMessage.setFrom(env.getProperty("spring.mail.username"));
			mailMessage.setFrom(env.getProperty("spring.mail.replyto"));
			mailMessage.setText(body);
			emailSenderService.sendEmail(mailMessage);
			logger.info("Appointment notification sent to :: " + toEmail);
		} catch (Exception e) {
			logger.error("Failed to send apppointment notification to :: " + toEmail);
		}
	}
	
	public UserNotification getUserNotifications(String userEmail) {
    	logger.info("RegistrationController :: In getuserNotifications : " + userEmail);
        String criteria = " email = '" + userEmail + "'";
        UserNotification userNotification =  mySQLService.selectUserNotification(criteria);
        return userNotification;
    }
}
