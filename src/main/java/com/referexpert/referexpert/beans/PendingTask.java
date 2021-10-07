package com.referexpert.referexpert.beans;

public class PendingTask {

	private String currentAppointment;

	private String pendingAppointment;

	private String pendingAvailabilityResponse;

	private String pendingAvailabilityRequest;

	public String getCurrentAppointment() {
		return currentAppointment;
	}

	public void setCurrentAppointment(String currentAppointment) {
		this.currentAppointment = currentAppointment;
	}

	public String getPendingAppointment() {
		return pendingAppointment;
	}

	public void setPendingAppointment(String pendingAppointment) {
		this.pendingAppointment = pendingAppointment;
	}

	public String getPendingAvailabilityResponse() {
		return pendingAvailabilityResponse;
	}

	public void setPendingAvailabilityResponse(String pendingAvailabilityResponse) {
		this.pendingAvailabilityResponse = pendingAvailabilityResponse;
	}

	public String getPendingAvailabilityRequest() {
		return pendingAvailabilityRequest;
	}

	public void setPendingAvailabilityRequest(String pendingAvailabilityRequest) {
		this.pendingAvailabilityRequest = pendingAvailabilityRequest;
	}

	@Override
	public String toString() {
		return "PendingTask [currentAppointment=" + currentAppointment + ", pendingAppointment=" + pendingAppointment
				+ ", pendingAvailabilityResponse=" + pendingAvailabilityResponse + ", pendingAvailabilityRequest="
				+ pendingAvailabilityRequest + "]";
	}

}
