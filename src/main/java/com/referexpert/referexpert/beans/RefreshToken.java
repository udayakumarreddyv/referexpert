package com.referexpert.referexpert.beans;

import java.sql.Timestamp;

public class RefreshToken {

	private String refreshTokenId;

	private String userId;

	private String token;

	private Timestamp expiryDate;

	public RefreshToken() {
	}

	public String getRefreshTokenId() {
		return refreshTokenId;
	}

	public void setRefreshTokenId(String refreshTokenId) {
		this.refreshTokenId = refreshTokenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		return "RefreshToken [refreshTokenId=" + refreshTokenId + ", userId=" + userId + ", token=" + token
				+ ", expiryDate=" + expiryDate + "]";
	}

}
