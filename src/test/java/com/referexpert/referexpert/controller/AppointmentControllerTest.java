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
import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.AppointmentService;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        testAppointment = new Appointment();
        testAppointment.setAppointmentId("test-id");
        testAppointment.setAppointmentFrom("from@test.com");
        testAppointment.setAppointmentTo("to@test.com");
        testAppointment.setDateAndTimeString("2025-05-17 10:00:00");
        testAppointment.setIsAccepted(Constants.PENDING);
        testAppointment.setIsServed(Constants.INACTIVE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAppointments_ShouldReturnAllAppointments() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(testAppointment));

        mockMvc.perform(get("/api/appointments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].appointmentId").value("test-id"))
            .andExpect(jsonPath("$[0].appointmentFrom").value("from@test.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAppointmentById_WhenExists_ShouldReturnAppointment() throws Exception {
        when(appointmentService.getAppointmentById("test-id")).thenReturn(Optional.of(testAppointment));

        mockMvc.perform(get("/api/appointments/test-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.appointmentId").value("test-id"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAppointmentById_WhenNotExists_ShouldReturn404() throws Exception {
        when(appointmentService.getAppointmentById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/non-existent"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createAppointment_ShouldReturnCreatedAppointment() throws Exception {
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(testAppointment);

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppointment)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.appointmentId").value("test-id"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAppointmentStatus_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(appointmentService.updateAppointmentStatus("test-id", Constants.ACTIVE)).thenReturn(true);

        mockMvc.perform(put("/api/appointments/test-id/status/" + Constants.ACTIVE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }    @Test
    @WithMockUser(roles = "USER")
    void updateAppointmentStatus_WhenFailed_ShouldReturnFailure() throws Exception {
        when(appointmentService.updateAppointmentStatus("test-id", Constants.ACTIVE)).thenReturn(false);

        mockMvc.perform(put("/api/appointments/test-id/status/" + Constants.ACTIVE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.FAILURE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAppointmentResponse_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(appointmentService.updateAppointmentResponse("test-id", "ACCEPTED")).thenReturn(true);

        mockMvc.perform(put("/api/appointments/test-id/response/ACCEPTED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAppointmentResponse_WhenFailed_ShouldReturnFailure() throws Exception {
        when(appointmentService.updateAppointmentResponse("test-id", "ACCEPTED")).thenReturn(false);

        mockMvc.perform(put("/api/appointments/test-id/response/ACCEPTED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.FAILURE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateServedStatus_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(appointmentService.updateServedStatus("test-id", Constants.ACTIVE)).thenReturn(true);

        mockMvc.perform(put("/api/appointments/test-id/served/" + Constants.ACTIVE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateServedStatus_WhenFailed_ShouldReturnFailure() throws Exception {
        when(appointmentService.updateServedStatus("test-id", Constants.ACTIVE)).thenReturn(false);

        mockMvc.perform(put("/api/appointments/test-id/served/" + Constants.ACTIVE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.FAILURE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteAppointment_WhenSuccessful_ShouldReturnSuccess() throws Exception {
        when(appointmentService.deleteAppointment("test-id")).thenReturn(true);

        mockMvc.perform(delete("/api/appointments/test-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteAppointment_WhenFailed_ShouldReturnFailure() throws Exception {
        when(appointmentService.deleteAppointment("test-id")).thenReturn(false);

        mockMvc.perform(delete("/api/appointments/test-id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.FAILURE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void hasPendingTasks_WhenHasTasks_ShouldReturnSuccess() throws Exception {
        when(appointmentService.hasPendingTasks(
            Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com"))
            .thenReturn(true);

        mockMvc.perform(get("/api/appointments/pending-tasks/{type}/{isAccepted}/{isReferral}/{email}",
                Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.SUCCESS));
    }

    @Test
    @WithMockUser(roles = "USER")
    void hasPendingTasks_WhenNoTasks_ShouldReturnFailure() throws Exception {
        when(appointmentService.hasPendingTasks(
            Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com"))
            .thenReturn(false);

        mockMvc.perform(get("/api/appointments/pending-tasks/{type}/{isAccepted}/{isReferral}/{email}",
                Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Constants.FAILURE));
    }

    @Test
    void whenUnauthorizedAccess_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/appointments"))
            .andExpect(status().isUnauthorized());
    }
}
