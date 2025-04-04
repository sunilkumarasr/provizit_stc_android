package com.provizit.Utilities;

import java.util.ArrayList;

public class NotificationObjects {
    String $oid;
    String subject;
    String purpose;
    Long date,start,end;
    //created_time
    Long $numberLong;

    //employeee
    String name;
    private ArrayList<String> pic;

    public String get$oid() {
        return $oid;
    }

    public void set$oid(String $oid) {
        this.$oid = $oid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public Long get$numberLong() {
        return $numberLong;
    }

    public void set$numberLong(Long $numberLong) {
        this.$numberLong = $numberLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }


}
