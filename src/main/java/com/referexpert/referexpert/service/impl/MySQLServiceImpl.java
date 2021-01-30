package com.referexpert.referexpert.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;
import com.referexpert.referexpert.repository.MySQLRepository;
import com.referexpert.referexpert.service.MySQLService;

@Service("mysqlService")
@Transactional
public class MySQLServiceImpl implements MySQLService {

    private static final Logger logger = LoggerFactory.getLogger(MySQLServiceImpl.class);

    @Autowired
    private MySQLRepository mysqlRepository;

    @Override
    public List<UserType> selectAllUserTypes() {
        List<UserType> userTypes;
        try {
            userTypes = mysqlRepository.selectAllUserTypes();
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for user_type");
            logger.error("Exception details :: " + e);
            return null;
        }
        return userTypes;
    }

    @Override
    public List<UserType> selectUserTypeByUserType(String userType) {
        List<UserType> userTypes;
        try {
            userTypes = mysqlRepository.selectUserTypeByUserType(userType);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for user_type");
            logger.error("Exception details :: " + e);
            return null;
        }
        return userTypes;
    }

    @Override
    public UserSpeciality selectUserSpecialityByUserType(String userType) {
        UserSpeciality userSpeciality;
        try {
            userSpeciality = mysqlRepository.selectUserSpecialityByUserType(userType);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for user_speciality");
            logger.error("Exception details :: " + e);
            return null;
        }
        return userSpeciality;
    }

    @Override
    public int insertUserProfile(UserRegistration userRegistration) {
        Integer userTypeId = selectUserTypeById(userRegistration.getUserType());
        Integer userSpecialityId = selectUserSpecialityById(userTypeId, userRegistration.getUserSpeciality());
        int value = 0;
        try {
            value = mysqlRepository.insertUserProfile(userRegistration, userTypeId, userSpecialityId);
        }
        catch (DuplicateKeyException e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 999999;
        }
        catch (DataIntegrityViolationException e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 888888;
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public Integer selectUserTypeById(String userType) {
        Integer userTypeId = null;
        try {
            userTypeId = mysqlRepository.selectUserTypeById(userType);
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return userTypeId;
    }

    @Override
    public Integer selectUserSpecialityById(Integer userTypeId, String speciality) {
        Integer userSpecialityId = null;
        try {
            userSpecialityId = mysqlRepository.selectUserSpecialityById(userTypeId, speciality);
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return userSpecialityId;
    }

    @Override
    public int insertConfirmationToken(ConfirmationToken confirmationToken) {
        int value = 0;
        try {
            value = mysqlRepository.insertConfirmationToken(confirmationToken);
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into user_profile");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public boolean selectUserProfile(String email, String criteria) {
        try {
            return mysqlRepository.selectUserProfile(email, criteria);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for user_profile");
            logger.error("Exception details :: " + e);
            return false;
        }
    }

    @Override
    public boolean selectConfirmationToken(String token) {
        try {
            return mysqlRepository.selectConfirmationToken(token);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for confirmation_token");
            logger.error("Exception details :: " + e);
            return false;
        }
    }

    @Override
    public int updateUserProfile(String email, String indicator) {
        int value = 0;
        try {
            value = mysqlRepository.updateUserProfile(email, indicator);
        }
        catch (Exception e) {
            logger.error("Exception while updating data into user_profile");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public int deleteConfirmationToken(String token) {
        int value = 0;
        try {
            value = mysqlRepository.deleteConfirmationToken(token);
        }
        catch (Exception e) {
            logger.error("Exception while deleting data from confirmation_token");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public int insertUserReferral(String referralId, String userEmail, String docEmail, String isRegistered) {
        int value = 0;
        try {
            value = mysqlRepository.insertUserReferral(referralId,userEmail, docEmail, isRegistered);
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into user_referral");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public boolean selectUserReferral(String userReferralId) {
        try {
            return mysqlRepository.selectUserReferral(userReferralId);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data for user_referral");
            logger.error("Exception details :: " + e);
            return false;
        }
    }

    @Override
    public int updateUserReferral(String email, String indicator) {
        int value = 0;
        try {
            value = mysqlRepository.updateUserReferral(email, indicator);
        }
        catch (Exception e) {
            logger.error("Exception while updating data into user_referral");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public List<UserRegistration> selectActiveUsers(String criteria) {
        List<UserRegistration> userRegistrations = new ArrayList<UserRegistration>();
        try {
            userRegistrations = mysqlRepository.selectActiveUsers(criteria);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data into user_referral");
            logger.error("Exception details :: " + e);
        }
        return userRegistrations;
    }

    @Override
    public int insertAppointment(Appointment appointment) {
        String referFrom = null;
        String referTo = null;
        
        String criteriaFrom = " email = '" + appointment.getAppointmentFrom() + "'";
        List<UserRegistration> fromUsers = selectActiveUsers(criteriaFrom);
        if(fromUsers != null && fromUsers.size() > 0) {
            referFrom = fromUsers.get(0).getUserId();
        }
        
        String criteriaTo = " email = '" + appointment.getAppointmentTo() + "'";
        List<UserRegistration> toUsers = selectActiveUsers(criteriaTo);
        if(toUsers != null && toUsers.size() > 0) {
            referTo = toUsers.get(0).getUserId();
        }
        
        int value = 0;
        try {
            value = mysqlRepository.insertAppointment(appointment, referFrom, referTo);
        }
        catch (DuplicateKeyException e) {
            logger.error("Exception while inserting data into appointment");
            logger.error("Exception details :: " + e);
            return 999999;
        }
        catch (DataIntegrityViolationException e) {
            logger.error("Exception while inserting data into appointment");
            logger.error("Exception details :: " + e);
            return 888888;
        }
        catch (Exception e) {
            logger.error("Exception while inserting data into appointment");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public int updateAppointmentAccepted(String appointmentId, String indicator) {
        int value = 0;
        try {
            value = mysqlRepository.updateAppointmentAccepted(appointmentId, indicator);
        }
        catch (Exception e) {
            logger.error("Exception while updating data into appointment");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public int updateAppointmentServed(String appointmentId, String indicator) {
        int value = 0;
        try {
            value = mysqlRepository.updateAppointmentServed(appointmentId, indicator);
        }
        catch (Exception e) {
            logger.error("Exception while updating data into appointment");
            logger.error("Exception details :: " + e);
            return 0;
        }
        return value;
    }

    @Override
    public List<Appointment> selectAppointments(String criteria) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        try {
            appointments = mysqlRepository.selectAppointments(criteria);
        }
        catch (Exception e) {
            logger.error("Exception while fetching data into appointment");
            logger.error("Exception details :: " + e);
        }
        return appointments;
    }
}
