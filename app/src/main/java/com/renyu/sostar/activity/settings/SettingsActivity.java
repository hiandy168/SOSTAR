package com.renyu.sostar.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.sign.SignInSignUpActivity;
import com.renyu.sostar.params.CommonParams;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/27.
 */

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_settings_cache)
    TextView tv_settings_cache;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("通用设置");
    }

    @Override
    public int initViews() {
        return R.layout.activity_settings;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.layout_settings_cache, R.id.layout_settings_rulechange,
            R.id.btn_settings_sign_out, R.id.ib_nav_left, R.id.layout_settings_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_settings_cache:
                Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT).show();
                Fresco.getImagePipeline().clearCaches();
                tv_settings_cache.setText("0MB");
                break;
            case R.id.layout_settings_rulechange:
                break;
            case R.id.btn_settings_sign_out:
                ACache.get(this).remove(CommonParams.USER_SIGNIN);
                Intent intent=new Intent(this, SignInSignUpActivity.class);
                intent.putExtra(CommonParams.FROM, CommonParams.FINISH);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_settings_feedback:
                startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
                break;
        }
    }

    private String showCacheSize() {
        File dir=new File(CommonParams.FRESCO_CACHE_PATH + File.separator + CommonParams.FRESCO_CACHE_NAME);
        long cacheSize = FileUtils.getDirLength(dir);
        if (1< cacheSize && cacheSize<1024) {
            return cacheSize+"B";
        }
        else if (1024 <cacheSize &&cacheSize<1024*1024) {
            return cacheSize/1024+"KB";
        }
        else if (1024*1024 <cacheSize &&cacheSize<1024*1024*1024) {
            return cacheSize/1024/1024+"MB";
        }
        else {
            return "0MB";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_settings_cache.setText(showCacheSize());
    }
}
