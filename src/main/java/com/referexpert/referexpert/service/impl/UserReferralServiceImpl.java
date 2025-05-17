package com.referexpert.referexpert.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.repository.UserReferralRepository;
import com.referexpert.referexpert.service.UserReferralService;

@Service
public class UserReferralServiceImpl implements UserReferralService {

    private static final Logger logger = LoggerFactory.getLogger(UserReferralServiceImpl.class);

    @Autowired
    private UserReferralRepository userReferralRepository;

    @Override
    public List<UserReferral> getAllReferrals() {
        logger.debug("Fetching all user referrals");
        return userReferralRepository.findAll();
    }

    @Override
    public Optional<UserReferral> getReferralById(String referralId) {
        logger.debug("Fetching user referral by ID: {}", referralId);
        return userReferralRepository.findById(referralId);
    }

    @Override
    public Optional<UserReferral> getReferralByUserEmail(String email) {
        logger.debug("Fetching user referral by email: {}", email);
        return userReferralRepository.findByUserEmail(email);
    }

    @Override
    public List<UserReferral> getReferralsByDoctorEmail(String docEmail) {
        logger.debug("Fetching user referrals by doctor email: {}", docEmail);
        return userReferralRepository.findByDoctorEmail(docEmail);
    }

    @Override
    public List<UserReferral> getNonRegisteredUsers() {
        logger.debug("Fetching non-registered users");
        return userReferralRepository.findNonRegisteredUsers();
    }

    @Override
    @Transactional
    public UserReferral createReferral(UserReferral referral) {
        logger.debug("Creating new user referral: {}", referral);
        if (referral.getUserReferralId() == null) {
            referral.setUserReferralId(UUID.randomUUID().toString());
        }
        return userReferralRepository.save(referral);
    }

    @Override
    @Transactional
    public boolean updateRegistrationStatus(String email, String status) {
        logger.debug("Updating registration status for email: {} to {}", email, status);
        return userReferralRepository.updateRegistrationStatus(email, status) > 0;
    }

    @Override
    @Transactional
    public boolean deleteReferral(String referralId) {
        logger.debug("Deleting user referral: {}", referralId);
        try {
            userReferralRepository.deleteById(referralId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting user referral: {}", referralId, e);
            return false;
        }
    }

    @Override
    public boolean existsByReferralIdAndDoctorEmail(String referralId, String docEmail) {
        logger.debug("Checking existence of referral ID: {} and doctor email: {}", referralId, docEmail);
        return userReferralRepository.existsByReferralIdAndDoctorEmail(referralId, docEmail);
    }
}
