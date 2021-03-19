package com.referexpert.referexpert.service;

import java.util.List;

import com.referexpert.referexpert.beans.ConfirmationToken;
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.beans.UserSpeciality;
import com.referexpert.referexpert.beans.UserType;

public interface MySQLService {

    public List<UserType> selectAllUserTypes();

    public List<UserType> selectUserTypeByUserType(String userType);

    public UserSpeciality selectUserSpecialityByUserType(String userType);

    public int insertUserProfile(UserRegistration userRegistration);

    public Integer selectUserTypeById(String userType);

    public Integer selectUserSpecialityById(Integer userTypeId, String speciality);

    public int insertConfirmationToken(ConfirmationToken confirmationToken);

    public boolean selectUserProfile(String email, String criteria);

    public boolean selectConfirmationToken(String token);

    public int updateUserProfile(String email, String indicator);

    public int deleteConfirmationToken(String token);

    public int insertUserReferral(String referralId, String userEmail, String docEmail, String isRegistered);

    public boolean selectUserReferral(String userReferralId);

    public int updateUserReferral(String email, String indicator);

    public List<UserRegistration> selectActiveUsers(String criteria);

    public int insertAppointment(Appointment referExpert);

    public int updateAppointmentAccepted(String referExpertId, String indicator);

    public int updateAppointmentServed(String referExpertId, String indicator);
    
    public List<Appointment> selectAppointments(String criteria);
    
    public UserRegistration selectUser(String criteria);
    
    public UserRegistration save(UserRegistration userRegistration);
}
