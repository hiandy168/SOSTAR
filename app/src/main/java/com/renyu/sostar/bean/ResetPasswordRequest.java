package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/22.
 */

public class ResetPasswordRequest {
    /**
     * deviceId : string
     * param : {"captcha":"string","password":"string","phone":"string"}
     * ver : string
     */

    private String deviceId;
    private ParamBean param;
    private String ver;

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

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public static class ParamBean {
        /**
         * captcha : string
         * password : string
         * phone : string
         */

        private String captcha;
        private String password;
        private String phone;

        public String getCaptcha() {
            return captcha;
        }

        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
