package com.referexpert.referexpert.beans;

public class Coordinates {

    private Double longitude;

    private Double lattitude;

    public Coordinates(Double longitude, Double lattitude) {
        super();
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public Coordinates() {
        super();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    @Override
    public String toString() {
        return "Coordinates [longitude=" + longitude + ", lattitude=" + lattitude + "]";
    }
}
