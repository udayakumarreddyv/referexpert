package com.referexpert.referexpert.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.impl.MySQLServiceImpl;

@RestController
public class UserController {

    @Autowired
    Environment env;

    @Autowired
    private MySQLServiceImpl mySQLService;

    @RequestMapping(value = "/referexpert/deactiveuser/{user}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/referexpert/activeuser/{user}", method = RequestMethod.GET)
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
    
    @RequestMapping(value = "/referexpert/validateuser", method = RequestMethod.GET)
    public ResponseEntity<GenericResponse> validateUser(@RequestParam("email") String userEmail,
            @RequestParam("password") String password) {
        ResponseEntity<GenericResponse> entity = null;
        String criteria = " email = '" + userEmail + "' and password = '" + password + "'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        if (users != null && users.size() > 0) {
            entity = new ResponseEntity<>(new GenericResponse("User Exists"), HttpStatus.OK);
        } else {
            entity = new ResponseEntity<>(new GenericResponse("Invalid Username/Password"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }
    
    @RequestMapping(value = "/referexpert/users/firstname/{firstname}", method = RequestMethod.GET)
    public ResponseEntity<List<UserRegistration>> selectUsersByFirstName(@PathVariable("firstname") String firstName) {
        String criteria = " first_name like '%" + firstName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/referexpert/users/lastname/{lastname}", method = RequestMethod.GET)
    public ResponseEntity<List<UserRegistration>> selectUsersByLastName(@PathVariable("lastname") String lastName) {
        String criteria = " last_name like '%" + lastName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/referexpert/users/type/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<UserRegistration>> selectUsersByType(@PathVariable("type") String type) {
        String criteria = " user_type like '%" + type + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/referexpert/users/speciality/{speciality}", method = RequestMethod.GET)
    public ResponseEntity<List<UserRegistration>> selectUsersBySpeciality(@PathVariable("speciality") String speciality) {
        String criteria = " user_speciality like '%" + speciality + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
}
