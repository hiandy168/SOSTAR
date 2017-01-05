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

    // 回调参数
    public final static int RESULT_TAKECAMERA=1000;
    public final static int RESULT_CHOICEPIC=1001;
    public final static int RESULT_CROP=1002;
    public final static int RESULT_ALUMNI=1003;
}
