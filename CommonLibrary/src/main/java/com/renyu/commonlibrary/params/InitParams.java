package com.renyu.commonlibrary.params;

import android.os.Environment;

import java.io.File;

/**
 * Created by renyu on 2017/4/19.
 */

public class InitParams {
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
    public static final String LOG_NAME= "sostar_log";
    // 缓存目录
    public static final String CACHE_PATH= ROOT_PATH + File.separator + "cache";
    // fresco缓存目录
    public static final String FRESCO_CACHE_NAME= "fresco_cache";
}
