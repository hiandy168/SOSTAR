package com.renyu.sostar.bean;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/2/26.
 */

public class EmployerAuthRequest {
    /**
     * deviceId : string
     * param : {"address":"string","cerPath":"string","companyCode":"string","companyName":"string","contactPhone":"string","introduction":"string","licPath":"string","logoPath":"string","rifPath":"string","userId":0}
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
         * cerPath : string
         * companyCode : string
         * companyName : string
         * contactPhone : string
         * introduction : string
         * licPath : string
         * logoPath : string
         * rifPath : string
         * userId : 0
         */

        private String address;
        private String cerPath;
        private String companyCode;
        private String companyName;
        private String contactPhone;
        private String introduction;
        private String licPath;
        private String logoPath;
        private String rifPath;
        private int userId;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCerPath() {
            return cerPath;
        }

        public void setCerPath(String cerPath) {
            this.cerPath = cerPath;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getLicPath() {
            return licPath;
        }

        public void setLicPath(String licPath) {
            this.licPath = licPath;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getRifPath() {
            return rifPath;
        }

        public void setRifPath(String rifPath) {
            this.rifPath = rifPath;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
