package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;

import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/17.
 */

public class UpdateEmployeeInfoActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_updateemployeeinfo;
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

    @OnClick({R.id.updateemployeeinfo_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateemployeeinfo_commit:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
        }
    }
}
