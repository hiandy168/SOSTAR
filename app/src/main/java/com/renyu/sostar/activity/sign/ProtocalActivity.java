package com.renyu.sostar.activity.sign;

import android.support.v4.content.ContextCompat;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;

/**
 * Created by renyu on 2017/2/17.
 */

public class ProtocalActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_protocal;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
