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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.EmailSenderService;
import com.referexpert.referexpert.service.MySQLService;

@RestController
@CrossOrigin()
public class ReferExpertController {
    
    private final static Logger logger = LoggerFactory.getLogger(ReferExpertController.class);
    
    @Autowired
    private MySQLService mySQLService;
    
    @Autowired
    Environment env;
    
    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping(value = "/referexpert/requestappointment")
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
            entity = new ResponseEntity<>(new GenericResponse("User Already Exists"), HttpStatus.BAD_REQUEST);
        } else if (value == 888888) {
            entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
        } else if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in request appointment"), HttpStatus.BAD_REQUEST);
        } else {
        	sendNotification(appointment.getAppointmentTo(), Constants.APPOINTMENT_SUBJECT, 
        			Constants.APPOINTMENT_LOGIN_BODY + env.getProperty("referexpert.signin.url"));
            entity = new ResponseEntity<>(new GenericResponse("Appointment Request Successful"), HttpStatus.OK);
        }
        return entity;
    }

    @PostMapping(value = "/referexpert/rejectappointment")
    public ResponseEntity<GenericResponse> rejectAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In rejectAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.INACTIVE, Constants.APPOINTMENT);
        return entity;
    }
    
    @PostMapping(value = "/referexpert/acceptappointment")
    public ResponseEntity<GenericResponse> acceptAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In acceptAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.ACTIVE, Constants.APPOINTMENT);
        return entity;
    }
    
    @PostMapping(value = "/referexpert/finalizeappointment")
    public ResponseEntity<GenericResponse> finalizeAppointment(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In finalizeAppointment : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.ACTIVE, Constants.SERVICE);
        return entity;
    }
    
    private void sendNotification(String toEmail, String subject, String body) {
        logger.info("ReferExpertController :: In sendNotification to : " + toEmail);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setFrom(env.getProperty("spring.mail.username"));
        mailMessage.setText(body);
        emailSenderService.sendEmail(mailMessage);
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
    
    @GetMapping(value = "/referexpert/myreferrals/{useremail}")
    public ResponseEntity<List<Appointment>> getMyReferrals(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyReferrals  : " + userEmail);
        String criteria = " f.email = '" + userEmail + "'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/myappointments/{useremail}")
    public ResponseEntity<List<Appointment>> getMyAppointments(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyAppointments  : " + userEmail);
        String criteria = " t.email = '" + userEmail + "'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
}
