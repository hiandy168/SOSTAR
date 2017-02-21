package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/22.
 */

public class VCodeRequest {
    /**
     * deviceId : string
     * param : {"phone":"string"}
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
