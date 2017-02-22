package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.wheelview.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.WheelViewUtils;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/17.
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("个人信息");
    }

    @Override
    public int initViews() {
        return R.layout.activity_userinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.layout_employeeinfo_age, R.id.layout_employeeinfo_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_employeeinfo_age:
                WheelViewUtils.showDate(UserInfoActivity.this.getSupportFragmentManager(),
                        "年龄",
                        "取消",
                        "完成",
                        value -> {
                            String string = value.toString();
                            String year = string.split("-")[0];
                            String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                            String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                            Log.d("UserInfoActivity", (year + "-" + month + "-" + day));
                        }, () -> {

                        }
                );
                break;
            case R.id.layout_employeeinfo_avatar:
                WheelViewUtils.showCamera(UserInfoActivity.this.getSupportFragmentManager(),
                        "设置头像", new String[]{"拍照", "从相册获取"},
                        position -> {
                            if (position==0) {
                                Utils.takePicture(UserInfoActivity.this, CommonParams.RESULT_TAKEPHOTO);
                            }
                            else if (position==1) {
                                Utils.choicePic(UserInfoActivity.this, 1, CommonParams.RESULT_ALUMNI);
                            }
                        }, "取消", () -> {

                        });
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {

        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {

        }
    }
}
