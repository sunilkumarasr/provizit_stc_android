package com.provizit.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Vehicles implements Serializable {
    private String desc,name,number,type;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public JSONObject getVehicles(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("desc", this.desc);
            jo.put("name", this.name);
            jo.put("number", this.number);
            jo.put("type", this.type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
