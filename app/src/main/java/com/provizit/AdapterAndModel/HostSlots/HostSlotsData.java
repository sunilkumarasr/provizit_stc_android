package com.provizit.AdapterAndModel.HostSlots;

import com.provizit.Utilities.CategoryData;
import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.Invited;
import com.provizit.Utilities.Room;

import java.io.Serializable;
import java.util.ArrayList;

public class HostSlotsData implements Serializable {


    private CommonObject _id;
    private String emp_id;
    private String subject;
    private long date,start,end;
    private String meetingroom;
    private int samecabin;
    Room room;
    private CategoryData categoryData;
    ArrayList<Invited> invites;


    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getMeetingroom() {
        return meetingroom;
    }

    public void setMeetingroom(String meetingroom) {
        this.meetingroom = meetingroom;
    }

    public int getSamecabin() {
        return samecabin;
    }

    public void setSamecabin(int samecabin) {
        this.samecabin = samecabin;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public ArrayList<Invited> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<Invited> invites) {
        this.invites = invites;
    }
}
