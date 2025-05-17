package com.referexpert.referexpert.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.UserReferralService;

@RestController
@RequestMapping("/api/referrals")
public class UserReferralController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserReferralController.class);
    
    @Autowired
    private UserReferralService userReferralService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserReferral>> getAllReferrals() {
        logger.info("Getting all user referrals");
        return ResponseEntity.ok(userReferralService.getAllReferrals());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getReferralById(@PathVariable("id") String referralId) {
        logger.info("Getting user referral by ID: {}", referralId);
        return userReferralService.getReferralById(referralId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getReferralByUserEmail(@PathVariable String email) {
        logger.info("Getting user referral by email: {}", email);
        return userReferralService.getReferralByUserEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/doctor/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserReferral>> getReferralsByDoctorEmail(@PathVariable String email) {
        logger.info("Getting user referrals by doctor email: {}", email);
        return ResponseEntity.ok(userReferralService.getReferralsByDoctorEmail(email));
    }
    
    @GetMapping("/non-registered")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserReferral>> getNonRegisteredUsers() {
        logger.info("Getting non-registered users");
        return ResponseEntity.ok(userReferralService.getNonRegisteredUsers());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserReferral> createReferral(@RequestBody UserReferral referral) {
        logger.info("Creating new user referral");
        return ResponseEntity.ok(userReferralService.createReferral(referral));
    }
    
    @PutMapping("/{email}/status/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateRegistrationStatus(
            @PathVariable String email,
            @PathVariable String status) {
        logger.info("Updating registration status for email: {} to {}", email, status);
        boolean updated = userReferralService.updateRegistrationStatus(email, status);
        return getGenericResponse(updated, "Registration status updated successfully", "Failed to update registration status");
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> deleteReferral(@PathVariable("id") String referralId) {
        logger.info("Deleting user referral: {}", referralId);
        boolean deleted = userReferralService.deleteReferral(referralId);
        return getGenericResponse(deleted, "Referral deleted successfully", "Failed to delete referral");
    }
    
    @GetMapping("/exists/{referralId}/{docEmail}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> checkReferralExists(
            @PathVariable String referralId,
            @PathVariable String docEmail) {
        logger.info("Checking if referral exists: {} for doctor: {}", referralId, docEmail);
        boolean exists = userReferralService.existsByReferralIdAndDoctorEmail(referralId, docEmail);
        return ResponseEntity.ok(new GenericResponse(
            exists ? Constants.SUCCESS : Constants.FAILURE,
            exists ? "Referral exists" : "Referral not found"
        ));
    }
    
    private ResponseEntity<GenericResponse> getGenericResponse(boolean success, String successMessage, String failureMessage) {
        return ResponseEntity.ok(new GenericResponse(
            success ? Constants.SUCCESS : Constants.FAILURE,
            success ? successMessage : failureMessage
        ));
    }
}
