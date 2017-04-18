package com.renyu.sostar.bean;

import java.util.List;

/**
 * Created by renyu on 2017/4/18.
 */

public class PayInfoResponse {
    /**
     * orderId : 0
     * staffName : ["string"]
     * tipsMoney : 0
     * totalMoney : 0
     * wagesMoney : 0
     */

    private int orderId;
    private int tipsMoney;
    private int totalMoney;
    private int wagesMoney;
    private List<String> staffName;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTipsMoney() {
        return tipsMoney;
    }

    public void setTipsMoney(int tipsMoney) {
        this.tipsMoney = tipsMoney;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getWagesMoney() {
        return wagesMoney;
    }

    public void setWagesMoney(int wagesMoney) {
        this.wagesMoney = wagesMoney;
    }

    public List<String> getStaffName() {
        return staffName;
    }

    public void setStaffName(List<String> staffName) {
        this.staffName = staffName;
    }
}
