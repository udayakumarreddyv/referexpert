package com.referexpert.referexpert.service;

import java.util.List;
import java.util.Optional;

import com.referexpert.referexpert.beans.UserReferral;

/**
 * Service interface for handling user referral operations
 */
public interface UserReferralService {
    List<UserReferral> getAllReferrals();
    Optional<UserReferral> getReferralById(String referralId);
    Optional<UserReferral> getReferralByUserEmail(String email);
    List<UserReferral> getReferralsByDoctorEmail(String docEmail);
    List<UserReferral> getNonRegisteredUsers();
    UserReferral createReferral(UserReferral referral);
    boolean updateRegistrationStatus(String email, String status);
    boolean deleteReferral(String referralId);
    boolean existsByReferralIdAndDoctorEmail(String referralId, String docEmail);
}
