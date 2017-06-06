package com.renyu.sostar.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alipay.euler.andfix.patch.PatchManager;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.meituan.android.walle.WalleChannelReader;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.imagelibrary.commonutils.ImagePipelineConfigUtils;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.params.CommonParams;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by renyu on 2016/12/26.
 */

public class SostarApp extends MultiDexApplication {

    public PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();

        String processName= Utils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getPackageName())) {

            Retrofit2Utils.getInstance(CommonParams.BaseUrl);

            com.blankj.utilcode.util.Utils.init(this);

            // 设置开发设备
            CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
            String channel = WalleChannelReader.getChannel(this);
            strategy.setAppChannel(channel);
            strategy.setAppVersion(BuildConfig.VERSION_NAME);
            strategy.setAppPackageName(getPackageName());
            // 设置是否为上报进程
            strategy.setUploadProcess(processName == null || processName.equals(getPackageName()));
            // 设置自定义Map参数
            CrashReport.putUserData(this, "VERSION_CODE", ""+BuildConfig.VERSION_CODE);
            CrashReport.initCrashReport(this, CommonParams.BUGLY_APPID, true, strategy);

            // 初始化fresco
            Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));

            // AndFix初始化
            mPatchManager = new PatchManager(this);
            mPatchManager.init(BuildConfig.VERSION_NAME);

            // 百度地图初始化
            SDKInitializer.initialize(this);

            // 初始化JPush
            JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
            JPushInterface.init(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
