package com.provizit.Utilities;

import java.io.Serializable;

public class VData implements Serializable {

    private CommonObject _id;

    private String comp_id;
    private String email;
    private String name;
    private String mobile;
    private String company;

    public String getMobile() {
        return mobile;
    }

    public CommonObject get_id() {
        return _id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }


}
