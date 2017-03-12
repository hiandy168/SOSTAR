package com.renyu.sostar.bean;

import java.util.List;

/**
 * Created by renyu on 2017/3/12.
 */

public class EmployerIndexResponse {
    /**
     * count : 0
     * logoPath : string
     * staffs : [{"latitude":"string","longitude":"string","name":"string","nickName":"string","userId":"string"}]
     */

    private int count;
    private String logoPath;
    private List<StaffsBean> staffs;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public List<StaffsBean> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<StaffsBean> staffs) {
        this.staffs = staffs;
    }

    public static class StaffsBean {
        /**
         * latitude : string
         * longitude : string
         * name : string
         * nickName : string
         * userId : string
         */

        private String latitude;
        private String longitude;
        private String name;
        private String nickName;
        private String userId;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
