package com.provizit.Services.Notifications;

import com.provizit.Utilities.NotificationObjects;

import java.io.Serializable;

public class NotificationsComon implements Serializable {

        String supertype;
        String status;
        String comp_id;
        String mid;
        String email;
        String nid;
        String ntype;

        NotificationObjects meetingData;
        NotificationObjects employeeData;
        NotificationObjects _id;
        NotificationObjects created_time;


    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNtype() {
        return ntype;
    }

    public void setNtype(String ntype) {
        this.ntype = ntype;
    }

    public NotificationObjects getMeetingData() {
        return meetingData;
    }

    public void setMeetingData(NotificationObjects meetingData) {
        this.meetingData = meetingData;
    }

    public NotificationObjects getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(NotificationObjects employeeData) {
        this.employeeData = employeeData;
    }

    public NotificationObjects get_id() {
        return _id;
    }

    public void set_id(NotificationObjects _id) {
        this._id = _id;
    }

    public NotificationObjects getCreated_time() {
        return created_time;
    }

    public void setCreated_time(NotificationObjects created_time) {
        this.created_time = created_time;
    }
}
