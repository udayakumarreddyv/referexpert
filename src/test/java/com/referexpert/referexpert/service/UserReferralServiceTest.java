package com.referexpert.referexpert.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.repository.UserReferralRepository;
import com.referexpert.referexpert.service.impl.UserReferralServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserReferralServiceTest {

    @Mock
    private UserReferralRepository userReferralRepository;

    @InjectMocks
    private UserReferralServiceImpl userReferralService;

    private UserReferral testReferral;

    @BeforeEach
    void setUp() {
        testReferral = new UserReferral();
        testReferral.setUserReferralId("test-id");
        testReferral.setUserEmail("user@test.com");
        testReferral.setDocEmail("doc@test.com");
        testReferral.setIsRegistered(Constants.INACTIVE);
    }

    @Test
    void getAllReferrals_ShouldReturnAllReferrals() {
        when(userReferralRepository.findAll()).thenReturn(Arrays.asList(testReferral));

        List<UserReferral> result = userReferralService.getAllReferrals();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("test-id", result.get(0).getUserReferralId());
    }

    @Test
    void getReferralById_WhenExists_ShouldReturnReferral() {
        when(userReferralRepository.findById("test-id")).thenReturn(Optional.of(testReferral));

        Optional<UserReferral> result = userReferralService.getReferralById("test-id");

        assertTrue(result.isPresent());
        assertEquals("test-id", result.get().getUserReferralId());
    }

    @Test
    void getReferralById_WhenNotExists_ShouldReturnEmpty() {
        when(userReferralRepository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<UserReferral> result = userReferralService.getReferralById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void getReferralByUserEmail_WhenExists_ShouldReturnReferral() {
        when(userReferralRepository.findByUserEmail("user@test.com")).thenReturn(Optional.of(testReferral));

        Optional<UserReferral> result = userReferralService.getReferralByUserEmail("user@test.com");

        assertTrue(result.isPresent());
        assertEquals("user@test.com", result.get().getUserEmail());
    }

    @Test
    void getReferralsByDoctorEmail_ShouldReturnMatchingReferrals() {
        when(userReferralRepository.findByDoctorEmail("doc@test.com"))
            .thenReturn(Arrays.asList(testReferral));

        List<UserReferral> result = userReferralService.getReferralsByDoctorEmail("doc@test.com");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("doc@test.com", result.get(0).getDocEmail());
    }

    @Test
    void getNonRegisteredUsers_ShouldReturnNonRegisteredUsers() {
        when(userReferralRepository.findNonRegisteredUsers()).thenReturn(Arrays.asList(testReferral));

        List<UserReferral> result = userReferralService.getNonRegisteredUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(Constants.INACTIVE, result.get(0).getIsRegistered());
    }

    @Test
    void createReferral_ShouldGenerateIdAndSave() {
        UserReferral newReferral = new UserReferral();
        newReferral.setUserEmail("user@test.com");
        
        when(userReferralRepository.save(any(UserReferral.class))).thenReturn(testReferral);

        UserReferral result = userReferralService.createReferral(newReferral);

        assertNotNull(result.getUserReferralId());
        assertEquals("test-id", result.getUserReferralId());
        verify(userReferralRepository).save(any(UserReferral.class));
    }

    @Test
    void updateRegistrationStatus_WhenSuccessful_ShouldReturnTrue() {
        when(userReferralRepository.updateRegistrationStatus("user@test.com", Constants.ACTIVE)).thenReturn(1);

        boolean result = userReferralService.updateRegistrationStatus("user@test.com", Constants.ACTIVE);

        assertTrue(result);
        verify(userReferralRepository).updateRegistrationStatus("user@test.com", Constants.ACTIVE);
    }

    @Test
    void updateRegistrationStatus_WhenFailed_ShouldReturnFalse() {
        when(userReferralRepository.updateRegistrationStatus("user@test.com", Constants.ACTIVE)).thenReturn(0);

        boolean result = userReferralService.updateRegistrationStatus("user@test.com", Constants.ACTIVE);

        assertFalse(result);
        verify(userReferralRepository).updateRegistrationStatus("user@test.com", Constants.ACTIVE);
    }

    @Test
    void deleteReferral_WhenSuccessful_ShouldReturnTrue() {
        doNothing().when(userReferralRepository).deleteById("test-id");

        boolean result = userReferralService.deleteReferral("test-id");

        assertTrue(result);
        verify(userReferralRepository).deleteById("test-id");
    }

    @Test
    void deleteReferral_WhenFailed_ShouldReturnFalse() {
        doThrow(new RuntimeException("Delete failed")).when(userReferralRepository).deleteById("test-id");

        boolean result = userReferralService.deleteReferral("test-id");

        assertFalse(result);
        verify(userReferralRepository).deleteById("test-id");
    }

    @Test
    void existsByReferralIdAndDoctorEmail_ShouldDelegateToRepository() {
        when(userReferralRepository.existsByReferralIdAndDoctorEmail("test-id", "doc@test.com"))
            .thenReturn(true);

        boolean result = userReferralService.existsByReferralIdAndDoctorEmail("test-id", "doc@test.com");

        assertTrue(result);
        verify(userReferralRepository).existsByReferralIdAndDoctorEmail("test-id", "doc@test.com");
    }
}
