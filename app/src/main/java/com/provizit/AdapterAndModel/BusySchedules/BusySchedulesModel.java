package com.provizit.AdapterAndModel.BusySchedules;

import com.provizit.Utilities.CompanyData;

import java.io.Serializable;
import java.util.ArrayList;

public class BusySchedulesModel implements Serializable {

    public Integer result;
    public ArrayList<BusySchedulesData> items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public ArrayList<BusySchedulesData> getItems() {
        return items;
    }

    public void setItems(ArrayList<BusySchedulesData> items) {
        this.items = items;
    }

}

