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

public class EmployeeInfoActivity extends BaseActivity {

    @BindView(R.id.nav_right_text)
    TextView nav_right_text;

    @Override
    public void initParams() {
        nav_right_text.setText("编辑");
    }

    @Override
    public int initViews() {
        return R.layout.activity_employeeinfo;
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

    @OnClick({R.id.nav_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_right_text:
                startActivityForResult(new Intent(EmployeeInfoActivity.this, UpdateEmployeeInfoActivity.class), CommonParams.RESULT_UPDATEUSREINFO);
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
