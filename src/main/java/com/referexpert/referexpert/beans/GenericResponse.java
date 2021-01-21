package com.referexpert.referexpert.beans;

public class GenericResponse {

    private String message;

    public GenericResponse(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GenericResponse [message=" + message + "]";
    }
}
