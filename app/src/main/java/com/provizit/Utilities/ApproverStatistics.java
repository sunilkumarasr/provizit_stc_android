package com.provizit.Utilities;

import java.io.Serializable;

public class ApproverStatistics implements Serializable {
    private String id,roleid,emp_id;
    private Boolean view_status;
    private Long status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public Boolean getView_status() {
        return view_status;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}

