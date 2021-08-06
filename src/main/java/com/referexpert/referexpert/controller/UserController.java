package com.referexpert.referexpert.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.referexpert.referexpert.beans.Coordinates;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserCount;
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

    @PostMapping(value = "/deactiveuser")
    public ResponseEntity<GenericResponse> deactivateUserAccount(@RequestBody String emailString) {
        logger.info("UserController :: In deactivateUserAccount : " + emailString);
        ResponseEntity<GenericResponse> entity = updateUserStatus(emailString, Constants.INACTIVE);
        return entity;
    }

    @PostMapping(value = "/activeuser")
    public ResponseEntity<GenericResponse> activateUserAccount(@RequestBody String emailString) {
        logger.info("UserController :: In activateUserAccount : " + emailString);
        ResponseEntity<GenericResponse> entity = updateUserStatus(emailString, Constants.ACTIVE);
        return entity;
    }
    
    private ResponseEntity<GenericResponse> updateUserStatus(String emailString, String status) {
        logger.info("UserController :: In updateUserStatus : " + emailString + " status : " + status);
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
    
	@GetMapping(value = "/users")
	public ResponseEntity<List<UserRegistration>> selectUsersByParams(@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName, @RequestParam(required = false) String city,
			@RequestParam(required = false) String state, @RequestParam(required = false) String zip,
			@RequestParam(required = false) String type, @RequestParam(required = false) String speciality,
			@RequestParam(required = false) String active) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuffer criteria = new StringBuffer(" email != '" + userDetails.getUsername() + "'");
		boolean isCriteriaPresent = false;
		if (!StringUtils.isEmpty(firstName)) {
			logger.info("UserController :: In selectUsersByParams firstname: " + firstName);
			criteria.append(" and first_name like '" + firstName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(lastName)) {
			logger.info("UserController :: In selectUsersByParams lastname: " + lastName);
			criteria.append(" and last_name like '" + lastName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(city)) {
			logger.info("UserController :: In selectUsersByParams city: " + city);
			criteria.append(" and city like '" + city + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(state)) {
			logger.info("UserController :: In selectUsersByParams state: " + state);
			criteria.append(" and state like '" + state + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(zip)) {
			logger.info("UserController :: In selectUsersByParams zip: " + zip);
			criteria.append(" and zip = '" + zip + "'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(type)) {
			logger.info("UserController :: In selectUsersByParams type: " + type);
			criteria.append(" and user_type like '" + type + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(speciality)) {
			logger.info("UserController :: In selectUsersByParams speciality: " + speciality);
			criteria.append(" and user_speciality like '" + speciality + "%'");
			isCriteriaPresent = true;
		}
		if (StringUtils.isEmpty(active)) {
			logger.info("UserController :: In selectUsersByParams active: " + active);
			criteria.append(" and is_active = 'Y'");
		}
		
		if (isCriteriaPresent) {
			List<UserRegistration> users = mySQLService.selectActiveUsers(criteria.toString());
			return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
		} else {
			logger.info("UserController :: In selectUsersByParams returning empty as no criteiria passed");
			return new ResponseEntity<List<UserRegistration>>(new ArrayList<UserRegistration>(), HttpStatus.OK);
		}
	}
    
    @GetMapping(value = "/users/distance/{distance}")
	public ResponseEntity<List<UserRegistration>> selectUsersByDistance(@PathVariable("distance") int distance,
			@RequestParam(required = false) String type, @RequestParam(required = false) String speciality,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuffer criteria = new StringBuffer(" email != '" + userDetails.getUsername() + "'");
		boolean isCriteriaPresent = false;
		if (!StringUtils.isEmpty(firstName)) {
			logger.info("UserController :: In selectUsersByDistance firstname: " + firstName);
			criteria.append(" and first_name like '" + firstName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(lastName)) {
			logger.info("UserController :: In selectUsersByDistance lastname: " + lastName);
			criteria.append(" and last_name like '" + lastName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(type)) {
			logger.info("UserController :: In selectUsersByDistance type: " + type);
			criteria.append(" and user_type like '" + type + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(speciality)) {
			logger.info("UserController :: In selectUsersByDistance speciality: " + speciality);
			criteria.append(" and user_speciality like '" + speciality + "%'");
			isCriteriaPresent = true;
		}
		criteria.append(" and is_active = 'Y'");
		if (isCriteriaPresent) {
			List<UserRegistration> users = mySQLService.selectActiveUsersByDistance(criteria.toString(), distance,
					userDetails.getUsername());
			return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
		} else {
			logger.info("UserController :: In selectUsersByDistance returning empty as no criteiria passed");
			return new ResponseEntity<List<UserRegistration>>(new ArrayList<UserRegistration>(), HttpStatus.OK);
		}
	}

    @GetMapping(value = "/users/distance/{lattitude}/{longitude}/{distance}")
	public ResponseEntity<List<UserRegistration>> selectUsersByCoordinates(@PathVariable("lattitude") Double lattitude,
			@PathVariable("longitude") Double longitude, @PathVariable("distance") int distance,
			@RequestParam(required = false) String type, @RequestParam(required = false) String speciality,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuffer criteria = new StringBuffer(" email != '" + userDetails.getUsername() + "'");
		boolean isCriteriaPresent = false;
		if (!StringUtils.isEmpty(firstName)) {
			logger.info("UserController :: In selectUsersByCoordinates firstname: " + firstName);
			criteria.append(" and first_name like '" + firstName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(lastName)) {
			logger.info("UserController :: In selectUsersByCoordinates lastname: " + lastName);
			criteria.append(" and last_name like '" + lastName + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(type)) {
			logger.info("UserController :: In selectUsersByCoordinates type: " + type);
			criteria.append(" and user_type like '" + type + "%'");
			isCriteriaPresent = true;
		}
		if (!StringUtils.isEmpty(speciality)) {
			logger.info("UserController :: In selectUsersByCoordinates speciality: " + speciality);
			criteria.append(" and user_speciality like '" + speciality + "%'");
			isCriteriaPresent = true;
		}
		criteria.append(" and is_active = 'Y'");
		if (isCriteriaPresent) {
			List<UserRegistration> users = mySQLService.selectActiveUsersByCoordinates(criteria.toString(), lattitude,
					longitude, distance);
			return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
		} else {
			logger.info("UserController :: In selectUsersByCoordinates returning empty as no criteiria passed");
			return new ResponseEntity<List<UserRegistration>>(new ArrayList<UserRegistration>(), HttpStatus.OK);
		}
	}

    @GetMapping(value = "/users/distance/{address}/{distance}")
    public ResponseEntity<List<UserRegistration>> selectUsersByAddress(@PathVariable("address") String address,
            @PathVariable("distance") int distance, @RequestParam(required = false) String type,
            @RequestParam(required = false) String speciality) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coordinates coordinates = GeoUtils.getCoordinates(address, geoApiContext);
        StringBuffer criteria = new StringBuffer(" email != '" + userDetails.getUsername() + "'");
        boolean isCriteriaPresent = false;
        if (!StringUtils.isEmpty(type)) {
            logger.info("UserController :: In selectUsersByAddress type: " + type);
            criteria.append(" and user_type like '" + type + "%'");
            isCriteriaPresent = true;
        }
        if (!StringUtils.isEmpty(speciality)) {
            logger.info("UserController :: In selectUsersByAddress speciality: " + speciality);
            criteria.append(" and user_speciality like '" + speciality + "%'");
            isCriteriaPresent = true;
        }
        criteria.append(" and is_active = 'Y'");
        if (isCriteriaPresent) {
            List<UserRegistration> users = mySQLService.selectActiveUsersByCoordinates(criteria.toString(),
                    coordinates.getLattitude(), coordinates.getLongitude(), distance);
            return new ResponseEntity<List<UserRegistration>>(users, HttpStatus.OK);
        } else {
            logger.info("UserController :: In selectUsersByAddress returning empty as no criteiria passed");
            return new ResponseEntity<List<UserRegistration>>(new ArrayList<UserRegistration>(), HttpStatus.OK);
        }
    }
    
    @GetMapping(value = "/users/count")
    public ResponseEntity<UserCount> selectUserCounts() {
    	UserCount userCount = new UserCount();
    	userCount.setActive(mySQLService.getUserCountByStatus(Constants.ACTIVE));
    	userCount.setPending(mySQLService.getUserCountByStatus(Constants.PENDING));
    	userCount.setDisabled(mySQLService.getUserCountByStatus(Constants.INACTIVE));
    	userCount.setTotal(userCount.getActive()+userCount.getPending()+userCount.getDisabled());
    	return new ResponseEntity<UserCount>(userCount, HttpStatus.OK);
    }
}
