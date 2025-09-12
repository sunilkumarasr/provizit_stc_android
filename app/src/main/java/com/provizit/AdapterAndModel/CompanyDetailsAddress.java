package com.provizit.AdapterAndModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyDetailsAddress implements Serializable {

    private ArrayList<LocationAddressList> address;


    public ArrayList<LocationAddressList> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<LocationAddressList> address) {
        this.address = address;
    }

}
