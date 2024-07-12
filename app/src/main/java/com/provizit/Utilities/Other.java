package com.provizit.Utilities;

import java.io.Serializable;

public class Other implements Serializable {


//    delete: false
//    depends: false
//    depends_upon: ""
//    depends_upon_val: []
//    disabled: true
//    hidden: false
//    replace: "Emp. Code"
//    unique: true


    private String label,data = "Not Mentioned",model;
    private Boolean status=false,active=false,unique=false,hidden=false,depends=false,delete=false;

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDepends() {
        return depends;
    }

    public void setDepends(Boolean depends) {
        this.depends = depends;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
//    public JSONObject getOther(){
//        JSONObject jo = new JSONObject();
//        try {
//            jo.put("label", this.label);
//            jo.put("data", this.data);
//            jo.put("model", this.model);
//            jo.put("status", this.status);
//            jo.put("active", this.active);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jo;
//    }
}
