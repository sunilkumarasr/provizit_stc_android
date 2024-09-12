package com.provizit.AdapterAndModel.HostSlots;

import java.io.Serializable;
import java.util.ArrayList;

public class HostSlotsModel implements Serializable {
    public Integer result;
    public ArrayList<HostSlotsData> items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public ArrayList<HostSlotsData> getItems() {
        return items;
    }

    public void setItems(ArrayList<HostSlotsData> items) {
        this.items = items;
    }
}
