package com.renyu.commonlibrary.commonutils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by renyu on 2016/12/26.
 */

public class Utils {

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
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
