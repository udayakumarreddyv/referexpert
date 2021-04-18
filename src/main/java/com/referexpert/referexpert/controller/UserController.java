package com.referexpert.referexpert.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.referexpert.referexpert.beans.Coordinates;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.util.GeoUtils;

@RestController
@CrossOrigin()
public class UserController {
    
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MySQLService mySQLService;
    
    @Autowired
    private GeoApiContext geoApiContext;

    @PostMapping(value = "/referexpert/deactiveuser")
    public ResponseEntity<GenericResponse> deactivateUserAccount(@RequestBody String emailString) {
        ResponseEntity<GenericResponse> entity = updateUserStatus(emailString, Constants.INACTIVE);
        return entity;
    }

    @PostMapping(value = "/referexpert/activeuser")
    public ResponseEntity<GenericResponse> activateUserAccount(@RequestBody String emailString) {
        ResponseEntity<GenericResponse> entity = updateUserStatus(emailString, Constants.ACTIVE);
        return entity;
    }
    
    private ResponseEntity<GenericResponse> updateUserStatus(String emailString, String status) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<GenericResponse> entity = null;
        UserRegistration userRegistration = null;
        try {
            userRegistration = mapper.readValue(emailString, UserRegistration.class);
        }
        catch (Exception e) {
            logger.error("Unable to parse the input :: " + emailString);
            logger.error("Exception as follows :: " + e);
            entity = new ResponseEntity<>(new GenericResponse("Unable to Parse Input"), HttpStatus.BAD_REQUEST);
        }
        logger.info("JSON to Object Conversion :: " + userRegistration != null ? userRegistration.toString() : null);
        // If present deactive user status
        String criteria = " email = ?";
        String email = userRegistration.getEmail();
        if (mySQLService.selectUserProfile(email, criteria)) {
            int value = mySQLService.updateUserActivation(email, status);
            if (value == 0) {
                entity = new ResponseEntity<>(new GenericResponse("Issue in updating user profile"),
                        HttpStatus.BAD_REQUEST);
            } else {
                if(Constants.INACTIVE.equals(status)) {
                    entity = new ResponseEntity<>(new GenericResponse("Deactivated Successfully"), HttpStatus.OK);
                } else {
                    entity = new ResponseEntity<>(new GenericResponse("Activated Successfully"), HttpStatus.OK);
                }
            }
        } else {
            entity = new ResponseEntity<>(new GenericResponse("User doesn't exists"), HttpStatus.NOT_FOUND);
        }
        return entity;
    }
      
    @GetMapping(value = "/referexpert/users/firstname/{firstname}")
    public ResponseEntity<List<UserRegistration>> selectUsersByFirstName(@PathVariable("firstname") String firstName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and first_name like '%" + firstName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/lastname/{lastname}")
    public ResponseEntity<List<UserRegistration>> selectUsersByLastName(@PathVariable("lastname") String lastName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and last_name like '%" + lastName + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/city/{city}")
    public ResponseEntity<List<UserRegistration>> selectUsersByCity(@PathVariable("city") String city) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and city like '%" + city + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/state/{state}")
    public ResponseEntity<List<UserRegistration>> selectUsersByState(@PathVariable("state") String state) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and state like '%" + state + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/zip/{zip}")
    public ResponseEntity<List<UserRegistration>> selectUsersByZip(@PathVariable("zip") String zip) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and zip = '" + zip + "'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/type/{type}")
    public ResponseEntity<List<UserRegistration>> selectUsersByType(@PathVariable("type") String type) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and user_type like '%" + type + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/speciality/{speciality}")
    public ResponseEntity<List<UserRegistration>> selectUsersBySpeciality(@PathVariable("speciality") String speciality) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "' and user_speciality like '%" + speciality + "%'";
        List<UserRegistration> users =  mySQLService.selectActiveUsers(criteria);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/distance/{distance}")
    public ResponseEntity<List<UserRegistration>> selectUsersByDistance(@PathVariable("distance") int distance) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String criteria =  " email != '" + userDetails.getUsername() + "'";
        List<UserRegistration> users =  mySQLService.selectActiveUsersByDistance(criteria, distance, userDetails.getUsername());
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/distance/{lattitude}/{longitude}/{distance}")
    public ResponseEntity<List<UserRegistration>> selectUsersByCoordinates(@PathVariable("lattitude") Double lattitude,
            @PathVariable("longitude") Double longitude, @PathVariable("distance") int distance) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String criteria = " email != '" + userDetails.getUsername() + "'";
        List<UserRegistration> users = mySQLService.selectActiveUsersByCoordinates(criteria, lattitude, longitude, distance);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
    
    @GetMapping(value = "/referexpert/users/distance/{address}/{distance}")
    public ResponseEntity<List<UserRegistration>> selectUsersByAddress(@PathVariable("address") String address,
            @PathVariable("distance") int distance) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coordinates coordinates = GeoUtils.getCoordinates(address, geoApiContext);
        String criteria = " email != '" + userDetails.getUsername() + "'";
        List<UserRegistration> users = mySQLService.selectActiveUsersByCoordinates(criteria, coordinates.getLattitude(),
                coordinates.getLongitude(), distance);
        return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
    }
}
