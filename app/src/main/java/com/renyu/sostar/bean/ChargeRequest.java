package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/4/9.
 */

public class ChargeRequest {
    /**
     * deviceId : string
     * param : {"amount":0,"payeeAccount":"string","payeeRealName":"string","userId":"string"}
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
         * amount : 0
         * payeeAccount : string
         * payeeRealName : string
         * userId : string
         */

        private int amount;
        private String payeeAccount;
        private String payeeRealName;
        private String userId;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getPayeeAccount() {
            return payeeAccount;
        }

        public void setPayeeAccount(String payeeAccount) {
            this.payeeAccount = payeeAccount;
        }

        public String getPayeeRealName() {
            return payeeRealName;
        }

        public void setPayeeRealName(String payeeRealName) {
            this.payeeRealName = payeeRealName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
