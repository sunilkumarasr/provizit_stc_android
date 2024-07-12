package com.provizit.Utilities;

public class People {

    private String MeetingRoom,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public People(String meetingRoom) {
        MeetingRoom = meetingRoom;
    }

    public String getMeetingRoom() {
        return MeetingRoom;
    }

    public void setMeetingRoom(String meetingRoom) {
        MeetingRoom = meetingRoom;
    }
}