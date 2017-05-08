package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/5/8.
 */

public class PushResponse {
    /**
     * msgFlg : string
     * userId : string
     */

    private String msgFlg;
    private String userId;

    public String getMsgFlg() {
        return msgFlg;
    }

    public void setMsgFlg(String msgFlg) {
        this.msgFlg = msgFlg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
