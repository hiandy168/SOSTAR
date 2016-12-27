package com.renyu.sostar.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.Utils;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.utils.CommonUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

/**
 * Created by renyu on 2016/12/26.
 */

public class APPLike extends DefaultApplicationLike {

    public APPLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String processName=CommonUtils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getApplication().getPackageName())) {

            Utils.init(getApplication());

            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplication());
            strategy.setUploadProcess(processName == null || processName.equals(getApplication().getPackageName()));
            Bugly.init(getApplication(), CommonParams.BUGLY_APPID, true, strategy);

        }
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker(this);
    }
}
