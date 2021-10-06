package com.referexpert.referexpert.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.RefreshToken;
import com.referexpert.referexpert.beans.UserNotification;
import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.constant.QueryConstants;

@Component
public class MySQLRepository {
    
    private final static Logger logger = LoggerFactory.getLogger(MySQLRepository.class);

    @Autowired
    private JdbcTemplate mysqlJdbcTemplate;
  
    public List<UserType> selectAllUserTypes() throws Exception {
        logger.info("MySQLRepository :: In selectAllUserTypes");
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
        logger.info("MySQLRepository :: In selectUserTypeByUserType");
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
        logger.info("MySQLRepository :: In selectUserTypeById");
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
        logger.info("MySQLRepository :: In selectUserSpecialityById");
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
        logger.info("MySQLRepository :: In selectUserSpecialityByUserType");
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
        logger.info("MySQLRepository :: In insertUserProfile");
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_USER_PROFILE,
                new Object[] { userRegistration.getUserId(), userRegistration.getFirstName(),
                        userRegistration.getLastName(), userRegistration.getEmail(), userRegistration.getPassword(),
                        userTypeId, userSpecialityId, userRegistration.getAddress(), userRegistration.getCity(),
                        userRegistration.getState(), userRegistration.getZip(), userRegistration.getPhone(),
                        userRegistration.getFax(), Constants.PENDING, userRegistration.getLattitude(),
                        userRegistration.getLongitude(), new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()), userRegistration.getService(), userRegistration.getInsurance() });
        return value;
    }

    public int insertConfirmationToken(ConfirmationToken confirmationToken) throws Exception {
        logger.info("MySQLRepository :: In insertConfirmationToken");
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_CONFIRMATION_TOKEN,
                new Object[] { confirmationToken.getTokenid(), confirmationToken.getConfirmationToken(),
                        new Timestamp(System.currentTimeMillis()), confirmationToken.getUserId() });
        return value;
    }

    public boolean selectUserProfile(String email, String criteria) throws Exception {
        logger.info("MySQLRepository :: In selectUserProfile");
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_USER_PROFILE).append(criteria);
        List<String> users = mysqlJdbcTemplate.query(query.toString(), new Object[] { email }, (rs, rowNum) -> {
            return rs.getString(1);
        });
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean selectConfirmationToken(String token) throws Exception {
        logger.info("MySQLRepository :: In selectConfirmationToken");
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

    public int updateUserActivation(String email, String indicator) throws Exception {
        logger.info("MySQLRepository :: In updateUserActivation");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_ACTIVATION,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), email });
        return value;
    }

    public int updateUserPassword(String email, String password) throws Exception {
        logger.info("MySQLRepository :: In updateUserPassword");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_PASSWORD,
                new Object[] { password, new Timestamp(System.currentTimeMillis()), email });
        return value;
    }

    public int updateUserProfile(UserRegistration userRegistration, Integer userTypeId, Integer userSpecialityId)
            throws Exception {
        logger.info("MySQLRepository :: In updateUserProfile");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_PROFILE,
                new Object[] { userRegistration.getFirstName(), userRegistration.getLastName(), userTypeId,
                        userSpecialityId, userRegistration.getAddress(), userRegistration.getCity(),
                        userRegistration.getState(), userRegistration.getZip(), userRegistration.getPhone(),
                        userRegistration.getFax(), userRegistration.getLattitude(), userRegistration.getLongitude(),
                        new Timestamp(System.currentTimeMillis()), userRegistration.getService(), userRegistration.getInsurance(),
                        userRegistration.getEmail() });
        return value;
    }

    public int deleteConfirmationToken(String token) throws Exception {
        logger.info("MySQLRepository :: In deleteConfirmationToken");
        int value = mysqlJdbcTemplate.update(QueryConstants.DELETE_CONFIRMATION_TOKEN, new Object[] { token });
        return value;
    }

    public int insertUserReferral(String referralId, String userEmail, String docEmail, String isRegistered)
            throws Exception {
        logger.info("MySQLRepository :: In insertUserReferral");
        int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_USER_REFERRAL,
                new Object[] { referralId, userEmail, docEmail, isRegistered, new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()) });
        return value;
    }

    public boolean selectUserReferral(String userReferralId, String docEmail) throws Exception {
        logger.info("MySQLRepository :: In selectUserReferral");
        List<String> referrals = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_REFERRAL,
                new Object[] { userReferralId, docEmail }, (rs, rowNum) -> {
                    return rs.getString(1);
                });
        if (referrals != null && referrals.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public List<UserReferral> selectNonRegisteredUsers() throws Exception {
        logger.info("MySQLRepository :: In selectNonRegisteredUsers");
        List<UserReferral> referrals = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_REFERRAL_NR,
                new Object[] {}, (rs, rowNum) -> {
                	UserReferral userReferral = populateUserReferral(rs);
                    return userReferral;
                });
        if (referrals != null && referrals.size() > 0) {
            return referrals;
        } else {
        	 return new ArrayList<UserReferral>();
        }
    }
    
    private UserReferral populateUserReferral(ResultSet rs) throws SQLException {
        logger.info("MySQLRepository :: In populateReferral");
        int i = 0;
        UserReferral userReferral = new UserReferral();
        userReferral.setUserReferralId(rs.getString(++i));
        userReferral.setUserEmail(rs.getString(++i));
        userReferral.setDocEmail(rs.getString(++i));
        userReferral.setIsRegistered(rs.getString(++i));
        return userReferral;
    }

    public int updateUserReferral(String email, String indicator) throws Exception {
        logger.info("MySQLRepository :: In updateUserReferral");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_REFERRAL,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), email });
        return value;
    }

    public List<UserRegistration> selectActiveUsers(String criteria) throws Exception {
        logger.info("MySQLRepository :: In selectActiveUsers");
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_ACTIVE_USERS).append(criteria);
        List<UserRegistration> userRegistrations = mysqlJdbcTemplate.query(query.toString(), new Object[] {},
                (rs, rowNum) -> {
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
        logger.info("MySQLRepository :: In selectUser");
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_ACTIVE_USER).append(criteria);
        List<UserRegistration> userRegistrations = mysqlJdbcTemplate.query(query.toString(), new Object[] {},
                (rs, rowNum) -> {
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
        logger.info("MySQLRepository :: In populateUser");
        int i = 0;
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUserId(rs.getString(++i));
        userRegistration.setFirstName(rs.getString(++i));
        userRegistration.setLastName(rs.getString(++i));
        userRegistration.setEmail(rs.getString(++i));
        userRegistration.setUserType(rs.getString(++i));
        userRegistration.setUserSpeciality(rs.getString(++i));
        userRegistration.setAddress(rs.getString(++i));
        userRegistration.setCity(rs.getString(++i));
        userRegistration.setState(rs.getString(++i));
        userRegistration.setZip(rs.getString(++i));
        userRegistration.setPhone(rs.getString(++i));
        userRegistration.setFax(rs.getString(++i));
        userRegistration.setIsActive(rs.getString(++i));
        userRegistration.setPassword(rs.getString(++i));
        userRegistration.setLattitude(rs.getDouble(++i));
        userRegistration.setLongitude(rs.getDouble(++i));
        userRegistration.setService(getValue(rs.getString(++i)));
        userRegistration.setInsurance(getValue(rs.getString(++i)));
        return userRegistration;
    }
    
    public int insertAppointment(Appointment appointment, String referFrom, String referTo, String type) throws Exception {
        logger.info("MySQLRepository :: In insertAppointment");
        int value = 0;
        if(Constants.APPOINTMENT.equals(type)) {
        	value = mysqlJdbcTemplate.update(QueryConstants.INSERT_APPOINTMENT,
                new Object[] { appointment.getAppointmentId(), referFrom, referTo, appointment.getDateAndTimeString(),
                        appointment.getSubject(), appointment.getReason(), Constants.PENDING, Constants.INACTIVE, Constants.INACTIVE,
                        new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()) });
        } else {
        	value = mysqlJdbcTemplate.update(QueryConstants.INSERT_APPOINTMENT,
                    new Object[] { appointment.getAppointmentId(), referFrom, referTo, appointment.getDateAndTimeString(),
                            appointment.getSubject(), appointment.getReason(), Constants.PENDING, null, Constants.ACTIVE, 
                            new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()) });
        }
        return value;
    }

    public int updateAppointmentAccepted(String appointmentId, String indicator) throws Exception {
        logger.info("MySQLRepository :: In updateAppointmentAccepted");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_APPOINTMENT_STATUS,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), appointmentId });
        return value;
    }
    
    public int updateAppointmentResponse(String appointmentId, String response) throws Exception {
        logger.info("MySQLRepository :: In updateAppointmentResponse");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_APPOINTMENT_RESPONSE,
                new Object[] { response, Constants.ACTIVE, new Timestamp(System.currentTimeMillis()), appointmentId });
        return value;
    }

    public int updateAppointmentServed(String appointmentId, String indicator) throws Exception {
        logger.info("MySQLRepository :: In updateAppointmentServed");
        int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_SERVED_STATUS,
                new Object[] { indicator, new Timestamp(System.currentTimeMillis()), appointmentId });
        return value;
    }

    public List<Appointment> selectAppointments(String criteria) throws Exception {
        logger.info("MySQLRepository :: In selectAppointments");
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_APPOINTMENT).append(criteria)
        		.append(QueryConstants.ORDERBY_APPOINT_TIMESTAMP);
        List<Appointment> appointments = mysqlJdbcTemplate.query(query.toString(), new Object[] {}, (rs, rowNum) -> {
            int i = 0;
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getString(++i));
            appointment.setAppointmentFrom(rs.getString(++i));
            appointment.setFromFirstName(rs.getString(++i));
            appointment.setFromLastName(rs.getString(++i));
            appointment.setAppointmentTo(rs.getString(++i));
            appointment.setToFirstName(rs.getString(++i));
            appointment.setToLastName(rs.getString(++i));
            appointment.setDateAndTimeString(getValue(rs.getString(++i)));
            appointment.setIsAccepted(getValue(rs.getString(++i)));
            appointment.setIsServed(getValue(rs.getString(++i)));
            appointment.setIsAvailabilityCheck(getValue(rs.getString(++i)));
            appointment.setSubject(getValue(rs.getString(++i)));
            appointment.setReason(getValue(rs.getString(++i)));
            return appointment;
        });
        if (appointments != null && appointments.size() > 0) {
            return appointments;
        } else {
            return new ArrayList<Appointment>();
        }
    }
    
	public RefreshToken findByRequestTokenId(String refreshTokenId) {
		logger.info("MySQLRepository :: In findByRequestTokenId");
		StringBuffer query = new StringBuffer(QueryConstants.SELECT_REFRESH_TOKEN)
				.append("refresh_token_id = '" + refreshTokenId + "'");
		List<RefreshToken> refreshTokens = mysqlJdbcTemplate.query(query.toString(), new Object[] {}, (rs, rowNum) -> {
			RefreshToken refreshToken = populateRefreshToken(rs);
			return refreshToken;
		});
		if (refreshTokens != null && refreshTokens.size() > 0) {
			return refreshTokens.get(0);
		} else {
			return new RefreshToken();
		}
	}

	public RefreshToken findByRequestToken(String token) {
		logger.info("MySQLRepository :: In findByRequestToken");
		StringBuffer query = new StringBuffer(QueryConstants.SELECT_REFRESH_TOKEN).append("token = '" + token + "'");
		List<RefreshToken> refreshTokens = mysqlJdbcTemplate.query(query.toString(), new Object[] {}, (rs, rowNum) -> {
			RefreshToken refreshToken = populateRefreshToken(rs);
			return refreshToken;
		});
		if (refreshTokens != null && refreshTokens.size() > 0) {
			return refreshTokens.get(0);
		} else {
			return new RefreshToken();
		}
	}

	private RefreshToken populateRefreshToken(ResultSet rs) throws SQLException {
		int i = 0;
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshTokenId(rs.getString(++i));
		refreshToken.setUserId(rs.getString(++i));
		refreshToken.setToken(rs.getString(++i));
		refreshToken.setExpiryDate(rs.getTimestamp(++i));
		return refreshToken;
	}
	
	public int insertRefreshToken(RefreshToken refreshToken) throws Exception {
		logger.info("MySQLRepository :: In insertRefreshToken");
		int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_REFRESH_TOKEN,
				new Object[] { refreshToken.getRefreshTokenId(), refreshToken.getUserId(), refreshToken.getToken(),
						refreshToken.getExpiryDate() });
		return value;
	}
	
	public int deleteRefreshToken(String token) throws Exception {
        logger.info("MySQLRepository :: In deleteRefreshToken");
        int value = mysqlJdbcTemplate.update(QueryConstants.DELETE_REFRESH_TOKEN, new Object[] { token });
        return value;
    }
	
	public int cleanupRefreshToken() throws Exception {
        logger.info("MySQLRepository :: In cleanupRefreshToken");
        int value = mysqlJdbcTemplate.update(QueryConstants.CLEANUP_REFRESH_TOKEN, new Object[] { });
        return value;
    }
	
	public int markAppointmentComplete() throws Exception {
        logger.info("MySQLRepository :: In markAppointmentComplete");
        int value = mysqlJdbcTemplate.update(QueryConstants.MARK_APPOINTMENT_COMPLETE, new Object[] { });
        return value;
    }
	
	public int deleteRefreshTokenByUser(String userId) throws Exception {
        logger.info("MySQLRepository :: In deleteRefreshTokenByUser");
        int value = mysqlJdbcTemplate.update(QueryConstants.DELETE_REFRESH_TOKEN_BYUSER, new Object[] { userId });
        return value;
    }
	
	public int getUserCountByStatus(String activeFlag) {
		logger.info("MySQLRepository :: In getUserCountByStatus");
		List<Integer> counts = mysqlJdbcTemplate.query(QueryConstants.SELECT_USER_COUNTS, new Object[] { activeFlag }, (rs, rowNum) -> {
            return rs.getInt(1);
        });
        if (counts != null && counts.size() > 0) {
            return counts.get(0);
        } else {
            return 0;
        }
	}
	
	public UserNotification selectUserNotification(String criteria) throws Exception {
        logger.info("MySQLRepository :: In selectUserNotification");
        StringBuffer query = new StringBuffer(QueryConstants.SELECT_USER_NOTIFICATION).append(criteria);
        List<UserNotification> userNotifications = mysqlJdbcTemplate.query(query.toString(), new Object[] {},
                (rs, rowNum) -> {
                	int i = 0;
                	UserNotification userNotification = new UserNotification();
                    userNotification.setUserEmail(rs.getString(++i));
                    userNotification.setNotificationEmail(rs.getString(++i));
                    userNotification.setNotificationMobile(rs.getString(++i));
                    return userNotification;
                });
        if (userNotifications != null && userNotifications.size() > 0) {
            return userNotifications.get(0);
        } else {
            return null;
        }
    }
	
	public int insertUserNotification(UserNotification userNotification, String userId) throws Exception {
		logger.info("MySQLRepository :: In insertUserNotification");
		int value = mysqlJdbcTemplate.update(QueryConstants.INSERT_USER_NOTIFICATION,
				new Object[] { userId, userNotification.getNotificationEmail(),
						userNotification.getNotificationMobile(), new Timestamp(System.currentTimeMillis()),
						new Timestamp(System.currentTimeMillis()) });
		return value;
	}

	public int updateUsernotification(UserNotification userNotification, String userId) throws Exception {
		logger.info("MySQLRepository :: In updateUsernotification");
		int value = mysqlJdbcTemplate.update(QueryConstants.UPDATE_USER_NOTIFICATION,
				new Object[] { userNotification.getNotificationEmail(), userNotification.getNotificationMobile(),
						new Timestamp(System.currentTimeMillis()), userId });
		return value;
	}
	
	private String getValue(String str) {
    	return str != null? str : "";
    }
	
	public boolean getPendingTasks(String isReferral, String email) {
		logger.info("MySQLRepository :: In getPendingTasks");
		List<String> userIds = mysqlJdbcTemplate.query(QueryConstants.PENDING_TASKS, new Object[] { isReferral, email }, (rs, rowNum) -> {
            return rs.getString(1);
        });
        if (userIds != null && userIds.size() > 0) {
            return true;
        } else {
            return false;
        }
	}
    
}
