package com.renyu.sostar.bean;

import java.util.List;

/**
 * Created by renyu on 2017/3/1.
 */

public class MsgListResponse {

    /**
     * total : 23
     * data : [{"messageId":"372","crtTime":1488371547000,"upTime":null,"message":"56789","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"358","crtTime":1488371544000,"upTime":null,"message":"45678","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"344","crtTime":1488371540000,"upTime":null,"message":"34567","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"330","crtTime":1488371537000,"upTime":null,"message":"23456","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"316","crtTime":1488371520000,"upTime":null,"message":"12345","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"302","crtTime":1488371517000,"upTime":null,"message":"8900","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"288","crtTime":1488371511000,"upTime":null,"message":"7890","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"274","crtTime":1488371508000,"upTime":null,"message":"6789","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"260","crtTime":1488371505000,"upTime":null,"message":"5678","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"246","crtTime":1488371502000,"upTime":null,"message":"4567","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"232","crtTime":1488371499000,"upTime":null,"message":"3456","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"218","crtTime":1488371495000,"upTime":null,"message":"2345","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"204","crtTime":1488371423000,"upTime":null,"message":"890","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"190","crtTime":1488371419000,"upTime":null,"message":"789","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"176","crtTime":1488371416000,"upTime":null,"message":"678","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"162","crtTime":1488371413000,"upTime":null,"message":"567","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"148","crtTime":1488371410000,"upTime":null,"message":"456","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"134","crtTime":1488371407000,"upTime":null,"message":"345","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"120","crtTime":1488371403000,"upTime":null,"message":"234","readFlg":"0","receiverIds":"14","msgType":"2"},{"messageId":"106","crtTime":1488371391000,"upTime":null,"message":"123","readFlg":"0","receiverIds":"14","msgType":"2"}]
     */

    private int total;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * messageId : 372
         * crtTime : 1488371547000
         * upTime : null
         * message : 56789
         * readFlg : 0
         * receiverIds : 14
         * msgType : 2
         */

        private String messageId;
        private long crtTime;
        private Object upTime;
        private String message;
        private String readFlg;
        private String receiverIds;
        private String msgType;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public long getCrtTime() {
            return crtTime;
        }

        public void setCrtTime(long crtTime) {
            this.crtTime = crtTime;
        }

        public Object getUpTime() {
            return upTime;
        }

        public void setUpTime(Object upTime) {
            this.upTime = upTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getReadFlg() {
            return readFlg;
        }

        public void setReadFlg(String readFlg) {
            this.readFlg = readFlg;
        }

        public String getReceiverIds() {
            return receiverIds;
        }

        public void setReceiverIds(String receiverIds) {
            this.receiverIds = receiverIds;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }
    }
}
