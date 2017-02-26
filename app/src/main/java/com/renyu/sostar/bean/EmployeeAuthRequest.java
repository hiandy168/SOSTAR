package com.renyu.sostar.bean;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/2/24.
 */

public class EmployeeAuthRequest {
    /**
     * deviceId : string
     * param : {"address":"string","certificateId":"string","introduction":"string","name":"string","phone":"string","picCerOppo":"string","picCerpos":"string","picPath":"string","userId":0}
     * ver : string
     */

    private String deviceId = Utils.getUniquePsuedoID();
    private ParamBean param;
    private String ver = BuildConfig.VERSION_NAME;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * address : string
         * certificateId : string
         * introduction : string
         * name : string
         * phone : string
         * picCerOppo : string
         * picCerpos : string
         * picPath : string
         * userId : 0
         */

        private String address;
        private String certificateId;
        private String introduction;
        private String name;
        private String phone;
        private String picCerOppo;
        private String picCerpos;
        private String picPath;
        private int userId;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCertificateId() {
            return certificateId;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPicCerOppo() {
            return picCerOppo;
        }

        public void setPicCerOppo(String picCerOppo) {
            this.picCerOppo = picCerOppo;
        }

        public String getPicCerpos() {
            return picCerpos;
        }

        public void setPicCerpos(String picCerpos) {
            this.picCerpos = picCerpos;
        }

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
