package com.renyu.imagelibrary.commonutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.renyu.imagelibrary.camera.CameraActivity;
import com.renyu.imagelibrary.crop.CropActivity;
import com.renyu.imagelibrary.params.CommonParams;
import com.renyu.imagelibrary.photopicker.PhotoPickerActivity;

/**
 * Created by renyu on 2017/1/3.
 */

public class Utils {

    /**
     * 选择调用系统相册
     */
    public static void chooseImage(Activity activity) {
        //调用调用系统相册
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CommonParams.RESULT_CHOICEPIC);
    }

    /**
     * 照相
     */
    public static void takePicture(Activity activity) {
        Intent intent=new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(intent, CommonParams.RESULT_TAKECAMERA);
    }

    /**
     * 剪裁头像
     * @param path
     */
    public static void cropImage(String path, Activity activity) {
        Intent intent=new Intent(activity, CropActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("path", path);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, CommonParams.RESULT_CROP);
    }

    /**
     * 选择图片
     */
    public static void choicePic(Activity activity, int maxNum) {
        Intent intent=new Intent(activity, PhotoPickerActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("maxNum", maxNum);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, CommonParams.RESULT_ALUMNI);
    }
}
