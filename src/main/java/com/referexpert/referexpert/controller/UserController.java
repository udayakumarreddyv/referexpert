package com.referexpert.referexpert.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.impl.MySQLServiceImpl;

@RestController
public class UserController {
    
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    Environment env;

    @Autowired
    private MySQLServiceImpl mySQLService;

    @GetMapping(value = "/referexpert/deactiveuser/{user}")
    public ResponseEntity<GenericResponse> deactivateUserAccount(@PathVariable("user") String email) {
        ResponseEntity<GenericResponse> entity = null;
        // If present deactive user status
        String criteria = " email = ?";
        if (mySQLService.selectUserProfile(email, criteria)) {
            int value = mySQLService.updateUserProfile(email, Constants.INACTIVE);
            if (value == 0) {
                entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                        HttpStatus.BAD_REQUEST);
            } else {
                entity = new ResponseEntity<>(new GenericResponse("Deactivated Successfully"), HttpStatus.OK);
            }
        } else {
            entity = new ResponseEntity<>(new GenericResponse("User doesn't exists"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }

    @GetMapping(value = "/referexpert/activeuser/{user}")
    public ResponseEntity<GenericResponse> activateUserAccount(@PathVariable("user") String email) {
        ResponseEntity<GenericResponse> entity = null;
        // If present active user status
        String criteria = " email = ?";
        if (mySQLService.selectUserProfile(email, criteria)) {
            int value = mySQLService.updateUserProfile(email, Constants.ACTIVE);
            if (value == 0) {
                entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                        HttpStatus.BAD_REQUEST);
            } else {
                entity = new ResponseEntity<>(new GenericResponse("Activated Successfully"), HttpStatus.OK);
            }
        } else {
            entity = new ResponseEntity<>(new GenericResponse("User doesn't exists"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }
    
    @PostMapping(value = "/referexpert/validateuser")
    public ResponseEntity<GenericResponse> validateUser(@RequestBody String credentials) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(credentials, UserRegistration.class);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + credentials);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        String userEmail = userRegistration.getEmail();
        String password = userRegistration.getPassword();
        String criteria = " email = '" + userEmail + "' and password = '" + password + "'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        if (users != null && users.size() > 0) {
            entity = new ResponseEntity<>(new GenericResponse("User Exists"), HttpStatus.OK);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("Invalid Username/Password"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }
    
    @GetMapping(value = "/referexpert/users/firstname/{firstname}")
    public ResponseEntity<List<UserRegistration>> selectUsersByFirstName(@PathVariable("firstname") String firstName) {
        String criteria = " first_name like '%" + firstName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/lastname/{lastname}")
    public ResponseEntity<List<UserRegistration>> selectUsersByLastName(@PathVariable("lastname") String lastName) {
        String criteria = " last_name like '%" + lastName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/referexpert/users/type/{type}")
    public ResponseEntity<List<UserRegistration>> selectUsersByType(@PathVariable("type") String type) {
        String criteria = " user_type like '%" + type + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/speciality/{speciality}")
    public ResponseEntity<List<UserRegistration>> selectUsersBySpeciality(@PathVariable("speciality") String speciality) {
        String criteria = " user_speciality like '%" + speciality + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
}
