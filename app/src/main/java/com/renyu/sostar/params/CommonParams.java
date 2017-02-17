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

    public static final String USER_PHONE="user_phone";
    public static final String USER_PASSWORD="user_password";
    // 启动页跳转流程
    public static final String FROM="from";
    // 跳转到首页
    public static final int INDEX=1;
    // 跳转到用户身份
    public static final int CUSTOMER_STATE=2;

    public static final int RESULT_MODIFY_PWD=1000;
    public static final int RESULT_SIGNUP=1001;
    public static final int RESULT_SPLASH=1002;
    public static final int RESULT_UPDATEUSREINFO=1003;
}
