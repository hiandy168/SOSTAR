package com.renyu.sostar.activity.sign;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.views.ProgressCircleView;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.index.MainActivity;
import com.renyu.sostar.application.SostarApp;
import com.renyu.sostar.params.CommonParams;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2016/12/27.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash)
    SimpleDraweeView iv_splash;
    @BindView(R.id.tv_splash_version)
    TextView tv_splash_version;
    @BindView(R.id.pcv_splash_jump)
    ProgressCircleView pcv_splash_jump;

    // 跳转动画
    ValueAnimator valueAnimator;

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
                FileUtils.createOrExistsDir(InitParams.IMAGE_PATH);
                FileUtils.createOrExistsDir(InitParams.HOTFIX_PATH);
                FileUtils.createOrExistsDir(InitParams.FILE_PATH);
                FileUtils.createOrExistsDir(InitParams.LOG_PATH);

                if (ACache.get(SplashActivity.this).getAsString("hotfix_version")!=null &&
                        !ACache.get(SplashActivity.this).getAsString("hotfix_version").equals(BuildConfig.VERSION_NAME)) {
                    // 删除老版本的热修复补丁
                    FileUtils.deleteFilesInDir(InitParams.HOTFIX_PATH);
                    // 更新热修复补丁版本
                    ACache.get(SplashActivity.this).put("hotfix_version", BuildConfig.VERSION_NAME);
                }
                // 加载热修复补丁
                ((SostarApp) getApplication()).mPatchManager.loadPatch();
                List<File> hotfixs = FileUtils.listFilesInDir(InitParams.HOTFIX_PATH);
                if (hotfixs!=null) {
                    for (File hotfix : hotfixs) {
                        try {
                            ((SostarApp) getApplication()).mPatchManager.addPatch(hotfix.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                valueAnimator=ValueAnimator.ofInt(100, 0);
                valueAnimator.setDuration(3000);
                valueAnimator.addUpdateListener(animation -> pcv_splash_jump.setText("跳过", (int) (100-animation.getAnimatedFraction()*100)));
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        jump();
                    }
                });
                valueAnimator.start();
            }

            @Override
            public void denied() {
                finish();
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
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 冷启动优化方案：在加载布局视图前，将主题修改回来
        setTheme(R.style.AppTheme);
        BarUtils.setDark(this);
        // 如果程序已经打开则不进入启动页，直接显示之前的界面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            isNeedOnCreate=false;
            super.onCreate(savedInstanceState);
            finish();
        }
        else {
            super.onCreate(savedInstanceState);
        }
    }

    @OnClick({R.id.pcv_splash_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pcv_splash_jump:
                if (valueAnimator!=null) {
                    valueAnimator.cancel();
                }
                break;
        }
    }

    private void jump() {
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
    }

    @Override
    public void onBackPressed() {

    }
}
