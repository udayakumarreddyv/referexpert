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

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.beans.GenericResponse;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private AppointmentService appointmentService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        logger.info("Getting all appointments");
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAppointmentById(@PathVariable("id") String appointmentId) {
        logger.info("Getting appointment by ID: {}", appointmentId);
        return appointmentService.getAppointmentById(appointmentId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/from/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAppointmentsFrom(@PathVariable String email) {
        logger.info("Getting appointments from email: {}", email);
        return ResponseEntity.ok(appointmentService.getAppointmentsByFrom(email));
    }
    
    @GetMapping("/to/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAppointmentsTo(@PathVariable String email) {
        logger.info("Getting appointments to email: {}", email);
        return ResponseEntity.ok(appointmentService.getAppointmentsByTo(email));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getPendingAppointments() {
        logger.info("Getting pending appointments");
        return ResponseEntity.ok(appointmentService.getPendingAppointments());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        logger.info("Creating new appointment");
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }
    
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateAppointmentStatus(
            @PathVariable("id") String appointmentId,
            @PathVariable("status") String status) {
        logger.info("Updating appointment status: {} to {}", appointmentId, status);
        boolean updated = appointmentService.updateAppointmentStatus(appointmentId, status);
        return getGenericResponse(updated, "Appointment status updated successfully", "Failed to update appointment status");
    }
    
    @PutMapping("/{id}/response/{response}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateAppointmentResponse(
            @PathVariable("id") String appointmentId,
            @PathVariable("response") String response) {
        logger.info("Updating appointment response: {} to {}", appointmentId, response);
        boolean updated = appointmentService.updateAppointmentResponse(appointmentId, response);
        return getGenericResponse(updated, "Appointment response updated successfully", "Failed to update appointment response");
    }
    
    @PutMapping("/{id}/served/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateServedStatus(
            @PathVariable("id") String appointmentId,
            @PathVariable("status") String status) {
        logger.info("Updating appointment served status: {} to {}", appointmentId, status);
        boolean updated = appointmentService.updateServedStatus(appointmentId, status);
        return getGenericResponse(updated, "Appointment served status updated successfully", "Failed to update appointment served status");
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> deleteAppointment(@PathVariable("id") String appointmentId) {
        logger.info("Deleting appointment: {}", appointmentId);
        boolean deleted = appointmentService.deleteAppointment(appointmentId);
        return getGenericResponse(deleted, "Appointment deleted successfully", "Failed to delete appointment");
    }
    
    @GetMapping("/pending-tasks/{type}/{isAccepted}/{isReferral}/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> hasPendingTasks(
            @PathVariable("type") String type,
            @PathVariable("isAccepted") String isAccepted,
            @PathVariable("isReferral") String isReferral,
            @PathVariable("email") String email) {
        logger.info("Checking pending tasks for email: {}", email);
        boolean hasPending = appointmentService.hasPendingTasks(type, isAccepted, isReferral, email);
        return ResponseEntity.ok(new GenericResponse(
            hasPending ? Constants.SUCCESS : Constants.FAILURE,
            hasPending ? "Pending tasks found" : "No pending tasks found"
        ));
    }
    
    private ResponseEntity<GenericResponse> getGenericResponse(boolean success, String successMessage, String failureMessage) {
        return ResponseEntity.ok(new GenericResponse(
            success ? Constants.SUCCESS : Constants.FAILURE,
            success ? successMessage : failureMessage
        ));
    }
}
