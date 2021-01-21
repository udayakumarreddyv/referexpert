package com.referexpert.referexpert.beans;

import java.util.UUID;

public class ConfirmationToken {

    private String tokenid;

    private String confirmationToken;

    private String userId;

    public ConfirmationToken() {
    }

    public ConfirmationToken(UserRegistration userRegistration) {
        tokenid = UUID.randomUUID().toString();
        confirmationToken = UUID.randomUUID().toString();
        userId = userRegistration.getUserId();
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ConfirmationToken [tokenid=" + tokenid + ", confirmationToken=" + confirmationToken + ", userId="
                + userId + "]";
    }
}