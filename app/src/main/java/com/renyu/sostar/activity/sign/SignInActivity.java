package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.views.ClearEditText;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.SigninRequest;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/13.
 */

public class SignInActivity extends BaseActivity {

    @BindView(R.id.signin_phone)
    ClearEditText signin_phone;
    @BindView(R.id.signin_pwd)
    ClearEditText signin_pwd;

    @Override
    public void initParams() {
        if (!TextUtils.isEmpty(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PHONE))) {
            signin_phone.setText(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PHONE));
        }
        if (!TextUtils.isEmpty(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PASSWORD))) {
            signin_pwd.setText(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PASSWORD));
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_signin;
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

    @OnClick({R.id.signin_findpwd, R.id.signin_signup, R.id.signin_signin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_findpwd:
                Intent intent_findpwd=new Intent(SignInActivity.this, FindPasswordActivity.class);
                intent_findpwd.putExtra("phone", signin_phone.getText().toString());
                startActivityForResult(intent_findpwd, CommonParams.RESULT_MODIFY_PWD);
                break;
            case R.id.signin_signup:
                Intent intent_signup=new Intent(SignInActivity.this, SignUpActivity.class);
                intent_signup.putExtra("phone", signin_phone.getText().toString());
                startActivityForResult(intent_signup, CommonParams.RESULT_SIGNUP);
                break;
            case R.id.signin_signin:
                signin();
                break;
        }
    }

    private void signin() {
        SigninRequest request=new SigninRequest();
        request.setDeviceId(Utils.getUniquePsuedoID());
        request.setVer(""+BuildConfig.VERSION_CODE);
        SigninRequest.ParamBean paramBean=new SigninRequest.ParamBean();
        paramBean.setPassword(signin_pwd.getText().toString());
        paramBean.setPhone(signin_phone.getText().toString());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .signin(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<SigninResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SigninResponse value) {
                Intent intent_splash=new Intent(SignInActivity.this, SplashActivity.class);
                intent_splash.putExtra("state", 1);
                intent_splash.putExtra(CommonParams.FROM, CommonParams.INDEX);
                intent_splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_splash);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonParams.RESULT_MODIFY_PWD && resultCode == RESULT_OK) {
            signin_pwd.setText(ACache.get(this).getAsString(CommonParams.USER_PASSWORD));
        }
    }
}
