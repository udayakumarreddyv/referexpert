package com.referexpert.referexpert.service;

import java.util.List;
import java.util.Optional;

import com.referexpert.referexpert.beans.Appointment;

/**
 * Service interface for handling appointment-related operations
 */
public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(String appointmentId);
    List<Appointment> getAppointmentsByFrom(String email);
    List<Appointment> getAppointmentsByTo(String email);
    List<Appointment> getPendingAppointments();
    Appointment createAppointment(Appointment appointment);
    boolean updateAppointmentStatus(String appointmentId, String status);
    boolean updateAppointmentResponse(String appointmentId, String response);
    boolean updateServedStatus(String appointmentId, String status);
    boolean deleteAppointment(String appointmentId);
    boolean hasPendingTasks(String type, String isAccepted, String isReferral, String email);
}
