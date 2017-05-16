package com.renyu.sostar.params;

/**
 * Created by renyu on 2016/12/26.
 */

public class CommonParams {

    // 腾讯bugly appId
    public static final String BUGLY_APPID="95c4c316f5";
    // 微信支付
    public static final String WX_APP_ID="";

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

    // 基础url
    public static final String BaseUrl="http://106.15.46.51:8080/";
    // 上传url
    public static final String UploadUrl="http://106.15.46.105:9333/";
    // 图片url
    public static final String ImageUrl="http://106.15.46.105:8081/";
    // 钱包协议url
    public static final String WealthProtocal=BaseUrl+"sostar/protocal/moneyprotocal.html";
    // 服务协议url
    public static final String ServiceProtocal=BaseUrl+"sostar/protocal/serviceprotocal.html";
    // 联系我们url
    public static final String contactUs=BaseUrl+"sostar/about/contact.html";
    // 版本信息url
    public static final String versionUrl=BaseUrl+"sostar/about/versioninfo.html";
}
