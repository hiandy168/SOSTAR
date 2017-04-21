package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/4/15.
 */

public class EmployerCashAvaliableResponse {
    /**
     * cashAvaiable : 0
     * cashFrozen : 0
     * cashTotal : 0
     * payeeAccount : string
     * payeeRealName : string
     */

    private String cashAvaiable;
    private int cashFrozen;
    private int cashTotal;
    private String payeeAccount;
    private String payeeRealName;

    public String getCashAvaiable() {
        return cashAvaiable;
    }

    public void setCashAvaiable(String cashAvaiable) {
        this.cashAvaiable = cashAvaiable;
    }

    public int getCashFrozen() {
        return cashFrozen;
    }

    public void setCashFrozen(int cashFrozen) {
        this.cashFrozen = cashFrozen;
    }

    public int getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(int cashTotal) {
        this.cashTotal = cashTotal;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getPayeeRealName() {
        return payeeRealName;
    }

    public void setPayeeRealName(String payeeRealName) {
        this.payeeRealName = payeeRealName;
    }
}
