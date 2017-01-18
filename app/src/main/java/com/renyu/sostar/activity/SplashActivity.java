package com.renyu.sostar.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.utils.FileUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.application.SostarApp;
import com.renyu.sostar.params.CommonParams;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by renyu on 2016/12/27.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public void initParams() {
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        checkPermission(permissions, "请授予SD卡读写权限与定位权限", new OnPermissionCheckedListener() {
            @Override
            public void checked(boolean flag) {

            }

            @Override
            public void grant() {
                // 初始化文件夹
                FileUtils.createOrExistsDir(CommonParams.IMAGE_PATH);
                FileUtils.createOrExistsDir(CommonParams.HOTFIX_PATH);
                FileUtils.createOrExistsDir(CommonParams.FILE_PATH);

                if (ACache.get(SplashActivity.this).getAsString("hotfix_version")!=null &&
                        !ACache.get(SplashActivity.this).getAsString("hotfix_version").equals(BuildConfig.VERSION_NAME)) {
                    // 删除老版本的热修复补丁
                    FileUtils.deleteFilesInDir(CommonParams.HOTFIX_PATH);
                    // 更新热修复补丁版本
                    ACache.get(SplashActivity.this).put("hotfix_version", BuildConfig.VERSION_NAME);
                }
                // 加载热修复补丁
                ((SostarApp) getApplication()).mPatchManager.loadPatch();
                List<File> hotfixs = FileUtils.listFilesInDir(CommonParams.HOTFIX_PATH);
                for (File hotfix : hotfixs) {
                    try {
                        ((SostarApp) getApplication()).mPatchManager.addPatch(hotfix.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 延时跳转
                Observable.timer(3, TimeUnit.SECONDS).subscribe(aLong -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                });
            }

            @Override
            public void denied() {

            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_splash;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimaryDark);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
