package com.referexpert.referexpert.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.constant.QueryConstants;

@ExtendWith(MockitoExtension.class)
class UserRegistrationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRegistrationRepositoryImpl repository;

    private UserRegistration testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserRegistration();
        testUser.setUserId("test-user-id");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setPassword("encoded-password");
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        when(jdbcTemplate.query(
            eq(QueryConstants.SELECT_ACTIVE_USER + " email = ?"), 
            any(Object[].class),
            any(RowMapper.class)))
        .thenReturn(Arrays.asList(testUser));

        Optional<UserRegistration> result = repository.findByEmail("test@example.com");
        
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        assertEquals("Test", result.get().getFirstName());
    }

    @Test
    void whenFindByEmailNotFound_thenReturnEmpty() {
        when(jdbcTemplate.query(
            eq(QueryConstants.SELECT_ACTIVE_USER + " email = ?"), 
            any(Object[].class),
            any(RowMapper.class)))
        .thenReturn(Arrays.asList());

        Optional<UserRegistration> result = repository.findByEmail("nonexistent@example.com");
        
        assertFalse(result.isPresent());
    }

    @Test
    void whenFindActiveUsersWithinDistance_thenReturnFilteredList() {
        UserRegistration user1 = new UserRegistration();
        user1.setLattitude(40.7128);
        user1.setLongitude(-74.0060);
        
        UserRegistration user2 = new UserRegistration();
        user2.setLattitude(40.7589);
        user2.setLongitude(-73.9851);

        when(jdbcTemplate.query(
            eq(QueryConstants.SELECT_ACTIVE_USERS + " is_active = 'Y'"),
            any(RowMapper.class)))
        .thenReturn(Arrays.asList(user1, user2));

        List<UserRegistration> result = repository.findActiveUsersWithinDistance(40.7128, -74.0060, 5);
        
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    void whenUpdatePassword_thenReturnSuccessCount() {
        when(jdbcTemplate.update(
            eq(QueryConstants.UPDATE_USER_PASSWORD),
            any(Object[].class)))
        .thenReturn(1);

        int result = repository.updatePassword("test@example.com", "new-encoded-password");
        
        assertEquals(1, result);
    }

    @Test
    void whenUpdateActivationStatus_thenReturnSuccessCount() {
        when(jdbcTemplate.update(
            eq(QueryConstants.UPDATE_USER_ACTIVATION),
            any(Object[].class)))
        .thenReturn(1);

        int result = repository.updateActivationStatus("test@example.com", "Y");
        
        assertEquals(1, result);
    }

    @Test
    void whenUpdateProfile_thenReturnSuccessCount() {
        when(jdbcTemplate.update(
            eq(QueryConstants.UPDATE_USER_PROFILE),
            any(Object[].class)))
        .thenReturn(1);

        int result = repository.updateProfile(testUser);
        
        assertEquals(1, result);
    }
}
