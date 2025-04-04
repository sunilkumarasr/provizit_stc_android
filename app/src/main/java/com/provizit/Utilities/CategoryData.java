package com.provizit.Utilities;

import java.io.Serializable;

public class CategoryData implements Serializable {

    private CommonObject _id;
    private String name;
    private boolean cat_type;

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

    public boolean getCat_type() {
        return cat_type;
    }

    public void setCat_type(boolean cat_type) {
        this.cat_type = cat_type;
    }
}
