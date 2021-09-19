package com.referexpert.referexpert.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.referexpert.referexpert.beans.RefreshToken;
import com.referexpert.referexpert.repository.MySQLRepository;

@Service
public class RefreshTokenService {
	
	private final static Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
	
	@Value("${jwt.refresh.expiration.ms}")
	private int jwtRefreshTokenValidity;

	@Autowired
	private MySQLRepository mysqlRepository;

	public RefreshToken findByToken(String token) {
		logger.info("RefreshTokenService :: findByToken :: " + token);
		return mysqlRepository.findByRequestToken(token);
	}

	public RefreshToken createRefreshToken(String userId) {
		logger.info("RefreshTokenService :: createRefreshToken :: " + userId);
		RefreshToken refreshToken = new RefreshToken();
		
		long currentTimeStamp = System.currentTimeMillis();
        Timestamp original = new Timestamp(currentTimeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(original.getTime());
        cal.add(Calendar.MILLISECOND, jwtRefreshTokenValidity);
        Timestamp expiryDate = new Timestamp(cal.getTime().getTime());
        
        String refreshTokenId = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString()+"."+UUID.randomUUID().toString();
		refreshToken.setRefreshTokenId(refreshTokenId);
		refreshToken.setUserId(userId);
		refreshToken.setExpiryDate(expiryDate);
		refreshToken.setToken(token);

        try {
            mysqlRepository.insertRefreshToken(refreshToken);
        } catch (Exception e) {
        	exceptionBlock(e, "Exception while inserting data into refresh_token");
            return new RefreshToken();
        }
		return mysqlRepository.findByRequestTokenId(refreshTokenId);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		logger.info("RefreshTokenService :: verifyExpiration :: " + token.toString());
		if (token.getExpiryDate().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
			try {
				mysqlRepository.deleteRefreshToken(token.getToken());
				logger.info("Refresh token was expired. Please make a new signin request");
				return null;
			} catch (Exception e) {
				exceptionBlock(e, "Exception while deleting data from refresh_token");
			}
		}

		return token;
	}
	
	public int deleteByToken(String token) {
		logger.info("RefreshTokenService :: deleteByToken :: " + token);
		try {
			logger.info("Refresh token deleted on logout. Please make a new signin request");
			return mysqlRepository.deleteRefreshToken(token);
		} catch (Exception e) {
			exceptionBlock(e, "Exception while deleting data from refresh_token");
		}
		return 0;
	}
	
	public int cleanupRefreshToken() {
		logger.info("RefreshTokenService :: cleanupRefreshToken");
		try {
			logger.info("Expired refresh tokens are deleted from database");
			return mysqlRepository.cleanupRefreshToken();
		} catch (Exception e) {
			exceptionBlock(e, "Exception while deleting data from refresh_token");
		}
		return 0;
	}

	public int deleteByUserId(String userId) {
		logger.info("RefreshTokenService :: deleteByUserId :: " + userId);
		try {
			return mysqlRepository.deleteRefreshTokenByUser(userId);
		} catch (Exception e) {
			exceptionBlock(e, "Exception while deleting data from refresh_token");
		}
		return 0;
	}
	
	private void exceptionBlock(Exception e, String message) {
		logger.error(message);
		logger.error("Exception details :: " + e);
		e.printStackTrace();
	}
}
