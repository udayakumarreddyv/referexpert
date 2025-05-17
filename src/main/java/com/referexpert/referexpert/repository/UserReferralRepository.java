package com.referexpert.referexpert.repository;

import java.util.List;
import java.util.Optional;

import com.referexpert.referexpert.beans.UserReferral;

/**
 * Repository interface for UserReferral entity operations
 */
public interface UserReferralRepository extends BaseRepository<UserReferral, String> {
    Optional<UserReferral> findByUserEmail(String email);
    List<UserReferral> findByDoctorEmail(String docEmail);
    List<UserReferral> findNonRegisteredUsers();
    boolean existsByReferralIdAndDoctorEmail(String referralId, String docEmail);
    int updateRegistrationStatus(String email, String status);
    Optional<UserReferral> findByReferralId(String referralId);
}
