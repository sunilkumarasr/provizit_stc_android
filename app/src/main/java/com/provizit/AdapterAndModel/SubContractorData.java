package com.provizit.AdapterAndModel;

import com.provizit.Utilities.MobileData;
import java.io.Serializable;

public class SubContractorData implements Serializable {

    public String name;
    public String company;
    public String companyName;
    public String id_type;
    public String id_name;
    public String nationality;
    public String id_number;
    public String email;
    public String mobile;
    public MobileData mobileData;
    public String performing_work;
    public boolean common_nation;
    public boolean disabledStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
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

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
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

    public MobileData getMobileData() {
        return mobileData;
    }

    public void setMobileData(MobileData mobileData) {
        this.mobileData = mobileData;
    }

    public String getPerforming_work() {
        return performing_work;
    }

    public void setPerforming_work(String performing_work) {
        this.performing_work = performing_work;
    }

    public boolean isCommon_nation() {
        return common_nation;
    }

    public void setCommon_nation(boolean common_nation) {
        this.common_nation = common_nation;
    }

    public boolean isDisabledStatus() {
        return disabledStatus;
    }

    public void setDisabledStatus(boolean disabledStatus) {
        this.disabledStatus = disabledStatus;
    }
}
