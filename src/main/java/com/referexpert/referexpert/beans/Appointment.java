package com.referexpert.referexpert.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {

    private String appointmentId;

    private String appointmentFrom;

    private String appointmentTo;

    private String dateAndTimeString;

    private String isAccepted;

    private String isServed;

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

    @Override
    public String toString() {
        return "Appointment [appointmentId=" + appointmentId + ", appointmentFrom=" + appointmentFrom
                + ", appointmentTo=" + appointmentTo + ", dateAndTimeString=" + dateAndTimeString + ", isAccepted="
                + isAccepted + ", isServed=" + isServed + "]";
    }
}
