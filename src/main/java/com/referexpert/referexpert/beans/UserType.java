package com.referexpert.referexpert.beans;

public class UserType {

    private String userType;

    private UserSpeciality userSpeciality;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserSpeciality getUserSpeciality() {
        return userSpeciality;
    }

    public void setUserSpeciality(UserSpeciality userSpeciality) {
        this.userSpeciality = userSpeciality;
    }

    @Override
    public String toString() {
        return "UserType [userType=" + userType + ", userSpeciality=" + userSpeciality + "]";
    }
}
