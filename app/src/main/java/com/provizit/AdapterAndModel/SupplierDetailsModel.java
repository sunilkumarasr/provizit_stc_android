package com.provizit.AdapterAndModel;

import com.provizit.Utilities.MobileData;

import java.io.Serializable;

public class SupplierDetailsModel implements Serializable {

    private String contact_person;
    private String supplier_email;
    private String id_number;
    private String nationality;
    private String vehicle_no;
    private String vehicle_type;
    private String disabledStatus;
    private MobileData supplier_mobile;


    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getSupplier_email() {
        return supplier_email;
    }

    public void setSupplier_email(String supplier_email) {
        this.supplier_email = supplier_email;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getDisabledStatus() {
        return disabledStatus;
    }

    public void setDisabledStatus(String disabledStatus) {
        this.disabledStatus = disabledStatus;
    }

    public MobileData getSupplier_mobile() {
        return supplier_mobile;
    }

    public void setSupplier_mobile(MobileData supplier_mobile) {
        this.supplier_mobile = supplier_mobile;
    }
}
