package com.provizit.AdapterAndModel;

import com.provizit.Utilities.CompanyDetails;

import java.io.Serializable;

public class CompanyDetailsModel implements Serializable {
    public Integer result;
    private TotalCounts total_counts;
    private IncompleteData incomplete_data;
    public CompanyDetailsAddress items;


    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public CompanyDetailsAddress getItems() {
        return items;
    }

    public void setItems(CompanyDetailsAddress items) {
        this.items = items;
    }

    public TotalCounts getTotal_counts() {
        return total_counts;
    }

    public void setTotal_counts(TotalCounts total_counts) {
        this.total_counts = total_counts;
    }

    public IncompleteData getIncomplete_data() {
        return incomplete_data;
    }

    public void setIncomplete_data(IncompleteData incomplete_data) {
        this.incomplete_data = incomplete_data;
    }
}
