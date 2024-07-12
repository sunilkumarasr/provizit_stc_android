package com.provizit.Utilities;

import java.io.Serializable;

public class CoordinatorData implements Serializable {

    private CommonObject _id;
    private String name;

    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
