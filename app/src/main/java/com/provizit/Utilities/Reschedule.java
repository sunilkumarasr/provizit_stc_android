package com.provizit.Utilities;

import java.io.Serializable;

public class Reschedule implements Serializable {

    private long date,start,end;

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
}
