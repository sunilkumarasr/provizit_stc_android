package com.provizit.MVVM.RequestModels;

public class ActionNotificationModelRequest {

    String email;
    String registration_ids;

    public ActionNotificationModelRequest(String email, String registration_ids) {
        this.email = email;
        this.registration_ids = registration_ids;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(String registration_ids) {
        this.registration_ids = registration_ids;
    }
}
