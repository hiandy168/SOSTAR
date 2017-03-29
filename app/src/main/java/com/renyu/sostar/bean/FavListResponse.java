package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/3/29.
 */

public class FavListResponse {
    /**
     * name : string
     * nickName : string
     * picPath : string
     * star : string
     * userId : string
     */

    private String name;
    private String nickName;
    private String picPath;
    private String star;
    private String userId;

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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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
}
