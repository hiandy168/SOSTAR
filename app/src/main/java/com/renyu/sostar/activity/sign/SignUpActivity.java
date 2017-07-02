package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.views.ClearEditText;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.bean.SignupRequest;
import com.renyu.sostar.bean.VCodeRequest;
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

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.signup_phone)
    ClearEditText signup_phone;
    @BindView(R.id.signup_vcode)
    ClearEditText signup_vcode;
    @BindView(R.id.signup_pwd)
    ClearEditText signup_pwd;
    @BindView(R.id.signup_rec)
    ClearEditText signup_rec;
    @BindView(R.id.btn_signup_getvcode)
    Button btn_signup_getvcode;
    @BindView(R.id.layout_signup_rootview)
    LinearLayout layout_signup_rootview;
    @BindView(R.id.btn_signup_protocal)
    TextView btn_signup_protocal;

    Disposable vcode_disposable;

    @Override
    public void initParams() {
        btn_signup_protocal.setMovementMethod(LinkMovementMethod.getInstance());
        btn_signup_protocal.setText(new SpanUtils()
                .append("点击注册即表示你同意").append("《开工啦用户服务协议》")
                .setForegroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(SignUpActivity.this, WebActivity.class);
                        intent.putExtra("url", CommonParams.ServiceProtocal);
                        startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                }).create());
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
                                layout_signup_rootview.scrollTo(0, 0);
                            }
                            else {
                                if (BarUtils.getNavBarHeight(SignUpActivity.this)>0) {
                                    layout_signup_rootview.scrollTo(0, SizeUtils.dp2px(60)+BarUtils.getNavBarHeight(SignUpActivity.this));
                                }
                                else {
                                    layout_signup_rootview.scrollTo(0, SizeUtils.dp2px(60));
                                }
                            }
                        }
                        previousKeyboardHeight = height;
                    }
                });
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
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_signup_getvcode, R.id.btn_signup, R.id.btn_signup_protocal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup_getvcode:
                getVCode();
                break;
            case R.id.btn_signup:
                signup();
                break;
            case R.id.btn_signup_protocal:
                startActivity(new Intent(this, ProtocalActivity.class));
                break;
        }
    }

    private void getVCode() {
        if (TextUtils.isEmpty(signup_phone.getText().toString())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
        else {
            VCodeRequest request=new VCodeRequest();
            VCodeRequest.ParamBean paramBean=new VCodeRequest.ParamBean();
            paramBean.setPhone(signup_phone.getText().toString());
            request.setParam(paramBean);
            retrofit.create(RetrofitImpl.class)
                    .getVcode(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    showNetworkDialog("正在操作，请稍后");
                }

                @Override
                public void onNext(EmptyResponse value) {
                    dismissNetworkDialog();

                    Toast.makeText(SignUpActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                    btn_signup_getvcode.setEnabled(false);
                    vcode_disposable= Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                                btn_signup_getvcode.setText(""+(60-aLong));
                                if (60-1-aLong==0) {
                                    btn_signup_getvcode.setEnabled(true);
                                    btn_signup_getvcode.setText("获取验证码");
                                }
                            });
                }

                @Override
                public void onError(Throwable e) {
                    dismissNetworkDialog();

                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private void signup() {
        if (TextUtils.isEmpty(signup_phone.getText().toString())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(signup_vcode.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(signup_pwd.getText().toString())) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(signup_rec.getText().toString())) {
            Toast.makeText(this, "请输入邀请码", Toast.LENGTH_SHORT).show();
        }
        else {
            SignupRequest request=new SignupRequest();
            SignupRequest.ParamBean paramBean=new SignupRequest.ParamBean();
            paramBean.setPhone(signup_phone.getText().toString());
            paramBean.setPassword(signup_pwd.getText().toString());
            paramBean.setCaptcha(signup_vcode.getText().toString());
            paramBean.setRecommend(signup_rec.getText().toString());
            request.setParam(paramBean);
            retrofit.create(RetrofitImpl.class)
                    .signup(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<SigninResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    showNetworkDialog("正在操作，请稍后");
                }

                @Override
                public void onNext(SigninResponse value) {
                    dismissNetworkDialog();

                    ACache.get(SignUpActivity.this).put(CommonParams.USER_PHONE, signup_phone.getText().toString());
                    ACache.get(SignUpActivity.this).put(CommonParams.USER_PASSWORD, signup_pwd.getText().toString());
                    ACache.get(SignUpActivity.this).put(CommonParams.USER_ID, value.getUserId());

                    Intent intent_sisu=new Intent(SignUpActivity.this, SignInSignUpActivity.class);
                    intent_sisu.putExtra(CommonParams.FROM, CommonParams.CUSTOMER_STATE);
                    intent_sisu.putExtra("rec", signup_rec.getText().toString());
                    intent_sisu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent_sisu);
                }

                @Override
                public void onError(Throwable e) {
                    dismissNetworkDialog();

                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
