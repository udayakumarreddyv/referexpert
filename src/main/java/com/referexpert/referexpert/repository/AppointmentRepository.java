package com.referexpert.referexpert.repository;

import java.util.List;
import java.util.Optional;

import com.referexpert.referexpert.beans.Appointment;

/**
 * Repository interface for Appointment entity operations
 */
public interface AppointmentRepository extends BaseRepository<Appointment, String> {
    List<Appointment> findByAppointmentFrom(String email);
    List<Appointment> findByAppointmentTo(String email);
    List<Appointment> findPendingAppointments();
    Optional<Appointment> findByAppointmentId(String appointmentId);
    int updateAppointmentStatus(String appointmentId, String status);
    int updateAppointmentResponse(String appointmentId, String response);
    int updateServedStatus(String appointmentId, String status);
    boolean hasPendingTasks(String type, String isAccepted, String isReferral, String email);
}
