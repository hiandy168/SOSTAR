package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/3/30.
 */

public class OverTimeRequest {
    /**
     * deviceId : string
     * param : {"allExtraPrice":0,"extraNum":0,"extraPrice":0,"extraTime":0,"orderId":0,"userId":0}
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
         * allExtraPrice : 0
         * extraNum : 0
         * extraPrice : 0
         * extraTime : 0
         * orderId : 0
         * userId : 0
         */

        private int allExtraPrice;
        private int extraNum;
        private int extraPrice;
        private int extraTime;
        private int orderId;
        private int userId;

        public int getAllExtraPrice() {
            return allExtraPrice;
        }

        public void setAllExtraPrice(int allExtraPrice) {
            this.allExtraPrice = allExtraPrice;
        }

        public int getExtraNum() {
            return extraNum;
        }

        public void setExtraNum(int extraNum) {
            this.extraNum = extraNum;
        }

        public int getExtraPrice() {
            return extraPrice;
        }

        public void setExtraPrice(int extraPrice) {
            this.extraPrice = extraPrice;
        }

        public int getExtraTime() {
            return extraTime;
        }

        public void setExtraTime(int extraTime) {
            this.extraTime = extraTime;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
