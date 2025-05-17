package com.referexpert.referexpert.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.constant.Constants;

@ExtendWith(MockitoExtension.class)
public class UserReferralRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserReferralRepositoryImpl repository;

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
    void findById_WhenReferralExists_ReturnsReferral() {
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RowMapper.class)))
            .thenReturn(Arrays.asList(testReferral));

        Optional<UserReferral> result = repository.findById("test-id");

        assertTrue(result.isPresent());
        assertEquals("test-id", result.get().getUserReferralId());
    }

    @Test
    void findById_WhenReferralDoesNotExist_ReturnsEmpty() {
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RowMapper.class)))
            .thenReturn(Arrays.asList());

        Optional<UserReferral> result = repository.findById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ReturnsAllReferrals() {
        when(jdbcTemplate.query(any(String.class), any(RowMapper.class)))
            .thenReturn(Arrays.asList(testReferral));

        List<UserReferral> results = repository.findAll();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("test-id", results.get(0).getUserReferralId());
    }

    @Test
    void save_NewReferral_Success() {
        when(jdbcTemplate.update(
            any(String.class),
            any(Object[].class)))
            .thenReturn(1);

        UserReferral result = repository.save(testReferral);

        assertNotNull(result);
        assertEquals("test-id", result.getUserReferralId());
    }

    @Test
    void findByUserEmail_ReturnsMatchingReferral() {
        when(jdbcTemplate.query(
            any(String.class),
            any(Object[].class),
            any(RowMapper.class)))
            .thenReturn(Arrays.asList(testReferral));

        Optional<UserReferral> result = repository.findByUserEmail("user@test.com");

        assertTrue(result.isPresent());
        assertEquals("user@test.com", result.get().getUserEmail());
    }

    @Test
    void findByDoctorEmail_ReturnsMatchingReferrals() {
        when(jdbcTemplate.query(
            any(String.class),
            any(Object[].class),
            any(RowMapper.class)))
            .thenReturn(Arrays.asList(testReferral));

        List<UserReferral> results = repository.findByDoctorEmail("doc@test.com");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("doc@test.com", results.get(0).getDocEmail());
    }

    @Test
    void findNonRegisteredUsers_ReturnsNonRegisteredReferrals() {
        when(jdbcTemplate.query(any(String.class), any(RowMapper.class)))
            .thenReturn(Arrays.asList(testReferral));

        List<UserReferral> results = repository.findNonRegisteredUsers();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(Constants.INACTIVE, results.get(0).getIsRegistered());
    }

    @Test
    void existsByReferralIdAndDoctorEmail_WhenExists_ReturnsTrue() {
        when(jdbcTemplate.queryForList(
            any(String.class),
            eq(String.class),
            any(Object[].class)))
            .thenReturn(Arrays.asList("test-id"));

        boolean result = repository.existsByReferralIdAndDoctorEmail("test-id", "doc@test.com");

        assertTrue(result);
    }

    @Test
    void updateRegistrationStatus_Success() {
        when(jdbcTemplate.update(
            any(String.class),
            any(String.class),
            any(Timestamp.class),
            any(String.class)))
            .thenReturn(1);

        int result = repository.updateRegistrationStatus("user@test.com", Constants.ACTIVE);

        assertEquals(1, result);
    }
}
