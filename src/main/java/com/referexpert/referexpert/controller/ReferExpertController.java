package com.referexpert.referexpert.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserNotification;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.EmailSenderService;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.service.SMSSenderService;

@RestController
@CrossOrigin()
@EnableAsync
public class ReferExpertController {
    
    private final static Logger logger = LoggerFactory.getLogger(ReferExpertController.class);
    
    @Autowired
    private MySQLService mySQLService;
    
    @Autowired
    Environment env;
    
    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    private SMSSenderService smsSenderService;

    @PostMapping(value = "/requestappointment")
    public ResponseEntity<GenericResponse> requestAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In requestAppointment : " + appointmentString);
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        Appointment appointment = null;
        try {
            appointment = mapper.readValue(appointmentString, Appointment.class);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + appointmentString);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + appointment != null ? appointment.toString() : null);
        appointment.setAppointmentId(UUID.randomUUID().toString());
        int value = mySQLService.insertAppointment(appointment);
        if (value == 999999) {
            entity = new ResponseEntity<>(new GenericResponse("Unable to insert appointment due to unique constraint"), HttpStatus.BAD_REQUEST);
        } else if (value == 888888) {
            entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
        } else if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in request appointment"), HttpStatus.BAD_REQUEST);
        } else {
        	sendNotification(appointment.getAppointmentTo(), Constants.APPOINTMENT_SUBJECT, 
        			Constants.APPOINTMENT_REQUESTED.replaceAll("DATEANDTIMESTAMP",
        					appointment.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"));
            entity = new ResponseEntity<>(new GenericResponse("Appointment Request Successful"), HttpStatus.OK);
        }
        return entity;
    }

    @PostMapping(value = "/rejectappointment")
    public ResponseEntity<GenericResponse> rejectAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In rejectAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.INACTIVE, Constants.APPOINTMENT);
        return entity;
    }
    
    @PostMapping(value = "/acceptappointment")
    public ResponseEntity<GenericResponse> acceptAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In acceptAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.ACTIVE, Constants.APPOINTMENT);
        return entity;
    }
    
    @PostMapping(value = "/finalizeappointment")
    public ResponseEntity<GenericResponse> finalizeAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In finalizeAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.ACTIVE, Constants.SERVICE);
        return entity;
    }
    
    @Async
	private void sendNotification(String toEmail, String subject, String body) {
		logger.info("ReferExpertController :: In sendNotification to : " + toEmail);
		UserNotification userNotification = getuserNotifications(toEmail);

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
    
	private ResponseEntity<GenericResponse> updateStatus(String appointmentString, String status, String type) {
		logger.info("ReferExpertController :: In updateStatus  : " + appointmentString + " : " + status + " : " + type);
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<GenericResponse> entity = null;
		Appointment appointment = null;
		try {
			appointment = mapper.readValue(appointmentString, Appointment.class);
		} catch (Exception e) {
			logger.error("Unable to parse the input :: " + appointmentString);
			logger.error("Exception as follows :: " + e);
			entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
		}
		logger.info("JSON to Object Conversion :: " + appointment != null ? appointment.toString() : null);
		String appointmentId = appointment.getAppointmentId();
		String criteria = " appointment_id = '" + appointmentId + "'";
		Appointment appointmentFromDB = mySQLService.selectAppointmentById(criteria);
		int value = 0;
		if (Constants.SERVICE.equals(type)) {
			value = mySQLService.updateAppointmentServed(appointmentId, status);
			sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
					Constants.APPOINTMENT_COMPLETED.replaceAll("DATEANDTIMESTAMP",
							appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"));
		} else {
			if (Constants.INACTIVE.equals(status)) {
				mySQLService.updateAppointmentServed(appointmentId, Constants.INACTIVE);
				value = mySQLService.updateAppointmentAccepted(appointmentId, status);
				sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
						Constants.APPOINTMENT_REJECTED.replaceAll("DATEANDTIMESTAMP",
								appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
								+ env.getProperty("referexpert.signin.url"));
			} else {
				value = mySQLService.updateAppointmentAccepted(appointmentId, status);
				sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
						Constants.APPOINTMENT_ACCEPTED.replaceAll("DATEANDTIMESTAMP",
								appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
								+ env.getProperty("referexpert.signin.url"));
			}
		}
		if (value == 0) {
			entity = new ResponseEntity<>(new GenericResponse("Issue while updating refer expert"),
					HttpStatus.BAD_REQUEST);
		} else {
			entity = new ResponseEntity<>(new GenericResponse("Updated Successfully"), HttpStatus.OK);
		}
		return entity;
	}
    
    @GetMapping(value = "/myreferrals/{useremail}")
    public ResponseEntity<List<Appointment>> getMyReferrals(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyReferrals  : " + userEmail);
        String criteria = " f.email = '" + userEmail + "'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/myappointments/{useremail}")
    public ResponseEntity<List<Appointment>> getMyAppointments(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyAppointments  : " + userEmail);
        String criteria = " t.email = '" + userEmail + "'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/notification")
    public ResponseEntity<UserNotification> selectUserNotifications() {
        logger.info("RegistrationController :: In selectUserNotifications");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserNotification userNotification = getuserNotifications(userDetails.getUsername());
        if(userNotification != null) {
        	userNotification.setUserEmail(null);
        	return new ResponseEntity<UserNotification>(userNotification, HttpStatus.OK);
        } else {
        	return new ResponseEntity<UserNotification>(new UserNotification(), HttpStatus.OK);
        }
    }
    
    @PostMapping(value = "/notification")
    public ResponseEntity<GenericResponse> upsertNotifications(@RequestBody String notification) {
        logger.info("RegistrationController :: In upsertNotifications");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserNotification userNotification = null;
        try {
        	userNotification = mapper.readValue(notification, UserNotification.class);
            logger.info("RegistrationController :: In upsertNotifications : " + userDetails.getUsername());
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + notification);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        int value = mySQLService.upsertUserNotification(userNotification, userDetails.getUsername());
        if (value == 999999 || value == 888888 || value == 0) {
            entity = new ResponseEntity<GenericResponse>(new GenericResponse("Issue in userting user_notification"), HttpStatus.BAD_REQUEST);
        } else { 
            entity = new ResponseEntity<GenericResponse>(new GenericResponse("Notifications inserted/updated Successfully"), HttpStatus.OK);
        }
        return entity;
    }
    
    private UserNotification getuserNotifications(String userEmail) {
    	logger.info("RegistrationController :: In getuserNotifications : " + userEmail);
        String criteria = " email = '" + userEmail + "'";
        UserNotification userNotification =  mySQLService.selectUserNotification(criteria);
        return userNotification;
    }
}
