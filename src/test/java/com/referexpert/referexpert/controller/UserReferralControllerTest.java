package com.referexpert.referexpert.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.UserReferralService;

@WebMvcTest(UserReferralController.class)
public class UserReferralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserReferralService userReferralService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser(roles = "ADMIN")
    void getAllReferrals_ShouldReturnAllReferrals() throws Exception {
        when(userReferralService.getAllReferrals()).thenReturn(Arrays.asList(testReferral));

        mockMvc.perform(get("/api/referrals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userReferralId").value("test-id"))
            .andExpect(jsonPath("$[0].userEmail").value("user@test.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getReferralById_WhenExists_ShouldReturnReferral() throws Exception {
        when(userReferralService.getReferralById("test-id")).thenReturn(Optional.of(testReferral));

        mockMvc.perform(get("/api/referrals/test-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userReferralId").value("test-id"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getReferralById_WhenNotExists_ShouldReturn404() throws Exception {
        when(userReferralService.getReferralById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/referrals/non-existent"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getReferralByUserEmail_WhenExists_ShouldReturnReferral() throws Exception {
        when(userReferralService.getReferralByUserEmail("user@test.com"))
            .thenReturn(Optional.of(testReferral));

        mockMvc.perform(get("/api/referrals/user/user@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userEmail").value("user@test.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getReferralsByDoctorEmail_ShouldReturnMatchingReferrals() throws Exception {
        when(userReferralService.getReferralsByDoctorEmail("doc@test.com"))
            .thenReturn(Arrays.asList(testReferral));

        mockMvc.perform(get("/api/referrals/doctor/doc@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].docEmail").value("doc@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getNonRegisteredUsers_ShouldReturnNonRegisteredUsers() throws Exception {
        when(userReferralService.getNonRegisteredUsers()).thenReturn(Arrays.asList(testReferral));

        mockMvc.perform(get("/api/referrals/non-registered"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].isRegistered").value(Constants.INACTIVE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createReferral_ShouldReturnCreatedReferral() throws Exception {
        when(userReferralService.createReferral(any(UserReferral.class))).thenReturn(testReferral);

        mockMvc.perform(post("/api/referrals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReferral)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userReferralId").value("test-id"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateRegistrationStatus_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(userReferralService.updateRegistrationStatus("user@test.com", Constants.ACTIVE))
            .thenReturn(true);

        mockMvc.perform(put("/api/referrals/user@test.com/status/" + Constants.ACTIVE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteReferral_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(userReferralService.deleteReferral("test-id")).thenReturn(true);

        mockMvc.perform(delete("/api/referrals/test-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void checkReferralExists_WhenExists_ShouldReturnSuccess() throws Exception {
        when(userReferralService.existsByReferralIdAndDoctorEmail("test-id", "doc@test.com"))
            .thenReturn(true);

        mockMvc.perform(get("/api/referrals/exists/test-id/doc@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }
}
