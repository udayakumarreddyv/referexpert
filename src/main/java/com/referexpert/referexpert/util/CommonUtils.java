package com.referexpert.referexpert.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.referexpert.referexpert.beans.SupportContact;
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
		logger.info("CommonUtils :: In sendNotification to : " + toEmail);
		
		if (userNotification != null) {
			// Email Notification goes here.
			String notificationEmail = userNotification.getNotificationEmail();
			if (notificationEmail != null) {
				sendEmail(notificationEmail, subject, body);
			} else {
				sendEmail(toEmail, subject, body);
			}

			// SMS notification goes here
			String notificationMobile = userNotification.getNotificationMobile();
			sendSMS(body, notificationMobile); 
		} else {
			sendEmail(toEmail, subject, body);
		}
	}

	@Async
	public void contactSupport(SupportContact supportContact, String subject, String body) {
		logger.info("CommonUtils :: In contactSupport");
		
		if (supportContact != null) {
			// Email Notification goes here.
			sendEmail(supportContact.getEmail(), subject, body);
			
			// SMS notification goes here
			String notificationMobile = supportContact.getMobile();
			sendSMS(body, notificationMobile); 
		}
	}
	
	private void sendSMS(String body, String notificationMobile) {
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
	
	private void sendEmail(String toEmail, String subject, String body) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			//mailMessage.setTo(toEmail);
			if (toEmail.contains(",")) {
	            mailMessage.setTo(toEmail.split(","));
	        } else {
	            mailMessage.setTo(toEmail);
	        }
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
