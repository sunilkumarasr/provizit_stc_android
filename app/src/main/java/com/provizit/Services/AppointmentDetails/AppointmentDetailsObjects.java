package com.provizit.Services.AppointmentDetails;

import com.provizit.Utilities.CommonObject;
import com.provizit.Utilities.Pdfs;
import com.provizit.Utilities.VData;

import java.util.ArrayList;

public class AppointmentDetailsObjects {


    String comp_id;
    String purpose;
    String note;
    String emp_id;

    private CommonObject _id;
    private CommonObject a_time;

    private VData vData;

    ArrayList<Pdfs> pdfs;


    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public CommonObject getA_time() {
        return a_time;
    }

    public void setA_time(CommonObject a_time) {
        this.a_time = a_time;
    }

    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public void setPdfs(ArrayList<Pdfs> pdfs) {
        this.pdfs = pdfs;
    }

    public VData getvData() {
        return vData;
    }

    public void setvData(VData vData) {
        this.vData = vData;
    }

    public ArrayList<Pdfs> getPdfs() {
        return pdfs;
    }




}
