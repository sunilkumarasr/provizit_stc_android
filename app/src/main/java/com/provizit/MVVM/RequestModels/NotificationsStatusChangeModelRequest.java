package com.provizit.MVVM.RequestModels;

public class NotificationsStatusChangeModelRequest {

    String comp_id;
    String id;

    public NotificationsStatusChangeModelRequest(String comp_id, String id) {
        this.comp_id = comp_id;
        this.id = id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
