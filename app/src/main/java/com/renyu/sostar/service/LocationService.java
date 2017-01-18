package com.renyu.sostar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by renyu on 2017/1/18.
 */

public class LocationService extends Service {

    LocationClient mLocClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(bdLocation -> {
            if (bdLocation == null || bdLocation == null) {
                return;
            }
            Log.d("LocationService", bdLocation.getLongitude() + " " + bdLocation.getLatitude());
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(60000); // 1分钟定位一下
        option.setAddrType("all");
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
    }
}
