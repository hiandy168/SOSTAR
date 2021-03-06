package com.renyu.sostar.application;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.meituan.android.walle.WalleChannelReader;
import com.renyu.commonlibrary.commonutils.ImagePipelineConfigUtils;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.HttpsUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.params.CommonParams;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by renyu on 2016/12/26.
 */

public class SostarApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        String processName= Utils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getPackageName())) {
            // 初始化网络请求
            Retrofit2Utils retrofit2Utils=Retrofit2Utils.getInstance(CommonParams.BaseUrl);
            OkHttpClient.Builder baseBuilder=new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS);
            //https默认信任全部证书
            HttpsUtils.SSLParams sslParams= HttpsUtils.getSslSocketFactory(null, null, null);
            baseBuilder.hostnameVerifier((s, sslSession) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            retrofit2Utils.addBaseOKHttpClient(baseBuilder.build());
            retrofit2Utils.baseBuild();
            OkHttpClient.Builder imageUploadOkBuilder=new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS);
            retrofit2Utils.addImageOKHttpClient(imageUploadOkBuilder.build());
            retrofit2Utils.imageBuild();

            com.blankj.utilcode.util.Utils.init(this);

            // 初始化相关配置参数
            // 项目根目录
            // 请注意修改xml文件夹下filepaths.xml中的external-path节点，此值需与ROOT_PATH值相同，作为fileprovider使用
            InitParams.ROOT_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator + "sostar";
            // 项目图片目录
            InitParams.IMAGE_PATH= InitParams.ROOT_PATH + File.separator + "image";
            // 项目文件目录
            InitParams.FILE_PATH= InitParams.ROOT_PATH + File.separator + "file";
            // 项目热修复目录
            InitParams.HOTFIX_PATH= InitParams.ROOT_PATH + File.separator + "hotfix";
            // 项目日志目录
            InitParams.LOG_PATH= InitParams.ROOT_PATH + File.separator + "log";
            InitParams.LOG_NAME= "sostar_log";
            // 缓存目录
            InitParams.CACHE_PATH= InitParams.ROOT_PATH + File.separator + "cache";
            // fresco缓存目录
            InitParams.FRESCO_CACHE_NAME= "fresco_cache";

            // bugly
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

            // Sophix初始化
            SophixManager.getInstance().setContext(this)
                    .setAppVersion(BuildConfig.VERSION_NAME)
                    .setAesKey(null)
                    .setEnableDebug(true)
                    .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        }
                        else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            // SophixManager.getInstance().killProcessSafely();
                        }
                        else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            // SophixManager.getInstance().cleanPatches();
                        }
                        else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }).initialize();
            SophixManager.getInstance().queryAndLoadNewPatch();

            // 初始化fresco
            Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));

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
