package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.views.ClearEditText;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.ResetPasswordRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/13.
 */

public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.et_findpwd_phone)
    ClearEditText et_findpwd_phone;
    @BindView(R.id.et_findpwd_vcode)
    ClearEditText et_findpwd_vcode;
    @BindView(R.id.et_findpwd_pwd)
    ClearEditText et_findpwd_pwd;
    @BindView(R.id.btn_findpwd_getvcode)
    Button btn_findpwd_getvcode;

    Disposable vcode_disposable;
    Disposable network_disposable;

    @Override
    public void initParams() {
        tv_nav_title.setText("找回密码");
        et_findpwd_phone.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_findpassword;
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

    @OnClick({R.id.findpwd_commit, R.id.btn_findpwd_getvcode, R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.findpwd_commit:
                resetPassword();
                break;
            case R.id.btn_findpwd_getvcode:
                btn_findpwd_getvcode.setEnabled(false);
                vcode_disposable=Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    btn_findpwd_getvcode.setText(""+(60-aLong));
                    if (60-1-aLong==0) {
                        btn_findpwd_getvcode.setEnabled(true);
                        btn_findpwd_getvcode.setText("获取验证码");
                    }
                });
                break;
            case R.id.ib_nav_left:
                finish();
        }
    }

    private void resetPassword() {
        if (TextUtils.isEmpty(et_findpwd_phone.getText().toString())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(et_findpwd_vcode.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(et_findpwd_pwd.getText().toString())) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
        }
        else {
            ResetPasswordRequest request=new ResetPasswordRequest();
            ResetPasswordRequest.ParamBean paramBean=new ResetPasswordRequest.ParamBean();
            paramBean.setPhone(et_findpwd_phone.getText().toString());
            paramBean.setCaptcha(et_findpwd_vcode.getText().toString());
            paramBean.setPassword(et_findpwd_pwd.getText().toString());
            request.setParam(paramBean);
            retrofit.create(RetrofitImpl.class)
                    .resetPwd(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    network_disposable=d;
                }

                @Override
                public void onNext(EmptyResponse value) {
                    Toast.makeText(FindPasswordActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                    ACache.get(FindPasswordActivity.this).put(CommonParams.USER_PASSWORD,
                            et_findpwd_pwd.getText().toString());
                    Intent intent=new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(FindPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vcode_disposable!=null) {
            vcode_disposable.dispose();
        }
    }
}
