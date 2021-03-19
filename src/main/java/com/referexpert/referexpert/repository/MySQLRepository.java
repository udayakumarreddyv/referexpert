package com.referexpert.referexpert.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.constant.QueryConstants;

@Component
public class MySQLRepository {

    @Autowired
    private JdbcTemplate mysqlJdbcTemplate;

    public List<UserType> selectAllUserTypes() throws Exception {
        List<UserType> userTypes = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_TYPE, new Object[] {},
                (rs, rowNum) -> {
                    UserType userType = new UserType();
                    userType.setUserType(rs.getString(1));
                    try {
                        userType.setUserSpeciality(selectUserSpecialityByUserType(userType.getUserType()));
                    }
                    catch (Exception e) {
                        userType.setUserSpeciality(null);
                    }
                    return userType;
                });
        if (userTypes != null && userTypes.size() > 0) {
            return userTypes;
        } else {
            return null;
        }
    }

    public List<UserType> selectUserTypeByUserType(String userTypeParam) throws Exception {
        List<UserType> userTypes = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_TYPE_BY_USER_TYPE,
                new Object[] { userTypeParam }, (rs, rowNum) -> {
                    UserType userType = new UserType();
                    userType.setUserType(rs.getString(1));
                    try {
                        userType.setUserSpeciality(selectUserSpecialityByUserType(userType.getUserType()));
                    }
                    catch (Exception e) {
                        userType.setUserSpeciality(null);
                    }
                    return userType;
                });
        if (userTypes != null && userTypes.size() > 0) {
            return userTypes;
        } else {
            return null;
        }
    }

    public Integer selectUserTypeById(String userType) throws Exception {
        List<Integer> userTypeIds = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_TYPE_ID,
                new Object[] { userType }, (rs, rowNum) -> {
                    return rs.getInt(1);
                });
        if (userTypeIds != null && userTypeIds.size() > 0) {
            return userTypeIds.get(0);
        } else {
            return 0;
        }
    }

    public Integer selectUserSpecialityById(Integer userTypeId, String speciality) throws Exception {
        List<Integer> userSplecialityIds = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_SPL_ID,
                new Object[] { userTypeId, speciality }, (rs, rowNum) -> {
                    return rs.getInt(1);
                });
        if (userSplecialityIds != null && userSplecialityIds.size() > 0) {
            return userSplecialityIds.get(0);
        } else {
            return 0;
        }
    }

    public UserSpeciality selectUserSpecialityByUserType(String userType) throws Exception {
        List<String> specialities = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_SPECIALITY_BY_USER_TYPE,
                new Object[] { userType }, (rs, rowNum) -> {
                    return rs.getString(1);
                });
        UserSpeciality userSpeciality = new UserSpeciality();
        userSpeciality.setSpecialities(specialities);
        return userSpeciality;
    }

    public int insertUserProfile(UserRegistration userRegistration, Integer userTypeId, Integer userSpecialityId)
            throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_USER_PROFILE,
                new Object[] { userRegistration.getUserId(), userRegistration.getFirstName(),
                        userRegistration.getLastName(), userRegistration.getEmail(), userRegistration.getPassword(),
                        userTypeId, userSpecialityId, userRegistration.getAddress(), userRegistration.getPhone(),
                        userRegistration.getFax(), Constants.INACTIVE, new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()) });
        return value;
    }

    public int insertConfirmationToken(ConfirmationToken confirmationToken) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_CONFIRMATION_TOKEN,
                new Object[] { confirmationToken.getTokenid(), confirmationToken.getConfirmationToken(),
                        new Timestamp(System.currentTimeMillis()), confirmationToken.getUserId() });
        return value;
    }

    public boolean selectUserProfile(String email, String criteria) throws Exception {
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_USER_PROFILE).append(criteria);
     
        List<String> users = mysqlJdbcTemplate.query(query.toString(), new Object[] { email },
                (rs, rowNum) -> {
                    return rs.getString(1);
                });
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean selectConfirmationToken(String token) throws Exception {
        List<String> tokens = mysqlJdbcTemplate.query(QueryConstants.SELECT_CONFIRMATION_TOKEN, new Object[] { token },
                (rs, rowNum) -> {
                    return rs.getString(1);
                });
        if (tokens != null && tokens.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int updateUserProfile(String email, String indicator) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_PROFILE,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), email });
        return value;
    }

    public int deleteConfirmationToken(String token) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.DELETE_CONFIRMATION_TOKEN, new Object[] { token });
        return value;
    }

    public int insertUserReferral(String referralId, String userEmail, String docEmail, String isRegistered)
            throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_USER_REFERRAL,
                new Object[] { referralId, userEmail, docEmail, isRegistered, new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()) });
        return value;
    }

    public boolean selectUserReferral(String userReferralId) throws Exception {
        List<String> referrals = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_REFERRAL,
                new Object[] { userReferralId }, (rs, rowNum) -> {
                    return rs.getString(1);
                });
        if (referrals != null && referrals.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int updateUserReferral(String email, String indicator) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_REFERRAL,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), email });
        return value;
    }
    
    public List<UserRegistration> selectActiveUsers(String criteria) throws Exception {
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_ACTIVE_USER).append(criteria);
        List<UserRegistration> userRegistrations = mysqlJdbcTemplate.query(query.toString(),
                new Object[] {  }, (rs, rowNum) -> {
                    UserRegistration userRegistration = populateUser(rs);
                    return userRegistration;
                });
        if (userRegistrations != null && userRegistrations.size() > 0) {
            return userRegistrations;
        } else {
            return new ArrayList<UserRegistration>();
        }
    }
    
    public UserRegistration selectUser(String criteria) throws Exception {
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_ACTIVE_USER).append(criteria);
        List<UserRegistration> userRegistrations = mysqlJdbcTemplate.query(query.toString(),
                new Object[] {  }, (rs, rowNum) -> {
                    UserRegistration userRegistration = populateUser(rs);
                    return userRegistration;
                });
        if (userRegistrations != null && userRegistrations.size() > 0) {
            return userRegistrations.get(0);
        } else {
            return new UserRegistration();
        }
    }

    private UserRegistration populateUser(ResultSet rs) throws SQLException {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUserId(rs.getString(1));
        userRegistration.setFirstName(rs.getString(2));
        userRegistration.setLastName(rs.getString(3));
        userRegistration.setEmail(rs.getString(4));
        userRegistration.setUserType(rs.getString(5));
        userRegistration.setUserSpeciality(rs.getString(6));
        userRegistration.setAddress(rs.getString(7));
        userRegistration.setPhone(rs.getString(8));
        userRegistration.setFax(rs.getString(9));
        userRegistration.setIsActive(rs.getString(10));
        userRegistration.setPassword(rs.getString(11));
        return userRegistration;
    }
  
    public int insertAppointment(Appointment appointment, String referFrom, String referTo) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_APPOINTMENT,
                new Object[] { appointment.getAppointmentId(), referFrom, referTo, appointment.getDateAndTimeString(),
                        Constants.PENDING, Constants.INACTIVE, new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()) });
        return value;
    }
    
    public int updateAppointmentAccepted(String appointmentId, String indicator) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_APPOINTMENT_STATUS,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), appointmentId });
        return value;
    }
    
    public int updateAppointmentServed(String appointmentId, String indicator) throws Exception {
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_SERVED_STATUS,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), appointmentId });
        return value;
    }
    
    public List<Appointment> selectAppointments(String criteria) throws Exception {
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_APPOINTMENT).append(criteria);
        List<Appointment> appointments = mysqlJdbcTemplate.query(query.toString(),
                new Object[] {  }, (rs, rowNum) -> {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getString(1));
                    appointment.setAppointmentFrom(rs.getString(2));
                    appointment.setAppointmentTo(rs.getString(3));
                    appointment.setDateAndTimeString(rs.getString(4));
                    appointment.setIsAccepted(rs.getString(5));
                    appointment.setIsServed(rs.getString(6));
                    return appointment;
                });
        if (appointments != null && appointments.size() > 0) {
            return appointments;
        } else {
            return new ArrayList<Appointment>();
        }
    }
}
