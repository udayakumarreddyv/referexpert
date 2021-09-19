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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.referexpert.referexpert.service.MySQLService;

@RestController
@CrossOrigin()
public class RegistrationController {

    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    Environment env;

    @Autowired
    private MySQLService mySQLService;

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

    @GetMapping(value = "/usertypes")
    public ResponseEntity<List<UserType>> getUserTypes() {
        logger.info("RegistrationController :: In getUserTypes");
        List<UserType> usertypes = mySQLService.selectAllUserTypes();
        ResponseEntity<List<UserType>> entity = new ResponseEntity<>(usertypes, HttpStatus.OK);
        return entity;
    }

    @GetMapping(value = "/usertype/{usertype}")
    public ResponseEntity<List<UserType>> getUserTypeByUserType(@PathVariable("usertype") String usertype) {
        logger.info("RegistrationController :: In getUserTypeByUserType : " + usertype);
        List<UserType> usertypes = mySQLService
                .selectUserTypeByUserType(usertype != null ? usertype.toUpperCase() : "");
        ResponseEntity<List<UserType>> entity = new ResponseEntity<>(usertypes, HttpStatus.OK);
        return entity;
    }

    @GetMapping(value = "/usertype/{usertype}/userspecialities")
    public ResponseEntity<UserSpeciality> getUserSpecialitiesByUserType(@PathVariable("usertype") String usertype) {
        logger.info("RegistrationController :: In getUserSpecialitiesByUserType : " + usertype);
        UserSpeciality userSpeciality = mySQLService
                .selectUserSpecialityByUserType(usertype != null ? usertype.toUpperCase() : "");
        ResponseEntity<UserSpeciality> entity = new ResponseEntity<>(userSpeciality, HttpStatus.OK);
        return entity;
    }
    
    @PostMapping(value = "/registeruser")
    public ResponseEntity<GenericResponse> registerUser(@RequestBody String registration,
            @RequestParam("referralid") String referralId) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(registration, UserRegistration.class);
            logger.info("RegistrationController :: In registerUser : " + userRegistration.getEmail() + " referral : "
                    + referralId);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + registration);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        if (mySQLService.selectUserReferral(referralId, userRegistration.getEmail())) {
            userRegistration.setUserId(UUID.randomUUID().toString());
            userRegistration.setPassword(bcryptEncoder.encode(userRegistration.getPassword()));
            int value = mySQLService.insertUserProfile(userRegistration);
            if (value == 999999) {
                entity = new ResponseEntity<>(new GenericResponse("User Already Exists"), HttpStatus.BAD_REQUEST);
            } else if (value == 888888) {
                entity = new ResponseEntity<>(new GenericResponse("Reference Data Incorrect"), HttpStatus.BAD_REQUEST);
            } else if (value == 0) {
                entity = new ResponseEntity<>(new GenericResponse("Issue in Registration"), HttpStatus.BAD_REQUEST);
            } else {
                // Store confirmation in table and send email
                ConfirmationToken confirmationToken = new ConfirmationToken(userRegistration);
                mySQLService.insertConfirmationToken(confirmationToken);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(userRegistration.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setFrom(env.getProperty("spring.mail.username"));
                mailMessage.setFrom(env.getProperty("spring.mail.replyto"));
                mailMessage.setText("To confirm your account, please click here : "
                        + env.getProperty("referexpert.confirm.account.url") + "?email=" + userRegistration.getEmail()
                        + "&token=" + confirmationToken.getConfirmationToken());
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

    @GetMapping(value = "/confirmaccount")
    public ResponseEntity<GenericResponse> confirmUserAccount(@RequestParam("token") String token,
            @RequestParam("user") String email) {
        logger.info("RegistrationController :: In confirmUserAccount token : " + token + " email : " + email);
        ResponseEntity<GenericResponse> entity = null;
        // check for token present in database or not. If present active user status
        String criteria = " email = ?";
        if (mySQLService.selectConfirmationToken(token) && mySQLService.selectUserProfile(email, criteria)) {
            int value = mySQLService.updateUserActivation(email, Constants.ACTIVE);
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

    @PostMapping(value = "/referuser")
    public ResponseEntity<GenericResponse> referUser(@RequestBody String referString) {
        logger.info("RegistrationController :: In referUser : " + referString);
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
        logger.info("RegistrationController :: In processReferral : " + userEmail + " : " + tokenizer);
        ResponseEntity<GenericResponse> entity = null;
        String referralId = UUID.randomUUID().toString();
        int value = mySQLService.insertUserReferral(referralId, userEmail, tokenizer, Constants.INACTIVE);
        if (value != 0) {
            emailSenderService.sendReferralEMail(tokenizer, referralId);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("Referral not Successful"), HttpStatus.BAD_REQUEST);
        }
        return entity;
    }
    
    @GetMapping(value = "/users/{email}")
    public ResponseEntity<UserRegistration> selectUserProfile(@PathVariable("email") String email) {
        logger.info("RegistrationController :: In selectUserProfile : " + email);
        UserRegistration userRegistration = getUserDetails(email);
        return new ResponseEntity<UserRegistration>(userRegistration, HttpStatus.OK);
    }

    private UserRegistration getUserDetails(String email) {
        logger.info("RegistrationController :: In getUserDetails : " + email);
        String criteria = " email = '" + email + "'";
        UserRegistration userRegistration =  mySQLService.selectUser(criteria);
        userRegistration.setPassword(null);
        return userRegistration;
    }
    
    @GetMapping(value = "/userdetails")
    public ResponseEntity<UserRegistration> selectUserDetails() {
        logger.info("RegistrationController :: In selectUserDetails");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserRegistration userRegistration = getUserDetails(userDetails.getUsername());
        return new ResponseEntity<UserRegistration>(userRegistration, HttpStatus.OK);
    }
    
    @PostMapping(value = "/userprofile")
    public ResponseEntity<GenericResponse> updateprofile(@RequestBody String registration) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(registration, UserRegistration.class);
            logger.info("RegistrationController :: In updateprofile : " + userRegistration.getEmail());
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + registration);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        int value = mySQLService.updateUserProfile(userRegistration);
        if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                    HttpStatus.BAD_REQUEST);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("User profile updated Successfully"), HttpStatus.OK);
        }
        return entity;
    }
 
    @PostMapping(value = "/updatepassword")
    public ResponseEntity<GenericResponse> updatePassword(@RequestBody String registration) {
        return managePassword(registration,"update");
    }
    
    @PostMapping(value = "/resetnotification")
    public ResponseEntity<GenericResponse> resetNotification(@RequestBody String registration) {
        ObjectMapper mapper = new ObjectMapper();
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(registration, UserRegistration.class);
            logger.info("RegistrationController :: In resetNotification : " + userRegistration.getEmail());
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + registration);
            logger.error("Exception as follows :: " + e);
            return new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        UserRegistration user = getUserDetails(userRegistration.getEmail());
        if (user.getUserId() != null) {
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            mySQLService.insertConfirmationToken(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Password rest request");
            mailMessage.setFrom(env.getProperty("spring.mail.username"));
            mailMessage.setFrom(env.getProperty("spring.mail.replyto"));
            mailMessage.setText("Please click on below link to reset your password :: "
                    + env.getProperty("referexpert.resetpass.url") + "?email=" + userRegistration.getEmail()
                    + "&token=" + confirmationToken.getConfirmationToken());
            emailSenderService.sendEmail(mailMessage);
            return new ResponseEntity<>(new GenericResponse("Email sent successful"), HttpStatus.OK);
        } else {
            logger.error("User not exists to send reset notification");
            return new ResponseEntity<>(new GenericResponse("User doesn't exist in system"), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value = "/resetpassword")
    public ResponseEntity<GenericResponse> resetPassword(@RequestBody String registration,
            @RequestParam("token") String token) {
        if (mySQLService.selectConfirmationToken(token)) {
            mySQLService.deleteConfirmationToken(token);
            return managePassword(registration, "reset");
        } else {
            return new ResponseEntity<>(new GenericResponse("The link is invalid or broken!"), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<GenericResponse> managePassword(String registration, String action) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(registration, UserRegistration.class);
            logger.info("RegistrationController :: In managePassword : " + userRegistration.getEmail());
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + registration);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        int value = mySQLService.updateUserPassword(userRegistration.getEmail(),
                bcryptEncoder.encode(userRegistration.getPassword()));
        if (value == 0) {
            entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                    HttpStatus.BAD_REQUEST);
        } else {
            if ("reset".equals(action)) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(userRegistration.getEmail());
                mailMessage.setSubject("Password rest successful");
                mailMessage.setFrom(env.getProperty("spring.mail.username"));
                mailMessage.setFrom(env.getProperty("spring.mail.replyto"));
                mailMessage.setText("Your pasword reset succesful, please login here "
                        + env.getProperty("referexpert.signin.url") + " with your new password");
                emailSenderService.sendEmail(mailMessage);
            }
            entity = new ResponseEntity<>(new GenericResponse("Password updated Successfully"), HttpStatus.OK);
        }
        return entity;
    }
}
