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
import com.referexpert.referexpert.beans.PendingTask;
import com.referexpert.referexpert.beans.UserNotification;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.util.CommonUtils;

@RestController
@CrossOrigin()
@EnableAsync
public class AvailabilityController {
    
    private final static Logger logger = LoggerFactory.getLogger(AvailabilityController.class);
    
    @Autowired
    private MySQLService mySQLService;
    
    @Autowired
    Environment env;
    
    @Autowired
    private CommonUtils commonUtils;
    
    @PostMapping(value = "/availabilityrequest")
    public ResponseEntity<GenericResponse> availabilityRequest(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In availabilityRequest : " + appointmentString);
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
        int value = mySQLService.insertAppointment(appointment, Constants.APPOINTMENT_REQUEST);
        if (value == 999999) {
            entity = new ResponseEntity<>(new GenericResponse("Unable to insert appointment due to unique constraint"), HttpStatus.BAD_REQUEST);
        } else if (value == 888888) {
            entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
        } else if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in request appointment"), HttpStatus.BAD_REQUEST);
        } else {
        	UserNotification userNotification = commonUtils.getUserNotifications(appointment.getAppointmentTo());
        	commonUtils.sendNotification(appointment.getAppointmentTo(), Constants.AVAILABILITY_NOTIFICATION, 
        			Constants.AVAILABILITY_REQUESTED + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
            entity = new ResponseEntity<>(new GenericResponse("Appointment Request Inserted Successful"), HttpStatus.OK);
        }
        return entity;
    }
    
    @PostMapping(value = "/availabilityresponse")
    public ResponseEntity<GenericResponse> availabilityResponse(@RequestBody String appointmentString) {
        logger.info("ReferExpertController :: In availabilityResponse : " + appointmentString);
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
        
        int value = mySQLService.updateAppointmentResponse(appointment.getAppointmentId(), appointment.getDateAndTimeString());

        if (value == 0) {
			entity = new ResponseEntity<>(new GenericResponse("Issue while updating appointment table"),
					HttpStatus.BAD_REQUEST);
		} else {
			String appointmentId = appointment.getAppointmentId();
			String criteria = " appointment_id = '" + appointmentId + "'";
			Appointment appointmentFromDB = mySQLService.selectAppointmentById(criteria);
			
			UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
			commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.AVAILABILITY_NOTIFICATION,
					Constants.AVAILABILITY_RESPONDED + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
	        entity = new ResponseEntity<>(new GenericResponse("Appointment Response Updated Successful"), HttpStatus.OK);
		}
       
        return entity;
    }
    
    @GetMapping(value = "/myavailabilityrequests/{useremail}")
    public ResponseEntity<List<Appointment>> getMyAvailabilityRequest(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyAvailabilityRequest  : " + userEmail);
        String criteria = " f.email = '" + userEmail + "' and is_avail = 'Y'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/myavailabilityresponses/{useremail}")
    public ResponseEntity<List<Appointment>> getMyAvailabilityRespone(@PathVariable("useremail") String userEmail) {
        logger.info("ReferExpertController :: In getMyAvailabilityRespone  : " + userEmail);
        String criteria = " t.email = '" + userEmail + "' and is_avail = 'Y'";
        List<Appointment> appointments =  mySQLService.selectAppointments(criteria);
        return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
    }
    
    @GetMapping(value = "/pendingtasks")
	public ResponseEntity<PendingTask> getPendingTasks() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("ReferExpertController :: In getPendingTasks  : " + userDetails.getUsername());
		PendingTask pendingTask = new PendingTask();
		pendingTask.setPendingAppointment(mySQLService.getPendingTasks(Constants.RESPONSE, Constants.PENDING,
				Constants.INACTIVE, userDetails.getUsername()) ? Constants.ACTIVE : Constants.INACTIVE);
		pendingTask.setCurrentAppointment(mySQLService.getPendingTasks(Constants.RESPONSE, Constants.ACTIVE,
				Constants.INACTIVE, userDetails.getUsername()) ? Constants.ACTIVE : Constants.INACTIVE);
		pendingTask.setPendingAvailabilityResponse(mySQLService.getPendingTasks(Constants.RESPONSE, Constants.PENDING,
				Constants.ACTIVE, userDetails.getUsername()) ? Constants.ACTIVE : Constants.INACTIVE);
		pendingTask.setPendingAvailabilityRequest(mySQLService.getPendingTasks(Constants.REQUEST, Constants.PENDING,
				Constants.ACTIVE, userDetails.getUsername()) ? Constants.ACTIVE : Constants.INACTIVE);
		return new ResponseEntity<PendingTask>(pendingTask, HttpStatus.OK);
	}
    
    @PostMapping(value = "/finalizeavailability")
    public ResponseEntity<GenericResponse> finalizeAvailability(@RequestBody String appointmentString) {
        logger.info("AvailabilityController :: In finalizeAvailability : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.ACTIVE, Constants.SERVICE);
        return entity;
    }
    
    @PostMapping(value = "/rejectavailability")
    public ResponseEntity<GenericResponse> rejectAvailability(@RequestBody String appointmentString) {
        logger.info("AvailabilityController :: In rejectAvailability : " + appointmentString);
        ResponseEntity<GenericResponse> entity = updateStatus(appointmentString, Constants.INACTIVE, Constants.SERVICE);
        return entity;
    }
    
	private ResponseEntity<GenericResponse> updateStatus(String appointmentString, String status, String type) {
		logger.info("AvailabilityController :: In updateStatus  : " + appointmentString + " : " + status + " : " + type);
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
		if (Constants.ACTIVE.equals(status)) {
			value = mySQLService.updateAppointmentServed(appointmentId, status);
			UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
			commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.AVAILABILITY_NOTIFICATION,
					Constants.AVAILABILITY_COMPLETED + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
		} else {
			value = mySQLService.updateAppointmentServed(appointmentId, status);
			UserNotification userNotification = commonUtils.getUserNotifications(appointmentFromDB.getAppointmentFrom());
			commonUtils.sendNotification(appointmentFromDB.getAppointmentFrom(), Constants.AVAILABILITY_NOTIFICATION,
					Constants.AVAILABILITY_REJECTED + Constants.APPOINTMENT_LOGIN_BODY
							+ env.getProperty("referexpert.signin.url"), userNotification);
		}
		if (value == 0) {
			entity = new ResponseEntity<>(new GenericResponse("Issue while updating appointment table"),
					HttpStatus.BAD_REQUEST);
		} else {
			entity = new ResponseEntity<>(new GenericResponse("Updated Successfully"), HttpStatus.OK);
		}
		return entity;
	}
}
