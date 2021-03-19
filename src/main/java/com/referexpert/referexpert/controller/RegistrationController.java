package com.referexpert.referexpert.controller;

import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.ReferUser;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.EmailSenderService;
import com.referexpert.referexpert.service.impl.MySQLServiceImpl;

@RestController
@CrossOrigin()
public class RegistrationController {

    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    Environment env;

    @Autowired
    private MySQLServiceImpl mySQLService;

    @Autowired
    private EmailSenderService emailSenderService;
    
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @GetMapping(value = "/health")
    public ResponseEntity health() {
        logger.debug("I am still alive and healthy");
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/authtest")
    public ResponseEntity<GenericResponse> getAuth() {
        logger.debug("Hurray.. Authentication working");
        ResponseEntity<GenericResponse> entity = new ResponseEntity<>(new GenericResponse("Authentication working"),
                HttpStatus.OK);
        return entity;
    }

    @GetMapping(value = "/referexpert/usertypes")
    public ResponseEntity<List<UserType>> getUserTypes() {
        List<UserType> usertypes = mySQLService.selectAllUserTypes();
        ResponseEntity<List<UserType>> entity = new ResponseEntity<>(usertypes, HttpStatus.OK);
        return entity;
    }

    @GetMapping(value = "/referexpert/usertype/{usertype}")
    public ResponseEntity<List<UserType>> getUserTypeByUserType(@PathVariable("usertype") String usertype) {
        List<UserType> usertypes = mySQLService
                .selectUserTypeByUserType(usertype != null ? usertype.toUpperCase() : "");
        ResponseEntity<List<UserType>> entity = new ResponseEntity<>(usertypes, HttpStatus.OK);
        return entity;
    }

    @GetMapping(value = "/referexpert/usertype/{usertype}/userspecialities")
    public ResponseEntity<UserSpeciality> getUserSpecialitiesByUserType(@PathVariable("usertype") String usertype) {
        UserSpeciality userSpeciality = mySQLService
                .selectUserSpecialityByUserType(usertype != null ? usertype.toUpperCase() : "");
        ResponseEntity<UserSpeciality> entity = new ResponseEntity<>(userSpeciality, HttpStatus.OK);
        return entity;
    }
    
    @PostMapping(value = "/referexpert/registeruser")
    public ResponseEntity<GenericResponse> registerUser(@RequestBody String registration,
            @RequestParam("referralid") String referralId) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(registration, UserRegistration.class);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + registration);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        if (mySQLService.selectUserReferral(referralId)) {
            userRegistration.setUserId(UUID.randomUUID().toString());
            userRegistration.setPassword(bcryptEncoder.encode(userRegistration.getPassword()));
            int value = mySQLService.insertUserProfile(userRegistration);
            if (value == 999999) {
                entity = new ResponseEntity<>(new GenericResponse("User Already Exists"), HttpStatus.BAD_REQUEST);
            } else if (value == 888888) {
                entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
            } else {
                // Store confirmation in table and send email
                ConfirmationToken confirmationToken = new ConfirmationToken(userRegistration);
                mySQLService.insertConfirmationToken(confirmationToken);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(userRegistration.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setFrom(env.getProperty("spring.mail.username"));
                mailMessage.setText("To confirm your account, please click here : " + env.getProperty("application.url")
                        + "referexpert/confirmaccount?token=" + confirmationToken.getConfirmationToken() + "&user="
                        + userRegistration.getEmail());
                emailSenderService.sendEmail(mailMessage);
                // Once registered mark record as registered
                mySQLService.updateUserReferral(userRegistration.getEmail(), Constants.ACTIVE);
                entity = new ResponseEntity<>(new GenericResponse("Registration Successful"), HttpStatus.OK);
            }
        } else {
            entity = new ResponseEntity<>(new GenericResponse("The link is invalid or broken!"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    @GetMapping(value = "/referexpert/confirmaccount")
    public ResponseEntity<GenericResponse> confirmUserAccount(@RequestParam("token") String token,
            @RequestParam("user") String email) {
        ResponseEntity<GenericResponse> entity = null;
        // check for token present in database or not. If present active user status
        String criteria = " email = ?";
        if (mySQLService.selectConfirmationToken(token) && mySQLService.selectUserProfile(email, criteria)) {
            int value = mySQLService.updateUserProfile(email, Constants.ACTIVE);
            if (value == 0) {
                entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                        HttpStatus.BAD_REQUEST);
            } else {
                // delete confirmation token as user is activated
                mySQLService.deleteConfirmationToken(token);
                entity = new ResponseEntity<>(new GenericResponse("Account Verified Successfully"), HttpStatus.OK);
            }
        } else {
            entity = new ResponseEntity<>(new GenericResponse("The link is invalid or broken!"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    @PostMapping(value = "/referexpert/referuser")
    public ResponseEntity<GenericResponse> referUser(@RequestBody String referString) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        ReferUser referUser= null;
        try {
            referUser = mapper.readValue(referString, ReferUser.class);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + referString);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + referUser != null ? referUser.toString() : null);
        String criteria = " email = ?";
        String userEmail = referUser.getUserEmail();
        String docEmail = referUser.getDocEmail();
        if (mySQLService.selectUserProfile(userEmail, criteria)) {
            if (!docEmail.contains(",")) {
                entity = processReferral(userEmail, docEmail);
            } else {
                StringTokenizer tokenizer = new StringTokenizer(docEmail, ",");
                while (tokenizer.hasMoreTokens()) {
                    // Store data in table and send email
                    entity = processReferral(userEmail, tokenizer.nextToken());
                }
            }
            entity = new ResponseEntity<>(new GenericResponse("Referral Successful"), HttpStatus.OK);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("Referrer is not registed with system"),
                    HttpStatus.BAD_REQUEST);
        }
        return entity;
    }

    private ResponseEntity<GenericResponse> processReferral(String userEmail, String tokenizer) {
        ResponseEntity<GenericResponse> entity = null;
        String referralId = UUID.randomUUID().toString();
        int value = mySQLService.insertUserReferral(referralId, userEmail, tokenizer, Constants.INACTIVE);
        if (value != 0) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(tokenizer);
            mailMessage.setSubject("You Referred to registered in ReferExpert Network");
            mailMessage.setFrom(env.getProperty("spring.mail.username"));
            mailMessage.setText(
                    "Congratulations!! Please register with ReferExpert using the below link :: <Link to registration page of website>"
                            + " Referral id :: " + referralId);
            emailSenderService.sendEmail(mailMessage);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("Referral not Successful"), HttpStatus.BAD_REQUEST);
        }
        return entity;
    }
}
