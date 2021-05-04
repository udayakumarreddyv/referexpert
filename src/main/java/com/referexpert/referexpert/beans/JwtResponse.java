package com.referexpert.referexpert.beans;

public class JwtResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private String refreshToken;

	public JwtResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public JwtResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "JwtResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", refreshToken=" + refreshToken
				+ "]";
	}

}
