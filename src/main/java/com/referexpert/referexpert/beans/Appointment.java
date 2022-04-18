package com.referexpert.referexpert.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {

	private String appointmentId;

	private String appointmentFrom;

	private String fromFirstName;

	private String fromLastName;

	private String appointmentTo;

	private String toFirstName;

	private String toLastName;

	private String dateAndTimeString;

	private String subject;

	private String reason;

	private String isAccepted;

	private String isServed;

	private String isAvailabilityCheck;
	
	private String patientName;
	
	private String patientEmail;
	
	private String patientPhone;
	
	private String fromDoctorOffice;
	
	private String toDoctorOffice;
	
	private String token;

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getAppointmentFrom() {
		return appointmentFrom;
	}

	public void setAppointmentFrom(String appointmentFrom) {
		this.appointmentFrom = appointmentFrom;
	}

	public String getAppointmentTo() {
		return appointmentTo;
	}

	public void setAppointmentTo(String appointmentTo) {
		this.appointmentTo = appointmentTo;
	}

	public String getDateAndTimeString() {
		return dateAndTimeString;
	}

	public void setDateAndTimeString(String dateAndTimeString) {
		this.dateAndTimeString = dateAndTimeString;
	}

	public String getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getIsServed() {
		return isServed;
	}

	public void setIsServed(String isServed) {
		this.isServed = isServed;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFromFirstName() {
		return fromFirstName;
	}

	public void setFromFirstName(String fromFirstName) {
		this.fromFirstName = fromFirstName;
	}

	public String getFromLastName() {
		return fromLastName;
	}

	public void setFromLastName(String fromLastName) {
		this.fromLastName = fromLastName;
	}

	public String getToFirstName() {
		return toFirstName;
	}

	public void setToFirstName(String toFirstName) {
		this.toFirstName = toFirstName;
	}

	public String getToLastName() {
		return toLastName;
	}

	public void setToLastName(String toLastName) {
		this.toLastName = toLastName;
	}

	public String getIsAvailabilityCheck() {
		return isAvailabilityCheck;
	}

	public void setIsAvailabilityCheck(String isAvailabilityCheck) {
		this.isAvailabilityCheck = isAvailabilityCheck;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientEmail() {
		return patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public String getFromDoctorOffice() {
		return fromDoctorOffice;
	}

	public void setFromDoctorOffice(String fromDoctorOffice) {
		this.fromDoctorOffice = fromDoctorOffice;
	}

	public String getToDoctorOffice() {
		return toDoctorOffice;
	}

	public void setToDoctorOffice(String toDoctorOffice) {
		this.toDoctorOffice = toDoctorOffice;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Appointment [appointmentId=" + appointmentId + ", appointmentFrom=" + appointmentFrom
				+ ", fromFirstName=" + fromFirstName + ", fromLastName=" + fromLastName + ", appointmentTo="
				+ appointmentTo + ", toFirstName=" + toFirstName + ", toLastName=" + toLastName + ", dateAndTimeString="
				+ dateAndTimeString + ", subject=" + subject + ", reason=" + reason + ", isAccepted=" + isAccepted
				+ ", isServed=" + isServed + ", isAvailabilityCheck=" + isAvailabilityCheck + ", patientName="
				+ patientName + ", patientEmail=" + patientEmail + ", patientPhone=" + patientPhone
				+ ", fromDoctorOffice=" + fromDoctorOffice + ", toDoctorOffice=" + toDoctorOffice + ", token=" + token
				+ "]";
	}

}
