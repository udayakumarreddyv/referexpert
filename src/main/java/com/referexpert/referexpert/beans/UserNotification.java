package com.referexpert.referexpert.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNotification {
	private String userEmail;

	private String notificationEmail;

	private String notificationMobile;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public String getNotificationMobile() {
		return notificationMobile;
	}

	public void setNotificationMobile(String notificationMobile) {
		this.notificationMobile = notificationMobile;
	}

	@Override
	public String toString() {
		return "UserNotification [userEmail=" + userEmail + ", notificationEmail=" + notificationEmail
				+ ", notificationMobile=" + notificationMobile + "]";
	}

}
