package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/2/22.
 */

public class VCodeRequest {
    /**
     * deviceId : string
     * param : {"phone":"string"}
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
         * phone : string
         */

        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
