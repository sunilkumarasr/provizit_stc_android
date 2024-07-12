package com.provizit.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class Invited implements Serializable {
    private String name,mobile,email,company,id="",link;

    ArrayList<Reschedule> reschedule;
    private boolean covisitor = false;
    private boolean assign = false;
    private Boolean view_status;
    ArrayList<String> pic;
    ArrayList<Pdfs> pdfs;
    private boolean pdfStatus;

    private int status = 0,verify = 0,pslot;
    private boolean isChecked;

    private String cat_id,lot_id,slot;

    private boolean auto_allot;
    private String pcat_name, lot_name,slot_name,plate_no;

    private boolean parking_status;
    public int getPslot() {
        return pslot;
    }

    public void setPslot(int pslot) {
        this.pslot = pslot;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public boolean isAssign() {
        return assign;
    }

    public void setAssign(boolean assign) {
        this.assign = assign;
    }


    public String getId() {
        return id;
    }

    public ArrayList<Pdfs> getPdfs() {
        return pdfs;
    }

    public void setPdfs(ArrayList<Pdfs> pdfs) {
        this.pdfs = pdfs;
    }

    public boolean isPdfStatus() {
        return pdfStatus;
    }

    public void setPdfStatus(boolean pdfStatus) {
        this.pdfStatus = pdfStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Reschedule> getReschedule() {
        return reschedule;
    }

    public void setReschedule(ArrayList<Reschedule> reschedule) {
        this.reschedule = reschedule;
    }

    public Boolean getView_status() {
        return view_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public boolean isCovisitor() {
        return covisitor;
    }

    public void setCovisitor(boolean covisitor) {
        this.covisitor = covisitor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getLot_id() {
        return lot_id;
    }

    public void setLot_id(String lot_id) {
        this.lot_id = lot_id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String pslots) {
        this.slot = pslots;
    }

    public boolean getAuto_allot() {
        return auto_allot;
    }

    public void setAuto_allot(boolean auto_allot) {
        this.auto_allot = auto_allot;
    }

    public String getPcat_name() {
        return pcat_name;
    }

    public void setPcat_name(String pcat_name) {
        this.pcat_name = pcat_name;
    }

    public String getLot_name() {
        return lot_name;
    }

    public void setLot_name(String lot_name) {
        this.lot_name = lot_name;
    }

    public String getSlot_name() {
        return slot_name;
    }

    public void setSlot_name(String slot_name) {
        this.slot_name = slot_name;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public boolean getParking_status() {
        return parking_status;
    }

    public void setParking_status(boolean parking_status) {
        this.parking_status = parking_status;
    }

    public JSONObject getInvites(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", this.name);
            jo.put("mobile", this.mobile);
            jo.put("company", this.company);
            jo.put("id", this.id);
            jo.put("email", this.email);
            jo.put("covisitor", this.covisitor);
            jo.put("link", this.link);
            jo.put("status", this.status);
            jo.put("verify", this.verify);
            jo.put("assign", this.assign);
            jo.put("pcategory", this.cat_id);
            jo.put("plot", this.lot_id);
            jo.put("slot", this.slot);
            jo.put("pslot", this.pslot);
            jo.put("auto_allot", this.auto_allot);
            jo.put("plate_no", this.plate_no);
            jo.put("reschedule", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

}
