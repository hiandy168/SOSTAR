package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/4/17.
 */

public class FlowResponse {
    /**
     * cashTotal : 0
     * date : 2017-04-17T05:17:17.275Z
     * orderId : string
     * type : string
     */

    private int cashTotal;
    private String date;
    private String orderId;
    private String type;

    public int getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(int cashTotal) {
        this.cashTotal = cashTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
