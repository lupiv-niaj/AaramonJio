package com.aaramon.aaramonjio.order;

/**
 * Created by DELL STORE on 17-May-17.
 */

public class OrderSummaryModel {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    String detail;
    public OrderSummaryModel(String name, String detail) {
        this.name = name;
        this.detail=detail;
    }

}
