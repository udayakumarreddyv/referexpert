package com.referexpert.referexpert.beans;

import java.util.List;

public class UserSpeciality {

    List<String> specialities;

    public List<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<String> specialities) {
        this.specialities = specialities;
    }

    @Override
    public String toString() {
        return "UserSpeciality [specialities=" + specialities + "]";
    }
}
