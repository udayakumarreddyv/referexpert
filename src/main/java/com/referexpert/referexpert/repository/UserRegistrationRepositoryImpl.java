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

import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.constant.Constants;
import com.referexpert.referexpert.constant.QueryConstants;
import com.referexpert.referexpert.util.GeoUtils;

@Repository
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<UserRegistration> userRowMapper = new RowMapper<UserRegistration>() {
        @Override
        public UserRegistration mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRegistration user = new UserRegistration();
            int i = 0;
            user.setUserId(rs.getString(++i));
            user.setFirstName(rs.getString(++i));
            user.setLastName(rs.getString(++i));
            user.setEmail(rs.getString(++i));
            user.setUserType(rs.getString(++i));
            user.setUserSpeciality(rs.getString(++i));
            user.setAddress(rs.getString(++i));
            user.setCity(rs.getString(++i));
            user.setState(rs.getString(++i));
            user.setZip(rs.getString(++i));
            user.setPhone(rs.getString(++i));
            user.setFax(rs.getString(++i));
            user.setIsActive(rs.getString(++i));
            user.setPassword(rs.getString(++i));
            user.setLattitude(rs.getDouble(++i));
            user.setLongitude(rs.getDouble(++i));
            user.setService(getValue(rs.getString(++i)));
            user.setInsurance(getValue(rs.getString(++i)));
            user.setOfficeName(getValue(rs.getString(++i)));
            return user;
        }
    };

    @Override
    public Optional<UserRegistration> findById(String id) {
        logger.debug("Finding user by ID: {}", id);
        try {
            List<UserRegistration> results = jdbcTemplate.query(
                QueryConstants.SELECT_ACTIVE_USER + " user_id = ?",
                new Object[]{id},
                userRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding user by ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserRegistration> findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        try {
            List<UserRegistration> results = jdbcTemplate.query(
                QueryConstants.SELECT_ACTIVE_USER + " email = ?",
                new Object[]{email},
                userRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding user by email: {}", email, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserRegistration> findAll() {
        logger.debug("Finding all users");
        try {
            return jdbcTemplate.query(QueryConstants.SELECT_ACTIVE_USERS, userRowMapper);
        } catch (Exception e) {
            logger.error("Error finding all users", e);
            return new ArrayList<>();
        }
    }

    @Override
    public UserRegistration save(UserRegistration user) {
        logger.debug("Saving user: {}", user.getEmail());
        try {
            if (user.getUserId() == null) {
                // Insert new user
                jdbcTemplate.update(
                    QueryConstants.INSERT_USER_PROFILE,
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getUserType(),
                    user.getUserSpeciality(),
                    user.getAddress(),
                    user.getCity(),
                    user.getState(),
                    user.getZip(),
                    user.getPhone(),
                    user.getFax(),
                    Constants.PENDING,
                    user.getLattitude(),
                    user.getLongitude(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()),
                    user.getService(),
                    user.getInsurance(),
                    user.getOfficeName()
                );
            } else {
                // Update existing user
                updateProfile(user);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error saving user: {}", user.getEmail(), e);
            throw new RuntimeException("Error saving user", e);
        }
    }

    @Override
    public void delete(UserRegistration user) {
        deleteById(user.getUserId());
    }

    @Override
    public void deleteById(String id) {
        logger.debug("Deleting user by ID: {}", id);
        try {
            jdbcTemplate.update(
                "UPDATE user_profile SET is_active = ? WHERE user_id = ?",
                Constants.INACTIVE,
                id
            );
        } catch (Exception e) {
            logger.error("Error deleting user: {}", id, e);
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public List<UserRegistration> findActiveUsersByType(String userType) {
        logger.debug("Finding active users by type: {}", userType);
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_ACTIVE_USERS + " user_type = ? AND is_active = 'Y'",
                new Object[]{userType},
                userRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding active users by type: {}", userType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserRegistration> findActiveUsersWithinDistance(double latitude, double longitude, int distance) {
        logger.debug("Finding active users within distance: lat={}, lon={}, distance={}", latitude, longitude, distance);
        try {
            List<UserRegistration> allActiveUsers = jdbcTemplate.query(
                QueryConstants.SELECT_ACTIVE_USERS + " is_active = 'Y'",
                userRowMapper
            );

            return allActiveUsers.stream()
                .filter(user -> GeoUtils.isDistanceInRange(
                    latitude, longitude, 
                    user.getLattitude(), user.getLongitude(), 
                    distance))
                .peek(user -> user.setDistance(
                    GeoUtils.getDistnace(
                        latitude, longitude,
                        user.getLattitude(), user.getLongitude(),
                        distance)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (Exception e) {
            logger.error("Error finding users within distance", e);
            return new ArrayList<>();
        }
    }

    @Override
    public int updatePassword(String email, String encodedPassword) {
        logger.debug("Updating password for user: {}", email);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_USER_PASSWORD,
                encodedPassword,
                new Timestamp(System.currentTimeMillis()),
                email
            );
        } catch (Exception e) {
            logger.error("Error updating password for user: {}", email, e);
            return 0;
        }
    }

    @Override
    public int updateActivationStatus(String email, String status) {
        logger.debug("Updating activation status for user: {} to {}", email, status);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_USER_ACTIVATION,
                status,
                new Timestamp(System.currentTimeMillis()),
                email
            );
        } catch (Exception e) {
            logger.error("Error updating activation status for user: {}", email, e);
            return 0;
        }
    }

    @Override
    public int updateProfile(UserRegistration user) {
        logger.debug("Updating profile for user: {}", user.getEmail());
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_USER_PROFILE,
                user.getFirstName(),
                user.getLastName(),
                user.getUserType(),
                user.getUserSpeciality(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getZip(),
                user.getPhone(),
                user.getFax(),
                user.getLattitude(),
                user.getLongitude(),
                new Timestamp(System.currentTimeMillis()),
                user.getService(),
                user.getInsurance(),
                user.getOfficeName(),
                user.getEmail()
            );
        } catch (Exception e) {
            logger.error("Error updating profile for user: {}", user.getEmail(), e);
            return 0;
        }
    }

    @Override
    public Optional<UserRegistration> findActiveUserByEmail(String email) {
        logger.debug("Finding active user by email: {}", email);
        try {
            List<UserRegistration> results = jdbcTemplate.query(
                QueryConstants.SELECT_ACTIVE_USER + " email = ? AND is_active = 'Y'",
                new Object[]{email},
                userRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding active user by email: {}", email, e);
            return Optional.empty();
        }
    }

    private String getValue(String str) {
        return str != null ? str : "";
    }
}
