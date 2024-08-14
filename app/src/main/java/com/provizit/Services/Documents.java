package com.provizit.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Documents implements Serializable {
    private String id;
    private ArrayList<String> pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public JSONObject getDocuments(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", this.id);
            jo.put("pic", new JSONArray(this.pic));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
