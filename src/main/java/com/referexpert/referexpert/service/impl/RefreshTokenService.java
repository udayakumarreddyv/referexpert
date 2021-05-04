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
import com.referexpert.referexpert.exception.TokenRefreshException;
import com.referexpert.referexpert.repository.MySQLRepository;

@Service
public class RefreshTokenService {
	
	private final static Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
	
	@Value("${jwt.refresh.expiration.ms}")
	private int jwtRefreshTokenValidity;

	@Autowired
	private MySQLRepository mysqlRepository;

	public RefreshToken findByToken(String token) {
		return mysqlRepository.findByRequestToken(token);
	}

	public RefreshToken createRefreshToken(String userId) {
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
            logger.error("Exception while inserting data into refresh_token");
            logger.error("Exception details :: " + e);
            return new RefreshToken();
        }
		return mysqlRepository.findByRequestTokenId(refreshTokenId);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
			try {
				mysqlRepository.deleteRefreshToken(token.getToken());
				logger.info("Refresh token was expired. Please make a new signin request");
				return null;
			} catch (Exception e) {
				logger.error("Exception while deleting data into refresh_token");
				e.printStackTrace();
			}
		}

		return token;
	}
	
	public int deleteByToken(String token) {
		try {
			logger.info("Refresh token deleted on logout. Please make a new signin request");
			return mysqlRepository.deleteRefreshToken(token);
		} catch (Exception e) {
			logger.error("Exception while deleting data into refresh_token");
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteByUserId(String userId) {
		try {
			return mysqlRepository.deleteRefreshTokenByUser(userId);
		} catch (Exception e) {
			logger.error("Exception while deleting data into refresh_token");
			e.printStackTrace();
		}
		return 0;
	}
}
