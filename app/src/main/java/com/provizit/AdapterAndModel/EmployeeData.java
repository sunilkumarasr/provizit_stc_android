package com.provizit.AdapterAndModel;

import java.io.Serializable;

public class EmployeeData implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
