package com.renyu.sostar.params;

import android.os.Environment;

import java.io.File;

/**
 * Created by renyu on 2016/12/26.
 */

public class CommonParams {

    // 腾讯bugly appId
    public static final String BUGLY_APPID="95c4c316f5";
    // 微信支付
    public static final String WX_APP_ID="";

    // 项目根目录
    public static final String ROOT_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator + "sostar";
    // 项目图片目录
    public static final String IMAGE_PATH= ROOT_PATH + File.separator + "image";
    // 项目文件目录
    public static final String FILE_PATH= ROOT_PATH + File.separator + "file";
    // 项目热修复目录
    public static final String HOTFIX_PATH= ROOT_PATH + File.separator + "hotfix";
    // 项目日志目录
    public static final String LOG_PATH= ROOT_PATH + File.separator + "log";
    // fresco缓存目录
    public static final String FRESCO_CACHE_PATH= ROOT_PATH + File.separator + "cache";
    public static final String FRESCO_CACHE_NAME= "fresco_cache";

    public static final String USER_PHONE="user_phone";
    public static final String USER_PASSWORD="user_password";
    public static final String USER_TYPE="user_type";
    public static final String USER_ID="user_id";
    public static final String USER_SIGNIN="user_signin";
    // 启动页跳转流程
    public static final String FROM="from";
    // 跳转到首页
    public static final int INDEX=1;
    // 跳转到用户身份
    public static final int CUSTOMER_STATE=2;
    // 跳转到登录页
    public static final int SIGNIN=3;
    // 调转到注册页
    public static final int SIGNUP=4;
    // 关闭
    public static final int FINISH=5;
    // 什么都不执行
    public static final int NOTHING=6;

    public static final int RESULT_MODIFY_PWD=1000;
    public static final int RESULT_UPDATEUSERINFO=1001;
    public static final int RESULT_TAKEPHOTO=1002;
    public static final int RESULT_ALUMNI=1003;
    public static final int RESULT_CROP=1004;
    public static final int RESULT_UPDATELABELINFO=1005;
    public static final int RESULT_UPDATEPICINFO=1006;
    public static final int RESULT_UPDATEADDRESSINFO=1007;
    public static final int RESULT_UPDATETIMEINFO=1008;
    public static final int RESULT_UPDATEPAYTYPEINFO=1009;
    public static final int RESULT_QRCODE=1011;
    public static final int RESULT_OVERTIME=1012;
    public static final int RESULT_EVALUATE=1013;
    public static final int RESULT_PAY=1014;

    // 当前城市
    public static String CITY=null;
}
