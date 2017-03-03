package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/3/3.
 */

public class NotificationChangeRequest {
    /**
     * deviceId : string
     * param : {"msgFlg":"string","userId":"string"}
     * platform : string
     * sysversion : string
     * ver : string
     */

    private String deviceId = Utils.getUniquePsuedoID();
    private ParamBean param;
    private String platform = "android";
    private String sysversion = Build.VERSION.RELEASE;
    private String ver = BuildConfig.VERSION_NAME;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
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
}
