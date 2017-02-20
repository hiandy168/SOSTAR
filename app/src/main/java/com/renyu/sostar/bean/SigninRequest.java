package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/20.
 */

public class SigninRequest {
    /**
     * deviceId : 123
     * param : {"password":"15261405166","phone":"15261405166"}
     * ver : 1
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
         * password : 15261405166
         * phone : 15261405166
         */

        private String password;
        private String phone;

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
