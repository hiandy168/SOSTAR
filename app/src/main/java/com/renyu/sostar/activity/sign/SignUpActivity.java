package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/13.
 */

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.signup_getvcode)
    Button signup_getvcode;

    Disposable disposable;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_signup;
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

    @OnClick({R.id.signup_getvcode, R.id.signup_signup, R.id.signup_signin, R.id.signup_protocal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_getvcode:
                signup_getvcode.setEnabled(false);
                disposable= Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                            signup_getvcode.setText(""+(60-aLong));
                            if (60-1-aLong==0) {
                                signup_getvcode.setEnabled(true);
                                signup_getvcode.setText("获取验证码");
                            }
                        });
                break;
            case R.id.signup_signup:
                Intent intent_splash=new Intent(this, SplashActivity.class);
                intent_splash.putExtra(CommonParams.FROM, CommonParams.CUSTOMER_STATE);
                intent_splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_splash);
                break;
            case R.id.signup_signin:
                finish();
                break;
            case R.id.signup_protocal:
                startActivity(new Intent(this, ProtocalActivity.class));
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null) {
            disposable.dispose();
        }
    }
}
