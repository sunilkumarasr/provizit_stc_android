package com.provizit.AdapterAndModel;

import com.provizit.Utilities.MobileData;

import java.io.Serializable;

public class ContractorData implements Serializable {

    public String name;
    public String email;
    public String mobile;
    public String companyName;
    public String id_number;
    public String id_name;
    public String nationality;
    public MobileData mobileData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public MobileData getMobileData() {
        return mobileData;
    }

    public void setMobileData(MobileData mobileData) {
        this.mobileData = mobileData;
    }
}
