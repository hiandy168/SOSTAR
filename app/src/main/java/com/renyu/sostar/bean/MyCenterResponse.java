package com.renyu.sostar.bean;

import java.io.Serializable;

/**
 * Created by renyu on 2017/2/22.
 */

public class MyCenterResponse implements Serializable {

    /**
     * age : string
     * authentication : string
     * certificateId : string
     * closeRate : string
     * evaluateLevel : string
     * finishedOrders : 0
     * introduction : string
     * name : string
     * nickName : string
     * phone : string
     * picCerOppo : string
     * picCerpos : string
     * picPath : string
     * rangeArea : 0
     * sex : string
     */

    private String age;
    private String authentication;
    private String certificateId;
    private String closeRate;
    private String evaluateLevel;
    private int finishedOrders;
    private String introduction;
    private String name;
    private String nickName;
    private String phone;
    private String picCerOppo;
    private String picCerpos;
    private String picPath;
    private int rangeArea;
    private String sex;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(String closeRate) {
        this.closeRate = closeRate;
    }

    public String getEvaluateLevel() {
        return evaluateLevel;
    }

    public void setEvaluateLevel(String evaluateLevel) {
        this.evaluateLevel = evaluateLevel;
    }

    public int getFinishedOrders() {
        return finishedOrders;
    }

    public void setFinishedOrders(int finishedOrders) {
        this.finishedOrders = finishedOrders;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicCerOppo() {
        return picCerOppo;
    }

    public void setPicCerOppo(String picCerOppo) {
        this.picCerOppo = picCerOppo;
    }

    public String getPicCerpos() {
        return picCerpos;
    }

    public void setPicCerpos(String picCerpos) {
        this.picCerpos = picCerpos;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getRangeArea() {
        return rangeArea;
    }

    public void setRangeArea(int rangeArea) {
        this.rangeArea = rangeArea;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
