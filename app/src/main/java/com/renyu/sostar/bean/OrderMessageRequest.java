package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/3/24.
 */

public class OrderMessageRequest {
    /**
     * deviceId : string
     * param : {"msg":"string","orderId":"string","receiverId":"string","userId":0}
     * platform : string
     * sysversion : string
     * ver : string
     */

    private String deviceId = Utils.getUniquePsuedoID();
    private ParamBean param;
    private String platform = "android";
    private String sysversion = Build.VERSION.RELEASE;
    private String ver = BuildConfig.VERSION_NAME;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSysversion() {
        return sysversion;
    }

    public void setSysversion(String sysversion) {
        this.sysversion = sysversion;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public static class ParamBean {
        /**
         * msg : string
         * orderId : string
         * receiverId : string
         * userId : 0
         */

        private String msg;
        private String orderId;
        private String receiverId;
        private int userId;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
