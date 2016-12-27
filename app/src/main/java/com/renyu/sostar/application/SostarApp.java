package com.renyu.sostar.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.utils.Utils;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.utils.CommonUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by renyu on 2016/12/26.
 */

public class SostarApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        String processName=CommonUtils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getPackageName())) {

            Utils.init(this);

            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
            strategy.setUploadProcess(processName == null || processName.equals(getPackageName()));
            Bugly.init(this, CommonParams.BUGLY_APPID, true, strategy);

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
