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
    private String cashFrozen;
    private String cashTotal;
    private String payeeAccount;
    private String payeeRealName;

    public String getCashAvaiable() {
        return cashAvaiable;
    }

    public void setCashAvaiable(String cashAvaiable) {
        this.cashAvaiable = cashAvaiable;
    }

    public String getCashFrozen() {
        return cashFrozen;
    }

    public void setCashFrozen(String cashFrozen) {
        this.cashFrozen = cashFrozen;
    }

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
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
