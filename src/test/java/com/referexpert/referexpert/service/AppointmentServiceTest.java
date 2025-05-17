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

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.repository.AppointmentRepository;
import com.referexpert.referexpert.service.impl.AppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

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
    void getAllAppointments_ShouldReturnAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(testAppointment));

        List<Appointment> result = appointmentService.getAllAppointments();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("test-id", result.get(0).getAppointmentId());
    }

    @Test
    void getAppointmentById_WhenExists_ShouldReturnAppointment() {
        when(appointmentRepository.findById("test-id")).thenReturn(Optional.of(testAppointment));

        Optional<Appointment> result = appointmentService.getAppointmentById("test-id");

        assertTrue(result.isPresent());
        assertEquals("test-id", result.get().getAppointmentId());
    }

    @Test
    void getAppointmentById_WhenNotExists_ShouldReturnEmpty() {
        when(appointmentRepository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentService.getAppointmentById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void createAppointment_ShouldGenerateIdAndSave() {
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentFrom("from@test.com");
        
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        Appointment result = appointmentService.createAppointment(newAppointment);

        assertNotNull(result.getAppointmentId());
        assertEquals("test-id", result.getAppointmentId());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentStatus_WhenSuccessful_ShouldReturnTrue() {
        when(appointmentRepository.updateAppointmentStatus("test-id", Constants.ACTIVE)).thenReturn(1);

        boolean result = appointmentService.updateAppointmentStatus("test-id", Constants.ACTIVE);

        assertTrue(result);
        verify(appointmentRepository).updateAppointmentStatus("test-id", Constants.ACTIVE);
    }

    @Test
    void updateAppointmentStatus_WhenFailed_ShouldReturnFalse() {
        when(appointmentRepository.updateAppointmentStatus("test-id", Constants.ACTIVE)).thenReturn(0);

        boolean result = appointmentService.updateAppointmentStatus("test-id", Constants.ACTIVE);

        assertFalse(result);
        verify(appointmentRepository).updateAppointmentStatus("test-id", Constants.ACTIVE);
    }

    @Test
    void deleteAppointment_WhenSuccessful_ShouldReturnTrue() {
        doNothing().when(appointmentRepository).deleteById("test-id");

        boolean result = appointmentService.deleteAppointment("test-id");

        assertTrue(result);
        verify(appointmentRepository).deleteById("test-id");
    }

    @Test
    void deleteAppointment_WhenFailed_ShouldReturnFalse() {
        doThrow(new RuntimeException("Delete failed")).when(appointmentRepository).deleteById("test-id");

        boolean result = appointmentService.deleteAppointment("test-id");

        assertFalse(result);
        verify(appointmentRepository).deleteById("test-id");
    }

    @Test
    void hasPendingTasks_ShouldDelegateToRepository() {
        when(appointmentRepository.hasPendingTasks(any(), any(), any(), any())).thenReturn(true);

        boolean result = appointmentService.hasPendingTasks(
            Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com");

        assertTrue(result);
        verify(appointmentRepository).hasPendingTasks(
            Constants.RESPONSE, Constants.ACTIVE, Constants.INACTIVE, "test@test.com");
    }
}
