package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/4/17.
 */

public class FlowResponse {

    /**
     * cashAmount : 0
     * cashTotal : 0
     * date : 2017-04-18T05:40:54.172Z
     * orderId : string
     * type : string
     */

    private int cashAmount;
    private int cashTotal;
    private String date;
    private String orderId;
    private String type;

    public int getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

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
