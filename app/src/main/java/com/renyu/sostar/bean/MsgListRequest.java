package com.renyu.sostar.bean;

import android.os.Build;

import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;

/**
 * Created by renyu on 2017/3/1.
 */

public class MsgListRequest {
    /**
     * deviceId : string
     * param : {"pagination":{"pageSize":0,"startPos":0},"userId":0}
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
         * pagination : {"pageSize":0,"startPos":0}
         * userId : 0
         */

        private PaginationBean pagination;
        private int userId;

        public PaginationBean getPagination() {
            return pagination;
        }

        public void setPagination(PaginationBean pagination) {
            this.pagination = pagination;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public static class PaginationBean {
            /**
             * pageSize : 0
             * startPos : 0
             */

            private int pageSize;
            private int startPos;

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getStartPos() {
                return startPos;
            }

            public void setStartPos(int startPos) {
                this.startPos = startPos;
            }
        }
    }
}
