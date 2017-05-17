package com.renyu.sostar.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by renyu on 2017/3/14.
 */

public class OrderResponse implements Serializable {
    /**
     * address : string
     * companyName : string
     * confirmFlg : string
     * createUserId : string
     * crtTime : 2017-03-14T02:16:31.348Z
     * description : string
     * distance : string
     * endTime : string
     * jobType : string
     * latitude : string
     * logoPath : string
     * longitude : string
     * okStaffAccount : 0
     * orderId : string
     * orderRange : 0
     * orderStatus : string
     * paymentType : string
     * periodTime : string
     * picListArray : ["string"]
     * sex : string
     * staffAccount : 0
     * startTime : string
     * unitPrice : 0
     * unitPriceType : string
     */

    private String address;
    private String aggregateAddress;
    private String aggregateTime;
    private String companyName;
    private String confirmFlg;
    private String createUserId;
    private String crtTime;
    private String description;
    private String distance;
    private String endTime;
    private String jobType;
    private String latitude;
    private String logoPath;
    private String longitude;
    private int okStaffAccount;
    private String orderId;
    private int orderRange;
    private String orderStatus;
    private String paymentType;
    private String periodTime;
    private String sex;
    private int staffAccount;
    private String startTime;
    private String unitPrice;
    private String unitPriceType;
    private List<String> picListArray;
    private List<String> picStaffArray;
    private int payFlg;
    private String evaFlg;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConfirmFlg() {
        return confirmFlg;
    }

    public void setConfirmFlg(String confirmFlg) {
        this.confirmFlg = confirmFlg;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getOkStaffAccount() {
        return okStaffAccount;
    }

    public void setOkStaffAccount(int okStaffAccount) {
        this.okStaffAccount = okStaffAccount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderRange() {
        return orderRange;
    }

    public void setOrderRange(int orderRange) {
        this.orderRange = orderRange;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getStaffAccount() {
        return staffAccount;
    }

    public void setStaffAccount(int staffAccount) {
        this.staffAccount = staffAccount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPriceType() {
        return unitPriceType;
    }

    public void setUnitPriceType(String unitPriceType) {
        this.unitPriceType = unitPriceType;
    }

    public List<String> getPicListArray() {
        return picListArray;
    }

    public void setPicListArray(List<String> picListArray) {
        this.picListArray = picListArray;
    }

    public List<String> getPicStaffArray() {
        return picStaffArray;
    }

    public void setPicStaffArray(List<String> picStaffArray) {
        this.picStaffArray = picStaffArray;
    }

    public String getAggregateAddress() {
        return aggregateAddress;
    }

    public void setAggregateAddress(String aggregateAddress) {
        this.aggregateAddress = aggregateAddress;
    }

    public String getAggregateTime() {
        return aggregateTime;
    }

    public void setAggregateTime(String aggregateTime) {
        this.aggregateTime = aggregateTime;
    }

    public int getPayFlg() {
        return payFlg;
    }

    public void setPayFlg(int payFlg) {
        this.payFlg = payFlg;
    }

    public String getEvaFlg() {
        return evaFlg;
    }

    public void setEvaFlg(String evaFlg) {
        this.evaFlg = evaFlg;
    }
}
