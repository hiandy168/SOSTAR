package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.views.ClearEditText;
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
    @BindView(R.id.layout_signin_rootview)
    LinearLayout layout_signin_rootview;

    Disposable disposable=null;

    @Override
    public void initParams() {
        if (!TextUtils.isEmpty(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PHONE))) {
            signin_phone.setText(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PHONE));
        }
        if (!TextUtils.isEmpty(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PASSWORD))) {
            signin_pwd.setText(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PASSWORD));
        }
        // 软键盘监听
        getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    int previousKeyboardHeight = -1;

                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                        int displayHeight = rect.bottom - rect.top;
                        int height = getWindow().getDecorView().getHeight();
                        int keyboardHeight = height - displayHeight;
                        if (previousKeyboardHeight != keyboardHeight) {
                            boolean hide = (double) displayHeight / height > 0.8;
                            if (hide) {
                                layout_signin_rootview.scrollTo(0, 0);
                            }
                            else {
                                if (BarUtils.getNavBarHeight(SignInActivity.this)>0) {
                                    layout_signin_rootview.scrollTo(0, SizeUtils.dp2px(60)+BarUtils.getNavBarHeight(SignInActivity.this));
                                }
                                else {
                                    layout_signin_rootview.scrollTo(0, SizeUtils.dp2px(60));
                                }
                            }
                        }
                        previousKeyboardHeight = height;
                    }
                });
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

    @OnClick({R.id.btn_findpwd, R.id.btn_signin_signup, R.id.btn_signin_signin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_findpwd:
                Intent intent_findpwd=new Intent(SignInActivity.this, FindPasswordActivity.class);
                intent_findpwd.putExtra("phone", signin_phone.getText().toString());
                startActivityForResult(intent_findpwd, CommonParams.RESULT_MODIFY_PWD);
                break;
            case R.id.btn_signin_signup:
                Intent intent_signup=new Intent(this, SignInSignUpActivity.class);
                intent_signup.putExtra(CommonParams.FROM, CommonParams.SIGNUP);
                intent_signup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_signup);
                break;
            case R.id.btn_signin_signin:
                signin();
                break;
        }
    }

    private void signin() {
        SigninRequest request=new SigninRequest();
        SigninRequest.ParamBean paramBean=new SigninRequest.ParamBean();
        paramBean.setPassword(signin_pwd.getText().toString());
        paramBean.setPhone(signin_phone.getText().toString());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .signin(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<SigninResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(SigninResponse value) {
                ACache.get(SignInActivity.this).put(CommonParams.USER_PHONE, signin_phone.getText().toString());
                ACache.get(SignInActivity.this).put(CommonParams.USER_PASSWORD, signin_pwd.getText().toString());
                ACache.get(SignInActivity.this).put(CommonParams.USER_ID, value.getUserId());
                // 如果没有用户身份类型，进入选择身份类型页面
                if (TextUtils.isEmpty(value.getUserType()) || value.getUserType().equals("-1")) {
                    Intent intent_sisu=new Intent(SignInActivity.this, SignInSignUpActivity.class);
                    intent_sisu.putExtra(CommonParams.FROM, CommonParams.CUSTOMER_STATE);
                    intent_sisu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent_sisu);
                }
                else {
                    ACache.get(SignInActivity.this).put(CommonParams.USER_TYPE, value.getUserType());
                    Intent intent_sisu=new Intent(SignInActivity.this, SignInSignUpActivity.class);
                    intent_sisu.putExtra(CommonParams.FROM, CommonParams.INDEX);
                    intent_sisu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent_sisu);
                }
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
            signin_phone.setText(ACache.get(SignInActivity.this).getAsString(CommonParams.USER_PHONE));
            signin_pwd.setText(ACache.get(this).getAsString(CommonParams.USER_PASSWORD));
        }
    }
}
