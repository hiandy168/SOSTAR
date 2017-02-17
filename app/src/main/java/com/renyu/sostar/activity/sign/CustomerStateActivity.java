package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/17.
 */

public class CustomerStateActivity extends BaseActivity {

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_customerstate;
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

    @OnClick({R.id.state_employer, R.id.state_employee})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.state_employer:
                gotoIndex();
                break;
            case R.id.state_employee:
                gotoIndex();
                break;
        }
    }

    private void gotoIndex() {
        Intent intent_splash=new Intent(this, SplashActivity.class);
        intent_splash.putExtra(CommonParams.FROM, CommonParams.INDEX);
        intent_splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent_splash);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }
}
