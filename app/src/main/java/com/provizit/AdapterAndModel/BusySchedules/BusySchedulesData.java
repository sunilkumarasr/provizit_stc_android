package com.provizit.AdapterAndModel.BusySchedules;

import com.provizit.Utilities.CommonObject;

import java.io.Serializable;

public class BusySchedulesData implements Serializable {

    private CommonObject _id;
    String from_date;
    String from_time;
    String to_date;
    String to_time;


    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }


    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }
}
