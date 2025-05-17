package com.referexpert.referexpert.repository;

import java.util.List;
import java.util.Optional;

import com.referexpert.referexpert.beans.UserRegistration;

/**
 * Repository interface for UserRegistration entity operations
 */
public interface UserRegistrationRepository extends BaseRepository<UserRegistration, String> {
    Optional<UserRegistration> findByEmail(String email);
    List<UserRegistration> findActiveUsersByType(String userType);
    List<UserRegistration> findActiveUsersWithinDistance(double latitude, double longitude, int distance);
    int updatePassword(String email, String encodedPassword);
    int updateActivationStatus(String email, String status);
    int updateProfile(UserRegistration userRegistration);
    Optional<UserRegistration> findActiveUserByEmail(String email);
}
