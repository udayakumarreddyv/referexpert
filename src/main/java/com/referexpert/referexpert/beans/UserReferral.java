package com.referexpert.referexpert.beans;

public class UserReferral {

	private String userReferralId;

	private String userEmail;

	private String docEmail;

	private String isRegistered;

	public String getUserReferralId() {
		return userReferralId;
	}

	public void setUserReferralId(String userReferralId) {
		this.userReferralId = userReferralId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getDocEmail() {
		return docEmail;
	}

	public void setDocEmail(String docEmail) {
		this.docEmail = docEmail;
	}

	public String getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(String isRegistered) {
		this.isRegistered = isRegistered;
	}

	@Override
	public String toString() {
		return "UserReferral [userReferralId=" + userReferralId + ", userEmail=" + userEmail + ", docEmail=" + docEmail
				+ ", isRegistered=" + isRegistered + "]";
	}

}
