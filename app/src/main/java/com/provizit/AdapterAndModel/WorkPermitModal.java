package com.provizit.AdapterAndModel;

import com.provizit.Utilities.CompanyData;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkPermitModal implements Serializable {

    public Integer result;
    public CompanyData items;

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
