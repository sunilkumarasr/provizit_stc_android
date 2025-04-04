package com.provizit.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pdfs implements Serializable {
    private String name,value;
    private boolean status;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isStatus() {
        return status;
    }
    public JSONObject getPdfs(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", this.name);
            jo.put("value", this.value);
            jo.put("status", this.status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
