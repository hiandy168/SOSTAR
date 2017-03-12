package com.renyu.sostar.bean;

import java.util.List;

/**
 * Created by renyu on 2017/3/12.
 */

public class EmployeeIndexResponse {
    /**
     * count : 0
     * logoPath : string
     * orders : [{"companyName":"string","latitude":"string","longitude":"string","orderId":"string"}]
     */

    private int count;
    private String logoPath;
    private List<OrdersBean> orders;

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

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * companyName : string
         * latitude : string
         * longitude : string
         * orderId : string
         */

        private String companyName;
        private String latitude;
        private String longitude;
        private String orderId;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
