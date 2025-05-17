package com.referexpert.referexpert.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.constant.QueryConstants;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentRepositoryImpl.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Appointment> appointmentRowMapper = new RowMapper<Appointment>() {
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getString(++i));
            appointment.setAppointmentFrom(rs.getString(++i));
            appointment.setFromFirstName(rs.getString(++i));
            appointment.setFromLastName(rs.getString(++i));
            appointment.setAppointmentTo(rs.getString(++i));
            appointment.setToFirstName(rs.getString(++i));
            appointment.setToLastName(rs.getString(++i));
            appointment.setDateAndTimeString(getValue(rs.getString(++i)));
            appointment.setIsAccepted(getValue(rs.getString(++i)));
            appointment.setIsServed(getValue(rs.getString(++i)));
            appointment.setIsAvailabilityCheck(getValue(rs.getString(++i)));
            appointment.setSubject(getValue(rs.getString(++i)));
            appointment.setReason(getValue(rs.getString(++i)));
            appointment.setFromDoctorOffice(getValue(rs.getString(++i)));
            appointment.setToDoctorOffice(getValue(rs.getString(++i)));
            appointment.setPatientName(getValue(rs.getString(++i)));
            appointment.setPatientEmail(getValue(rs.getString(++i)));
            appointment.setPatientPhone(getValue(rs.getString(++i)));
            return appointment;
        }
    };

    @Override
    public Optional<Appointment> findById(String id) {
        return findByAppointmentId(id);
    }

    @Override
    public List<Appointment> findAll() {
        logger.debug("Finding all appointments");
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_APPOINTMENT + QueryConstants.ORDERBY_APPOINT_TIMESTAMP,
                appointmentRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding all appointments", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Appointment> findByAppointmentId(String appointmentId) {
        logger.debug("Finding appointment by ID: {}", appointmentId);
        try {
            List<Appointment> results = jdbcTemplate.query(
                QueryConstants.SELECT_APPOINTMENT + " appointment_id = ?",
                new Object[]{appointmentId},
                appointmentRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding appointment by ID: {}", appointmentId, e);
            return Optional.empty();
        }
    }

    @Override
    public Appointment save(Appointment appointment) {
        logger.debug("Saving appointment: {}", appointment);
        try {
            if (appointment.getAppointmentId() == null) {
                // Insert new appointment
                jdbcTemplate.update(
                    QueryConstants.INSERT_APPOINTMENT,
                    appointment.getAppointmentId(),
                    appointment.getAppointmentFrom(),
                    appointment.getAppointmentTo(),
                    appointment.getDateAndTimeString(),
                    appointment.getSubject(),
                    appointment.getReason(),
                    Constants.PENDING,
                    Constants.INACTIVE,
                    appointment.getIsAvailabilityCheck(),
                    appointment.getPatientName(),
                    appointment.getPatientEmail(),
                    appointment.getPatientPhone(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis())
                );
            }
            return appointment;
        } catch (Exception e) {
            logger.error("Error saving appointment", e);
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public List<Appointment> findByAppointmentFrom(String email) {
        logger.debug("Finding appointments by from email: {}", email);
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_APPOINTMENT + " appointment_from = ?",
                new Object[]{email},
                appointmentRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding appointments by from email: {}", email, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> findByAppointmentTo(String email) {
        logger.debug("Finding appointments by to email: {}", email);
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_APPOINTMENT + " appointment_to = ?",
                new Object[]{email},
                appointmentRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding appointments by to email: {}", email, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Appointment> findPendingAppointments() {
        logger.debug("Finding pending appointments");
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_APPOINTMENT + " is_accepted = ?",
                new Object[]{Constants.PENDING},
                appointmentRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding pending appointments", e);
            return new ArrayList<>();
        }
    }

    @Override
    public int updateAppointmentStatus(String appointmentId, String status) {
        logger.debug("Updating appointment status: {} to {}", appointmentId, status);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_APPOINTMENT_STATUS,
                status,
                new Timestamp(System.currentTimeMillis()),
                appointmentId
            );
        } catch (Exception e) {
            logger.error("Error updating appointment status", e);
            return 0;
        }
    }

    @Override
    public int updateAppointmentResponse(String appointmentId, String response) {
        logger.debug("Updating appointment response: {} to {}", appointmentId, response);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_APPOINTMENT_RESPONSE,
                response,
                Constants.ACTIVE,
                new Timestamp(System.currentTimeMillis()),
                appointmentId
            );
        } catch (Exception e) {
            logger.error("Error updating appointment response", e);
            return 0;
        }
    }

    @Override
    public int updateServedStatus(String appointmentId, String status) {
        logger.debug("Updating appointment served status: {} to {}", appointmentId, status);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_SERVED_STATUS,
                status,
                new Timestamp(System.currentTimeMillis()),
                appointmentId
            );
        } catch (Exception e) {
            logger.error("Error updating appointment served status", e);
            return 0;
        }
    }

    @Override
    public void delete(Appointment appointment) {
        deleteById(appointment.getAppointmentId());
    }

    @Override
    public void deleteById(String id) {
        logger.debug("Deleting appointment by ID: {}", id);
        try {
            jdbcTemplate.update(
                "UPDATE appointment SET is_active = ? WHERE appointment_id = ?",
                Constants.INACTIVE,
                id
            );
        } catch (Exception e) {
            logger.error("Error deleting appointment: {}", id, e);
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean hasPendingTasks(String type, String isAccepted, String isReferral, String email) {
        logger.debug("Checking pending tasks for email: {}", email);
        try {
            String query = QueryConstants.PENDING_TASKS_WAITING_REQUEST;
            if (Constants.RESPONSE.equals(type)) {
                if (Constants.ACTIVE.equals(isAccepted)) {
                    query = QueryConstants.PENDING_TASKS_WAITING_RESPONSE + " and a.is_served = ?";
                    List<String> results = jdbcTemplate.queryForList(
                        query,
                        String.class,
                        isAccepted,
                        isReferral,
                        email,
                        Constants.INACTIVE
                    );
                    return !results.isEmpty();
                } else {
                    List<String> results = jdbcTemplate.queryForList(
                        QueryConstants.PENDING_TASKS_WAITING_RESPONSE,
                        String.class,
                        isAccepted,
                        isReferral,
                        email
                    );
                    return !results.isEmpty();
                }
            } else {
                query += " and a.is_served is null";
                List<String> results = jdbcTemplate.queryForList(
                    query,
                    String.class,
                    isAccepted,
                    isReferral,
                    email
                );
                return !results.isEmpty();
            }
        } catch (Exception e) {
            logger.error("Error checking pending tasks", e);
            return false;
        }
    }

    private String getValue(String str) {
        return str != null ? str : "";
    }
}
