package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.view.View;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.index.MainActivity;
import com.renyu.sostar.params.CommonParams;

import butterknife.OnClick;

/**
 * Created by renyu on 2017/2/21.
 */

public class SignInSignUpActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_signinsignup;
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

    @OnClick({R.id.btn_signinsignup_signin, R.id.btn_signinsignup_signup, R.id.btn_signinsignup_nosignin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signinsignup_signin:
                startActivity(new Intent(SignInSignUpActivity.this, SignInActivity.class));
                break;
            case R.id.btn_signinsignup_signup:
                startActivity(new Intent(SignInSignUpActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_signinsignup_nosignin:
                startActivity(new Intent(SignInSignUpActivity.this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra(CommonParams.FROM, -1)==CommonParams.INDEX) {
            startActivity(new Intent(SignInSignUpActivity.this, MainActivity.class));
        }
        if (intent.getIntExtra(CommonParams.FROM, -1)==CommonParams.CUSTOMER_STATE) {
            startActivity(new Intent(SignInSignUpActivity.this, CustomerStateActivity.class));
        }
        if (intent.getIntExtra(CommonParams.FROM, -1)==CommonParams.SIGNIN) {
            startActivity(new Intent(SignInSignUpActivity.this, SignInActivity.class));
        }
        if (intent.getIntExtra(CommonParams.FROM, -1)==CommonParams.SIGNUP) {
            startActivity(new Intent(SignInSignUpActivity.this, SignUpActivity.class));
        }
        if (intent.getIntExtra(CommonParams.FROM, -1)==CommonParams.FINISH) {
            finish();
        }
    }
}
