package com.referexpert.referexpert.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.referexpert.referexpert.beans.ContactSupport;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.SupportContact;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.util.CommonUtils;

@RestController
@CrossOrigin()
public class SupportController {

	private final static Logger logger = LoggerFactory.getLogger(SupportController.class);

	@Autowired
	private MySQLService mySQLService;

	@Autowired
	private CommonUtils commonUtils;

	@PostMapping(value = "/contactsupport")
	public ResponseEntity<GenericResponse> contactSupport(@RequestBody ContactSupport contactSupport) {
		logger.info("SupportController :: In contactSupport");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String fromEmail = userDetails.getUsername();
		SupportContact supportContact = mySQLService.getSupportContact();
		commonUtils.contactSupport(supportContact, contactSupport.getSubject(),
				"Request from :: " + fromEmail + "\n\n" + contactSupport.getBody());
		return new ResponseEntity<>(new GenericResponse("Message sent to support team"), HttpStatus.OK);
	}
}
