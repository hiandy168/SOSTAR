package com.renyu.sostar.activity.sign;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.index.MainActivity;
import com.renyu.sostar.application.SostarApp;
import com.renyu.sostar.params.CommonParams;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by renyu on 2016/12/27.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash)
    SimpleDraweeView iv_splash;
    @BindView(R.id.tv_splash_version)
    TextView tv_splash_version;

    @Override
    public void initParams() {
        tv_splash_version.setText("VERSION："+BuildConfig.VERSION_NAME);
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
                FileUtils.createOrExistsDir(CommonParams.LOG_PATH);

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
                    // 判断当前用户是否已经登录，没有登录去登录注册页
                    if (TextUtils.isEmpty(ACache.get(SplashActivity.this).getAsString(CommonParams.USER_ID))) {
                        ACache.get(SplashActivity.this).remove(CommonParams.USER_PHONE);
                        ACache.get(SplashActivity.this).remove(CommonParams.USER_PASSWORD);
                        ACache.get(SplashActivity.this).remove(CommonParams.USER_TYPE);
                        ACache.get(SplashActivity.this).remove(CommonParams.USER_ID);
                        startActivity(new Intent(SplashActivity.this, SignInSignUpActivity.class));
                    }
                    else {
                        Intent[] intents=new Intent[2];
                        // 注销过，需要重新登录
                        if (TextUtils.isEmpty(ACache.get(SplashActivity.this).getAsString(CommonParams.USER_SIGNIN))) {
                            startActivity(new Intent(SplashActivity.this, SignInSignUpActivity.class));
                        }
                        else {
                            // 如果没有用户身份类型，进入选择身份类型页面
                            if (TextUtils.isEmpty(ACache.get(SplashActivity.this).getAsString(CommonParams.USER_TYPE))) {
                                intents[0]=new Intent(SplashActivity.this, SignInSignUpActivity.class);
                                intents[1]=new Intent(SplashActivity.this, CustomerStateActivity.class);
                                startActivities(intents);
                            }
                            else {
                                intents[0]=new Intent(SplashActivity.this, SignInSignUpActivity.class);
                                intents[1]=new Intent(SplashActivity.this, MainActivity.class);
                                startActivities(intents);
                            }
                        }
                    }
                    finish();
                });

                openLog(CommonParams.LOG_PATH);
            }

            @Override
            public void denied() {
                finish();
            }
        });
        openLog(CommonParams.LOG_PATH);
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
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }
}
