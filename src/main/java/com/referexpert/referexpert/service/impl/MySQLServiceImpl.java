package com.referexpert.referexpert.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.Coordinates;
import com.referexpert.referexpert.beans.UserNotification;
import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;
import com.referexpert.referexpert.repository.MySQLRepository;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.util.GeoUtils;

@Service("mysqlService")
public class MySQLServiceImpl implements MySQLService {

    private static final Logger logger = LoggerFactory.getLogger(MySQLServiceImpl.class);

    @Autowired
    private MySQLRepository mysqlRepository;
    
    @Autowired
    private GeoApiContext geoApiContext;

    @Override
    public List<UserType> selectAllUserTypes() {
        logger.info("MySQLServiceImpl :: In selectAllUserTypes");
        List<UserType> userTypes;
        try {
            userTypes = mysqlRepository.selectAllUserTypes();
        }
        catch (Exception e) {
            exceptionBlock(e, "Exception while fetching data for user_type");
            return null;
        }
        return userTypes;
    }

	@Override
    public List<UserType> selectUserTypeByUserType(String userType) {
        logger.info("MySQLServiceImpl :: In selectUserTypeByUserType :: " + userType);
        List<UserType> userTypes;
        try {
            userTypes = mysqlRepository.selectUserTypeByUserType(userType);
        }
        catch (Exception e) {
            exceptionBlock(e, "Exception while fetching data for user_type");
            return null;
        }
        return userTypes;
    }

    @Override
    public UserSpeciality selectUserSpecialityByUserType(String userType) {
        logger.info("MySQLServiceImpl :: In selectUserSpecialityByUserType :: " + userType);
        UserSpeciality userSpeciality;
        try {
            userSpeciality = mysqlRepository.selectUserSpecialityByUserType(userType);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data for user_speciality");
            return null;
        }
        return userSpeciality;
    }
    
    private String getFullAddress(UserRegistration userRegistration) {
        logger.info("MySQLServiceImpl :: In getFullAddress :: " + userRegistration.toString());
        return userRegistration.getAddress() + " " + userRegistration.getCity() + " " + userRegistration.getState()
                + " " + userRegistration.getZip();
    }

    @Override
    public int insertUserProfile(UserRegistration userRegistration) {
        logger.info("MySQLServiceImpl :: In insertUserProfile :: " + userRegistration.toString());
        Integer userTypeId = selectUserTypeById(userRegistration.getUserType());
        Integer userSpecialityId = selectUserSpecialityById(userTypeId, userRegistration.getUserSpeciality());
        Coordinates coordinates = GeoUtils.getCoordinates(getFullAddress(userRegistration), geoApiContext);
        userRegistration.setLattitude(coordinates.getLattitude());
        userRegistration.setLongitude(coordinates.getLongitude());
        int value = 0;
        try {
            value = mysqlRepository.insertUserProfile(userRegistration, userTypeId, userSpecialityId);
        }
        catch (DuplicateKeyException e) {
        	exceptionBlock(e, "Exception while inserting data into user_profile");
            return 999999;
        }
        catch (DataIntegrityViolationException e) {
        	exceptionBlock(e, "Exception while inserting data into user_profile");
        	return 888888;
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while inserting data into user_profile");
            return 0;
        }
        return value;
    }

    @Override
    public Integer selectUserTypeById(String userType) {
        logger.info("MySQLServiceImpl :: In selectUserTypeById :: " + userType);
        Integer userTypeId = null;
        try {
            userTypeId = mysqlRepository.selectUserTypeById(userType);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_type");
            return 0;
        }
        return userTypeId;
    }

    @Override
    public Integer selectUserSpecialityById(Integer userTypeId, String speciality) {
        logger.info("MySQLServiceImpl :: In selectUserSpecialityById :: " + userTypeId + " :: " + speciality);
        Integer userSpecialityId = null;
        try {
            userSpecialityId = mysqlRepository.selectUserSpecialityById(userTypeId, speciality);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_speciality");
            return 0;
        }
        return userSpecialityId;
    }

    @Override
    public int insertConfirmationToken(ConfirmationToken confirmationToken) {
        logger.info("MySQLServiceImpl :: In insertConfirmationToken :: " + confirmationToken);
        int value = 0;
        try {
            value = mysqlRepository.insertConfirmationToken(confirmationToken);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while inserting data into confirmation_token");
            return 0;
        }
        return value;
    }

    @Override
    public boolean selectUserProfile(String email, String criteria) {
        logger.info("MySQLServiceImpl :: In selectUserProfile :: " + email + " :: " + criteria);
        try {
            return mysqlRepository.selectUserProfile(email, criteria);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_profile");
            return false;
        }
    }

    @Override
    public boolean selectConfirmationToken(String token) {
        logger.info("MySQLServiceImpl :: In selectConfirmationToken :: " + token);
        try {
            return mysqlRepository.selectConfirmationToken(token);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for confirmation_token");
            logger.error("Exception details :: " + e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int updateUserActivation(String email, String indicator) {
        logger.info("MySQLServiceImpl :: In updateUserActivation :: " + email + " :: " + indicator);
        int value = 0;
        try {
            value = mysqlRepository.updateUserActivation(email, indicator);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating active indicator into user_profile");
            return 0;
        }
        return value;
    }

    public int updateUserPassword(String email, String password) {
        logger.info("MySQLServiceImpl :: In updateUserPassword :: " + email);
        int value = 0;
        try {
            value = mysqlRepository.updateUserPassword(email, password);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating password into user_profile");
            return 0;
        }
        return value;
    }

    public int updateUserProfile(UserRegistration userRegistration) {
        logger.info("MySQLServiceImpl :: In updateUserProfile :: " + userRegistration.toString());
        int value = 0;
        Integer userTypeId = selectUserTypeById(userRegistration.getUserType());
        Integer userSpecialityId = selectUserSpecialityById(userTypeId, userRegistration.getUserSpeciality());
        Coordinates coordinates = GeoUtils.getCoordinates(getFullAddress(userRegistration), geoApiContext);
        userRegistration.setLattitude(coordinates.getLattitude());
        userRegistration.setLongitude(coordinates.getLongitude());
        try {
            value = mysqlRepository.updateUserProfile(userRegistration, userTypeId, userSpecialityId);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating user_profile table");
            return 0;
        }
        return value;
    }

    @Override
    public int deleteConfirmationToken(String token) {
        logger.info("MySQLServiceImpl :: In deleteConfirmationToken :: " + token);
        int value = 0;
        try {
            value = mysqlRepository.deleteConfirmationToken(token);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while deleting data from confirmation_token");
            return 0;
        }
        return value;
    }

    @Override
    public int insertUserReferral(String referralId, String userEmail, String docEmail, String isRegistered) {
        logger.info("MySQLServiceImpl :: In insertUserReferral :: " + referralId + " :: " + userEmail + " :: " 
        			+ docEmail + " :: " + isRegistered);
        int value = 0;
        try {
            value = mysqlRepository.insertUserReferral(referralId, userEmail, docEmail, isRegistered);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while inserting data into user_referral");
            return 0;
        }
        return value;
    }

    @Override
    public boolean selectUserReferral(String userReferralId, String docEmail) {
        logger.info("MySQLServiceImpl :: In selectUserReferral :: " + userReferralId + " :: " + docEmail);
        try {
            return mysqlRepository.selectUserReferral(userReferralId, docEmail);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_referral");
            return false;
        }
    }
    
    @Override
    public boolean selectUserReferralByUser(String docEmail) {
    	logger.info("MySQLServiceImpl :: In selectUserReferralByUser :: "  + docEmail);
        try {
            return mysqlRepository.selectUserReferralByUser(docEmail);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_referral");
            return false;
        }
    }
    
    @Override
    public List<UserReferral> selectNonRegisteredUsers() {
    	logger.info("MySQLServiceImpl :: In selectNonRegisteredUsers");
        try {
            return mysqlRepository.selectNonRegisteredUsers();
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_referral");
            return null;
        }
    }

    @Override
    public int updateUserReferral(String email, String indicator) {
        logger.info("MySQLServiceImpl :: In updateUserReferral :: " + email + " :: " + indicator);
        int value = 0;
        try {
            value = mysqlRepository.updateUserReferral(email, indicator);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating data into user_referral");
            return 0;
        }
        return value;
    }

    @Override
    public List<UserRegistration> selectActiveUsers(String criteria) {
        logger.info("MySQLServiceImpl :: In selectActiveUsers :: " + criteria);
        List<UserRegistration> userRegistrations = new ArrayList<UserRegistration>();
        try {
            userRegistrations = mysqlRepository.selectActiveUsers(criteria);
            userRegistrations.parallelStream().forEach(x -> x.setPassword(null));
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_profile");
        }
        return userRegistrations;
    }
    
    @Override
    public List<UserRegistration> selectActiveUsersByDistance(String criteria, int distance, String email) {
        logger.info("MySQLServiceImpl :: In selectActiveUsersByDistance :: " + criteria + " :: " + distance + " :: " + email);
        UserRegistration userRegistration = selectUser(" email = '" + email + "'");
        List<UserRegistration> userRegistrations = new ArrayList<UserRegistration>();
        try {
            userRegistrations = getUserRegistrations(criteria, distance, userRegistration.getLattitude(),
                    userRegistration.getLongitude());
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_profile");
        }
        return userRegistrations;
    }
    
    @Override
    public List<UserRegistration> selectActiveUsersByCoordinates(String criteria, Double lattitude, Double longitude,
            int distance) {
        logger.info("MySQLServiceImpl :: In selectActiveUsersByCoordinates :: " + criteria + " :: " + lattitude
        		+ " :: " + longitude +" :: " + distance);
        List<UserRegistration> userRegistrations = new ArrayList<UserRegistration>();
        try {
            userRegistrations = getUserRegistrations(criteria, distance, lattitude, longitude);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user_profile");
        }
        return userRegistrations;
    }

    private List<UserRegistration> getUserRegistrations(String criteria, int distance, Double lattitude,
            Double longitude) throws Exception {
        logger.info("MySQLServiceImpl :: In getUserRegistrations :: " + criteria + " :: " + distance + " :: " + lattitude
        		+ " :: " + longitude);
        List<UserRegistration> userRegistrations = mysqlRepository.selectActiveUsers(criteria);
        userRegistrations.parallelStream().forEach(x -> x.setPassword(null));
        List<UserRegistration> finalList = userRegistrations.parallelStream()
                .filter(p -> GeoUtils.isDistanceInRange(lattitude, longitude, p.getLattitude(),
                        p.getLongitude(), distance))
                .collect(Collectors.toCollection(() -> new ArrayList<UserRegistration>()));
        finalList.parallelStream().forEach(x -> x.setDistance(GeoUtils.getDistnace(lattitude, longitude, x.getLattitude(),
                x.getLongitude(), distance)));
        return finalList;
    }

    @Override
    public int insertAppointment(Appointment appointment, String type) {
        logger.info("MySQLServiceImpl :: In insertAppointment :: " + appointment);
        String criteriaFrom = " email = '" + appointment.getAppointmentFrom() + "'";
        UserRegistration fromUser = selectUser(criteriaFrom);
        String criteriaTo = " email = '" + appointment.getAppointmentTo() + "'";
        UserRegistration toUser = selectUser(criteriaTo);
        int value = 0;
        try {
            value = mysqlRepository.insertAppointment(appointment, fromUser.getUserId(), toUser.getUserId(), type);
        }
        catch (DuplicateKeyException e) {
        	exceptionBlock(e, "Exception while inserting data into appointment");
            return 999999;
        }
        catch (DataIntegrityViolationException e) {
        	exceptionBlock(e, "Exception while inserting data into appointment");
            return 888888;
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while inserting data into appointment");
            return 0;
        }
        return value;
    }

    @Override
    public int updateAppointmentAccepted(String appointmentId, String indicator) {
        logger.info("MySQLServiceImpl :: In updateAppointmentAccepted :: " + appointmentId + " :: " + indicator);
        int value = 0;
        try {
            value = mysqlRepository.updateAppointmentAccepted(appointmentId, indicator);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating data into appointment");
            return 0;
        }
        return value;
    }
    
    @Override
    public int updateAppointmentResponse(String appointmentId, String response) {
        logger.info("MySQLServiceImpl :: In updateAppointmentResponse :: " + appointmentId + " :: " + response);
        int value = 0;
        try {
            value = mysqlRepository.updateAppointmentResponse(appointmentId, response);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating data into appointment");
            return 0;
        }
        return value;
    }

    @Override
    public int updateAppointmentServed(String appointmentId, String indicator) {
        logger.info("MySQLServiceImpl :: In updateAppointmentServed :: " + appointmentId + " :: " + indicator);
        int value = 0;
        try {
            value = mysqlRepository.updateAppointmentServed(appointmentId, indicator);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while updating data into appointment");
            return 0;
        }
        return value;
    }

    @Override
    public List<Appointment> selectAppointments(String criteria) {
        logger.info("MySQLServiceImpl :: In selectAppointments :: " + criteria);
        List<Appointment> appointments = new ArrayList<Appointment>();
        try {
            appointments = mysqlRepository.selectAppointments(criteria);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from appointment");
        }
        return appointments;
    }
    
    @Override
    public Appointment selectAppointmentById(String criteria) {
        logger.info("MySQLServiceImpl :: In selectAppointments :: " + criteria);
        List<Appointment> appointments = new ArrayList<Appointment>();
        try {
            appointments = mysqlRepository.selectAppointments(criteria);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from appointment");
        }
        if(appointments.size() > 0) {
        	return appointments.get(0);
        }
        return new Appointment();
    }

    @Override
    public UserRegistration selectUser(String criteria) {
        logger.info("MySQLServiceImpl :: In selectUser :: " + criteria);
        UserRegistration userRegistration = new UserRegistration();
        try {
            userRegistration = mysqlRepository.selectUser(criteria);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user profile");
        }
        return userRegistration;
    }

    @Override
    public UserRegistration save(UserRegistration userRegistration) {
        logger.info("MySQLServiceImpl :: In save :: " + userRegistration.toString());
        try {
            insertUserProfile(userRegistration);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while saving data into user profile");
        }
        return userRegistration;
    }
    
    @Override
    public int getUserCountByStatus(String activeFlag) {
    	logger.info("MySQLServiceImpl :: In getUserCountByStatus :: " + activeFlag);
        try {
            return mysqlRepository.getUserCountByStatus(activeFlag);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user profile");
            return 0;
        }	
    }
    
    private void exceptionBlock(Exception e, String message) {
		logger.error(message);
		logger.error("Exception details :: " + e);
		e.printStackTrace();
	}
    
    @Override
    public UserNotification selectUserNotification(String criteria) {
        logger.info("MySQLServiceImpl :: In selectUserNotification :: " + criteria);
        UserNotification userNotification = new UserNotification();
        try {
        	userNotification = mysqlRepository.selectUserNotification(criteria);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from user notification");
        }
        return userNotification;
    }
    
    @Override
    public int upsertUserNotification(UserNotification userNotification, String userEmail) {
    	logger.info("MySQLServiceImpl :: In upsertUserNotification :: " + userNotification.toString());
    	String criteria = " email = '" + userEmail + "'";
        UserNotification userNotificationfromDB = selectUserNotification(criteria);
        int value = 0;
    	try {
    		if(userNotificationfromDB == null) {
    			String criteriaFrom = " email = '" + userEmail + "'";
    	        UserRegistration user = selectUser(criteriaFrom);
    			value = mysqlRepository.insertUserNotification(userNotification, user.getUserId());
    		} else {
    			value = mysqlRepository.updateUsernotification(userNotification, userNotificationfromDB.getUserEmail());
    		}
        }
        catch (DuplicateKeyException e) {
        	exceptionBlock(e, "Exception while upserting data into user_notification");
            return 999999;
        }
        catch (DataIntegrityViolationException e) {
        	exceptionBlock(e, "Exception while upserting data into user_notification");
        	return 888888;
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while upserting data into user_notification");
            return 0;
        }
    	return value;
    }
    
    @Override
    public boolean getPendingTasks(String type, String isAccepted, String isReferral, String email) {
    	logger.info("MySQLServiceImpl :: In getPendingTasks :: " + email);
        try {
            return mysqlRepository.getPendingTasks(type, isAccepted, isReferral, email);
        }
        catch (Exception e) {
        	exceptionBlock(e, "Exception while fetching data from appointments data");
            return false;
        }	
    }
}
