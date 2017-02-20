package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/20.
 */

public class SigninResponse {
    /**
     * userId : 1
     * userType : null
     */

    private String userId;
    private String userType;

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
