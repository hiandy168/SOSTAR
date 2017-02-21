package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.layout_state_employer, R.id.layout_state_employee})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_state_employer:
                gotoIndex(1);
                break;
            case R.id.layout_state_employee:
                gotoIndex(2);
                break;
        }
    }

    private void gotoIndex(int state) {
        Intent intent_sisu=new Intent(this, SignInSignUpActivity.class);
        intent_sisu.putExtra(CommonParams.FROM, CommonParams.INDEX);
        intent_sisu.putExtra("state", state);
        intent_sisu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent_sisu);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }
}
