package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.service.LocationService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/8.
 */

public class UpdateAddressInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.tv_updateaddressinfo_currentaddress)
    TextView tv_updateaddressinfo_currentaddress;
    @BindView(R.id.ed_updateaddressinfo_addaddress)
    EditText ed_updateaddressinfo_addaddress;
    @BindView(R.id.iv_updateaddressinfo_currentaddress)
    ImageView iv_updateaddressinfo_currentaddress;
    @BindView(R.id.iv_updateaddressinfo_addaddress)
    ImageView iv_updateaddressinfo_addaddress;

    int currentChoice=-1;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tv_updateaddressinfo_currentaddress.setText(LocationService.lastBdLocation.getAddrStr());

        ed_updateaddressinfo_addaddress.setText(getIntent().getStringExtra("source"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_updateaddressinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right, R.id.iv_updateaddressinfo_addaddress,
            R.id.iv_updateaddressinfo_currentaddress, R.id.tv_updateaddressinfo_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                if (currentChoice==-1) {
                    Toast.makeText(this, "请选择工作地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (currentChoice==1) {
                    Intent intent=new Intent();
                    intent.putExtra("value", tv_updateaddressinfo_currentaddress.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (currentChoice==2) {
                    if (TextUtils.isEmpty(ed_updateaddressinfo_addaddress.getText().toString())) {
                        Toast.makeText(this, "请填写工作地点", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent=new Intent();
                    intent.putExtra("value", ed_updateaddressinfo_addaddress.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.iv_updateaddressinfo_addaddress:
                iv_updateaddressinfo_addaddress.setImageResource(R.mipmap.ic_updateaddressinfo_sel);
                iv_updateaddressinfo_currentaddress.setImageResource(R.mipmap.ic_updateaddressinfo_nor);
                currentChoice=2;
                break;
            case R.id.iv_updateaddressinfo_currentaddress:
                iv_updateaddressinfo_currentaddress.setImageResource(R.mipmap.ic_updateaddressinfo_sel);
                iv_updateaddressinfo_addaddress.setImageResource(R.mipmap.ic_updateaddressinfo_nor);
                currentChoice=1;
                break;
            case R.id.tv_updateaddressinfo_refresh:
                tv_updateaddressinfo_currentaddress.setText(LocationService.lastBdLocation.getAddrStr());
                break;
        }
    }
}
