package com.referexpert.referexpert.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.util.CommonUtils;

@RestController
@CrossOrigin()
@EnableAsync
public class AppointmentController {
    
    private final static Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private MySQLService mySQLService;
    
    @Autowired
    private CommonUtils commonUtils;
    
    @Autowired
    Environment env;
    
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
        int value = mySQLService.insertAppointment(appointment, Constants.APPOINTMENT);
        if (value == 999999) {
            entity = new ResponseEntity<>(new GenericResponse("Unable to insert appointment due to unique constraint"), HttpStatus.BAD_REQUEST);
        } else if (value == 888888) {
            entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
        } else if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in request appointment"), HttpStatus.BAD_REQUEST);
        } else {
        	UserNotification userNotification = commonUtils.getUserNotifications(appointment.getAppointmentTo());
        	commonUtils.sendNotification(appointment.getAppointmentTo(), Constants.APPOINTMENT_SUBJECT, 
        			Constants.APPOINTMENT_REQUESTED.replaceAll("DATEANDTIMESTAMP",
        					appointment.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
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
			UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
			commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
					Constants.APPOINTMENT_COMPLETED.replaceAll("DATEANDTIMESTAMP",
							appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
		} else {
			if (Constants.INACTIVE.equals(status)) {
				mySQLService.updateAppointmentServed(appointmentId, Constants.INACTIVE);
				value = mySQLService.updateAppointmentAccepted(appointmentId, status);
				UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
				commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
						Constants.APPOINTMENT_REJECTED.replaceAll("DATEANDTIMESTAMP",
								appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
								+ env.getProperty("referexpert.signin.url"), userNotification);
			} else {
				value = mySQLService.updateAppointmentAccepted(appointmentId, status);
				UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
				commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.APPOINTMENT_SUBJECT,
						Constants.APPOINTMENT_ACCEPTED.replaceAll("DATEANDTIMESTAMP",
								appointmentFromDB.getDateAndTimeString()) + Constants.APPOINTMENT_LOGIN_BODY
								+ env.getProperty("referexpert.signin.url"), userNotification);
			}
		}
		if (value == 0) {
			entity = new ResponseEntity<>(new GenericResponse("Issue while updating appointment table"),
					HttpStatus.BAD_REQUEST);
		} else {
			entity = new ResponseEntity<>(new GenericResponse("Updated Successfully"), HttpStatus.OK);
		}
		return entity;
	}
    
    @GetMapping(value = "/myreferrals/{useremail}")
    public ResponseEntity<List<Appointment>> getMyReferrals(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyReferrals  : " + userEmail);
        String criteria = " f.email = '" + userEmail + "' and is_avail = 'N'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/myappointments/{useremail}")
    public ResponseEntity<List<Appointment>> getMyAppointments(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyAppointments  : " + userEmail);
        String criteria = " t.email = '" + userEmail + "' and is_avail = 'N'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/notification")
    public ResponseEntity<UserNotification> selectUserNotifications() {
        logger.info("RegistrationController :: In selectUserNotifications");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserNotification userNotification = commonUtils.getUserNotifications(userDetails.getUsername());
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
    
}
