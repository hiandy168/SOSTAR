package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/17.
 */

public class EmployerInfoActivity extends BaseActivity {

    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;

    @Override
    public void initParams() {
        tv_nav_right.setText("编辑");
    }

    @Override
    public int initViews() {
        return R.layout.activity_employerinfo;
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

    @OnClick({R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                startActivityForResult(new Intent(EmployerInfoActivity.this, UpdateEmployerInfoActivity.class), CommonParams.RESULT_UPDATEUSREINFO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_UPDATEUSREINFO && resultCode==RESULT_OK) {

        }
    }
}
