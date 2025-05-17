package com.referexpert.referexpert.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.repository.AppointmentRepository;
import com.referexpert.referexpert.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAllAppointments() {
        logger.debug("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(String appointmentId) {
        logger.debug("Fetching appointment by ID: {}", appointmentId);
        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public List<Appointment> getAppointmentsByFrom(String email) {
        logger.debug("Fetching appointments from email: {}", email);
        return appointmentRepository.findByAppointmentFrom(email);
    }

    @Override
    public List<Appointment> getAppointmentsByTo(String email) {
        logger.debug("Fetching appointments to email: {}", email);
        return appointmentRepository.findByAppointmentTo(email);
    }

    @Override
    public List<Appointment> getPendingAppointments() {
        logger.debug("Fetching pending appointments");
        return appointmentRepository.findPendingAppointments();
    }

    @Override
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        logger.debug("Creating new appointment: {}", appointment);
        if (appointment.getAppointmentId() == null) {
            appointment.setAppointmentId(UUID.randomUUID().toString());
        }
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public boolean updateAppointmentStatus(String appointmentId, String status) {
        logger.debug("Updating appointment status: {} to {}", appointmentId, status);
        return appointmentRepository.updateAppointmentStatus(appointmentId, status) > 0;
    }

    @Override
    @Transactional
    public boolean updateAppointmentResponse(String appointmentId, String response) {
        logger.debug("Updating appointment response: {} to {}", appointmentId, response);
        return appointmentRepository.updateAppointmentResponse(appointmentId, response) > 0;
    }

    @Override
    @Transactional
    public boolean updateServedStatus(String appointmentId, String status) {
        logger.debug("Updating served status: {} to {}", appointmentId, status);
        return appointmentRepository.updateServedStatus(appointmentId, status) > 0;
    }

    @Override
    @Transactional
    public boolean deleteAppointment(String appointmentId) {
        logger.debug("Deleting appointment: {}", appointmentId);
        try {
            appointmentRepository.deleteById(appointmentId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting appointment: {}", appointmentId, e);
            return false;
        }
    }

    @Override
    public boolean hasPendingTasks(String type, String isAccepted, String isReferral, String email) {
        logger.debug("Checking pending tasks for email: {}", email);
        return appointmentRepository.hasPendingTasks(type, isAccepted, isReferral, email);
    }
}
