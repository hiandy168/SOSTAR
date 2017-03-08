package com.renyu.imagelibrary.params;

import android.os.Environment;

import java.io.File;

/**
 * Created by renyu on 2016/12/26.
 */

public class CommonParams {

    // 项目根目录
    public static final String ROOT_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator + "sostar";
    // 项目图片目录
    public static final String IMAGECACHE= ROOT_PATH+ File.separator + "cache";

    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";


    public final static int RESULT_TAKECAMERA=1000;
    public final static int RESULT_PREVIEW=1001;
    public final static int RESULT_CROP=1002;
}
