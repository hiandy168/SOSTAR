package com.renyu.sostar.bean;

import java.io.Serializable;

/**
 * Created by renyu on 2017/2/24.
 */

public class MyCenterEmployerResponse implements Serializable {
    /**
     * authentication : string
     * cerPath : string
     * closeRate : string
     * companyCode : string
     * companyName : string
     * contactPhone : string
     * finishedOrderNum : string
     * introduction : string
     * licPath : string
     * logoPath : string
     * messageNum : string
     * ongoingOrder : string
     * rangeArea : string
     * rifPath : string
     * star : string
     */

    private String authentication;
    private String cerPath;
    private String closeRate;
    private String companyCode;
    private String companyName;
    private String contactPhone;
    private String finishedOrderNum;
    private String introduction;
    private String licPath;
    private String logoPath;
    private String messageNum;
    private String ongoingOrder;
    private String rangeArea;
    private String rifPath;
    private String star;
    private String webAddress;

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getCerPath() {
        return cerPath;
    }

    public void setCerPath(String cerPath) {
        this.cerPath = cerPath;
    }

    public String getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public String getLicPath() {
        return licPath;
    }

    public void setLicPath(String licPath) {
        this.licPath = licPath;
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

    public String getRangeArea() {
        return rangeArea;
    }

    public void setRangeArea(String rangeArea) {
        this.rangeArea = rangeArea;
    }

    public String getRifPath() {
        return rifPath;
    }

    public void setRifPath(String rifPath) {
        this.rifPath = rifPath;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }
}
