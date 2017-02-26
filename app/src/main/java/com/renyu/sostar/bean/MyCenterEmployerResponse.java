package com.renyu.sostar.bean;

import java.io.Serializable;

/**
 * Created by renyu on 2017/2/24.
 */

public class MyCenterEmployerResponse implements Serializable {

    /**
     * authentication : string
     * closeRate : string
     * companyId : string
     * companyName : string
     * contactPhone : string
     * finishedOrderNum : string
     * introduction : string
     * logoPath : string
     * messageNum : string
     * ongoingOrder : string
     * rangeArea : string
     * star : string
     */

    private String authentication;
    private String closeRate;
    private String companyId;
    private String companyName;
    private String contactPhone;
    private String finishedOrderNum;
    private String introduction;
    private String logoPath;
    private String messageNum;
    private String ongoingOrder;
    private int rangeArea;
    private String star;

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getFinishedOrderNum() {
        return finishedOrderNum;
    }

    public void setFinishedOrderNum(String finishedOrderNum) {
        this.finishedOrderNum = finishedOrderNum;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(String messageNum) {
        this.messageNum = messageNum;
    }

    public String getOngoingOrder() {
        return ongoingOrder;
    }

    public void setOngoingOrder(String ongoingOrder) {
        this.ongoingOrder = ongoingOrder;
    }

    public int getRangeArea() {
        return rangeArea;
    }

    public void setRangeArea(int rangeArea) {
        this.rangeArea = rangeArea;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
