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

import com.referexpert.referexpert.beans.UserReferral;
import com.referexpert.referexpert.constant.QueryConstants;

@Repository
public class UserReferralRepositoryImpl implements UserReferralRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserReferralRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<UserReferral> userReferralRowMapper = new RowMapper<UserReferral>() {
        @Override
        public UserReferral mapRow(ResultSet rs, int rowNum) throws SQLException {
            int i = 0;
            UserReferral userReferral = new UserReferral();
            userReferral.setUserReferralId(rs.getString(++i));
            userReferral.setUserEmail(rs.getString(++i));
            userReferral.setDocEmail(rs.getString(++i));
            userReferral.setIsRegistered(rs.getString(++i));
            return userReferral;
        }
    };

    @Override
    public Optional<UserReferral> findById(String id) {
        return findByReferralId(id);
    }

    @Override
    public Optional<UserReferral> findByReferralId(String referralId) {
        logger.debug("Finding user referral by ID: {}", referralId);
        try {
            List<UserReferral> results = jdbcTemplate.query(
                QueryConstants.SELECT_USER_REFERRAL + " user_referral_id = ?",
                new Object[]{referralId},
                userReferralRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding user referral by ID: {}", referralId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserReferral> findAll() {
        logger.debug("Finding all user referrals");
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_USER_REFERRAL,
                userReferralRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding all user referrals", e);
            return new ArrayList<>();
        }
    }

    @Override
    public UserReferral save(UserReferral userReferral) {
        logger.debug("Saving user referral: {}", userReferral);
        try {
            jdbcTemplate.update(
                QueryConstants.INSERT_USER_REFERRAL,
                userReferral.getUserReferralId(),
                userReferral.getUserEmail(),
                userReferral.getDocEmail(),
                userReferral.getIsRegistered(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
            );
            return userReferral;
        } catch (Exception e) {
            logger.error("Error saving user referral", e);
            throw new RuntimeException("Error saving user referral", e);
        }
    }

    @Override
    public Optional<UserReferral> findByUserEmail(String email) {
        logger.debug("Finding user referral by user email: {}", email);
        try {
            List<UserReferral> results = jdbcTemplate.query(
                QueryConstants.SELECT_USER_REFERRAL + " user_email = ?",
                new Object[]{email},
                userReferralRowMapper
            );
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            logger.error("Error finding user referral by user email: {}", email, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserReferral> findByDoctorEmail(String docEmail) {
        logger.debug("Finding user referrals by doctor email: {}", docEmail);
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_USER_REFERRAL + " doc_email = ?",
                new Object[]{docEmail},
                userReferralRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding user referrals by doctor email: {}", docEmail, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserReferral> findNonRegisteredUsers() {
        logger.debug("Finding non-registered users");
        try {
            return jdbcTemplate.query(
                QueryConstants.SELECT_USER_REFERRAL_NR,
                userReferralRowMapper
            );
        } catch (Exception e) {
            logger.error("Error finding non-registered users", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean existsByReferralIdAndDoctorEmail(String referralId, String docEmail) {
        logger.debug("Checking existence of referral ID: {} and doctor email: {}", referralId, docEmail);
        try {
            List<String> results = jdbcTemplate.queryForList(
                QueryConstants.SELECT_USER_REFERRAL + " user_referral_id = ? AND doc_email = ?",
                String.class,
                referralId,
                docEmail
            );
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking referral existence", e);
            return false;
        }
    }

    @Override
    public int updateRegistrationStatus(String email, String status) {
        logger.debug("Updating registration status for email: {} to {}", email, status);
        try {
            return jdbcTemplate.update(
                QueryConstants.UPDATE_USER_REFERRAL,
                status,
                new Timestamp(System.currentTimeMillis()),
                email
            );
        } catch (Exception e) {
            logger.error("Error updating registration status", e);
            return 0;
        }
    }

    @Override
    public void delete(UserReferral userReferral) {
        deleteById(userReferral.getUserReferralId());
    }

    @Override
    public void deleteById(String id) {
        logger.debug("Deleting user referral by ID: {}", id);
        try {
            jdbcTemplate.update(
                "DELETE FROM user_referral WHERE user_referral_id = ?",
                id
            );
        } catch (Exception e) {
            logger.error("Error deleting user referral: {}", id, e);
            throw new RuntimeException("Error deleting user referral", e);
        }
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }
}
