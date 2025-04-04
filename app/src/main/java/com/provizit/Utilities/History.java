package com.provizit.Utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class History  implements Serializable {
    private CommonObject _id;
    private CommonObject request;
    private CommonObject datetime;
    private Long checkin ;
    private Long hstatus ;
    private Long rstatus ;
    private Long status ;
    private String purpose;
    private String host;
    private Long date;
    private Long start;
    private Long end;
    private String type;
    private String vlocation;
    private String user_id;

    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public String getBadge() {
        return badge;
    }

    public void setRequest(CommonObject request) {
        this.request = request;
    }

    public void setCheckin(Long checkin) {
        this.checkin = checkin;
    }

    public void setHstatus(Long hstatus) {
        this.hstatus = hstatus;
    }

    public void setRstatus(Long rstatus) {
        this.rstatus = rstatus;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public void setLivepic(ArrayList<String> livepic) {
        this.livepic = livepic;
    }

    private String badge;
    private ArrayList<String> livepic;

    public Long getStatus() {
        return status;
    }

    public String getHost() {
        return host;
    }


    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getCheckin() {
        return checkin;
    }

    public Long getHstatus() {
        return hstatus;
    }

    public Long getRstatus() {
        return rstatus;
    }

    public CommonObject getRequest() {
        return request;
    }

    public CommonObject getDatetime() {
        return datetime;
    }

    public void setDatetime(CommonObject datetime) {
        this.datetime = datetime;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVlocation() {
        return vlocation;
    }

    public void setVlocation(String vlocation) {
        this.vlocation = vlocation;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getLivepic() {
        return livepic;
    }
    //    public CommonObject getCheckin() {
//        return checkin;
//    }
}
