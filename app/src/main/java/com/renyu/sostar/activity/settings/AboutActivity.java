package com.renyu.sostar.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/5/11.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("关于开工啦");
    }

    @Override
    public int initViews() {
        return R.layout.activity_about;
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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.layout_contact_us, R.id.layout_about_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_about_version:
                Intent intent_version=new Intent(AboutActivity.this, WebActivity.class);
                intent_version.putExtra("title", "版本介绍");
                intent_version.putExtra("url", CommonParams.versionUrl);
                startActivity(intent_version);
                break;
            case R.id.layout_contact_us:
                Intent intent_contact=new Intent(AboutActivity.this, WebActivity.class);
                intent_contact.putExtra("title", "联系我们");
                intent_contact.putExtra("url", CommonParams.contactUs);
                startActivity(intent_contact);
                break;
        }
    }
}
