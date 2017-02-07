package com.renyu.imagelibrary.commonutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import com.renyu.imagelibrary.camera.CameraActivity;
import com.renyu.imagelibrary.crop.CropActivity;
import com.renyu.imagelibrary.photopicker.PhotoPickerActivity;

import java.io.File;

/**
 * Created by renyu on 2017/1/3.
 */

public class Utils {

    /**
     * 选择调用系统相册
     */
    public static void chooseImage(Activity activity, int requestCode) {
        //调用调用系统相册
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 照相
     */
    public static void takePicture(Activity activity, int requestCode) {
        Intent intent=new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 剪裁头像
     * @param path
     */
    public static void cropImage(String path, Activity activity, int requestCode) {
        Intent intent=new Intent(activity, CropActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("path", path);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片
     */
    public static void choicePic(Activity activity, int maxNum, int requestCode) {
        Intent intent=new Intent(activity, PhotoPickerActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("maxNum", maxNum);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照后刷新系统相册
     * @param context
     * @param newFile
     */
    public static void refreshAlbum(Context context, String newFile, String dirPath) {
        //刷新文件夹
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{dirPath}, null, null);
        }
        else {
            Intent scan_dir=new Intent(Intent.ACTION_MEDIA_MOUNTED);
            scan_dir.setData(Uri.fromFile(new File(dirPath)));
            context.sendBroadcast(scan_dir);
        }
        //刷新文件
        Intent intent_scan=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent_scan.setData(Uri.fromFile(new File(newFile)));
        context.sendBroadcast(intent_scan);
    }
}
