package com.referexpert.referexpert.beans;

public class PendingTask {

	private String pendingAppointment;

	private String pendingAvailability;

	public String getPendingAppointment() {
		return pendingAppointment;
	}

	public void setPendingAppointment(String pendingAppointment) {
		this.pendingAppointment = pendingAppointment;
	}

	public String getPendingAvailability() {
		return pendingAvailability;
	}

	public void setPendingAvailability(String pendingAvailability) {
		this.pendingAvailability = pendingAvailability;
	}

	@Override
	public String toString() {
		return "PendingTask [pendingAppointment=" + pendingAppointment + ", pendingAvailability=" + pendingAvailability
				+ "]";
	}

}
