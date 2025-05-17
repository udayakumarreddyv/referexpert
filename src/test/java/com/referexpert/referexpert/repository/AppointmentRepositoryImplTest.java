package com.referexpert.referexpert.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.referexpert.referexpert.beans.Appointment;
import com.referexpert.referexpert.constant.Constants;

@ExtendWith(MockitoExtension.class)
public class AppointmentRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AppointmentRepositoryImpl repository;

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
    void findById_WhenAppointmentExists_ReturnsAppointment() {
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RowMapper.class)))
            .thenReturn(Arrays.asList(testAppointment));

        Optional<Appointment> result = repository.findById("test-id");

        assertTrue(result.isPresent());
        assertEquals("test-id", result.get().getAppointmentId());
    }

    @Test
    void findById_WhenAppointmentDoesNotExist_ReturnsEmpty() {
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RowMapper.class)))
            .thenReturn(Arrays.asList());

        Optional<Appointment> result = repository.findById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ReturnsAllAppointments() {
        when(jdbcTemplate.query(any(String.class), any(RowMapper.class)))
            .thenReturn(Arrays.asList(testAppointment));

        List<Appointment> results = repository.findAll();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("test-id", results.get(0).getAppointmentId());
    }

    @Test
    void save_NewAppointment_Success() {
        when(jdbcTemplate.update(
            any(String.class),
            any(Object[].class)))
            .thenReturn(1);

        Appointment result = repository.save(testAppointment);

        assertNotNull(result);
        assertEquals("test-id", result.getAppointmentId());
    }

    @Test
    void findByAppointmentFrom_ReturnsMatchingAppointments() {
        when(jdbcTemplate.query(
            any(String.class),
            any(Object[].class),
            any(RowMapper.class)))
            .thenReturn(Arrays.asList(testAppointment));

        List<Appointment> results = repository.findByAppointmentFrom("from@test.com");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("from@test.com", results.get(0).getAppointmentFrom());
    }

    @Test
    void updateAppointmentStatus_Success() {
        when(jdbcTemplate.update(
            any(String.class),
            any(String.class),
            any(Timestamp.class),
            any(String.class)))
            .thenReturn(1);

        int result = repository.updateAppointmentStatus("test-id", Constants.ACTIVE);

        assertEquals(1, result);
    }

    @Test
    void hasPendingTasks_WhenTasksExist_ReturnsTrue() {
        when(jdbcTemplate.queryForList(
            any(String.class),
            eq(String.class),
            any(Object[].class)))
            .thenReturn(Arrays.asList("test-id"));

        boolean result = repository.hasPendingTasks(
            Constants.RESPONSE,
            Constants.ACTIVE,
            Constants.INACTIVE,
            "test@test.com"
        );

        assertTrue(result);
    }

    @Test
    void hasPendingTasks_WhenNoTasks_ReturnsFalse() {
        when(jdbcTemplate.queryForList(
            any(String.class),
            eq(String.class),
            any(Object[].class)))
            .thenReturn(Arrays.asList());

        boolean result = repository.hasPendingTasks(
            Constants.RESPONSE,
            Constants.ACTIVE,
            Constants.INACTIVE,
            "test@test.com"
        );

        assertFalse(result);
    }
}
