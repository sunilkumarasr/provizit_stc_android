package com.provizit.Services;

import com.provizit.Utilities.CompanyData;

import java.io.Serializable;

public class TotalModelCount implements Serializable {

    public Integer result;

    public String total_counts;

    public CompanyData items;
    public CompanyData item;


    public String getTotal_counts() {
        return total_counts;
    }

    public CompanyData getItem() {
        return item;
    }

    public void setItem(CompanyData item) {
        this.item = item;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public CompanyData getItems() {
        return items;
    }

    public void setItems(CompanyData items) {
        this.items = items;
    }

}
