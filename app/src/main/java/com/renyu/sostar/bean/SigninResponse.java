package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/20.
 */

public class SigninResponse {

    /**
     * authentication : string
     * closeRate : string
     * introduction : string
     * name : string
     * path : string
     * star : string
     * userId : string
     * userType : string
     */

    private String authentication;
    private String closeRate;
    private String introduction;
    private String name;
    private String path;
    private String star;
    private String userId;
    private String userType;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
