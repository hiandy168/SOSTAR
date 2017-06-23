package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SpannableStringUtils;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.bean.BindCashInfoRequest;
import com.renyu.sostar.bean.ChargeRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
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
 * Created by renyu on 2017/4/9.
 */

public class WithdrawalsActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ed_withdrawals_money)
    EditText ed_withdrawals_money;
    @BindView(R.id.tv_withdrawals_lastmoney)
    TextView tv_withdrawals_lastmoney;
    @BindView(R.id.ed_alipay_account)
    EditText ed_alipay_account;
    @BindView(R.id.ed_alipay_name)
    EditText ed_alipay_name;
    @BindView(R.id.ed_alipay_code)
    EditText ed_alipay_code;
    @BindView(R.id.btn_alipay_getvcode)
    Button btn_alipay_getvcode;
    @BindView(R.id.tv_withdrawals_protocal)
    TextView tv_withdrawals_protocal;

    Disposable vcode_disposable;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("账户提现");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_withdrawals_protocal.setMovementMethod(LinkMovementMethod.getInstance());
        tv_withdrawals_protocal.setText(new SpannableStringUtils.Builder()
                .append("请以实际到账时间为准  参见").append("《开工啦钱包使用规则》")
                .setForegroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(WithdrawalsActivity.this, WebActivity.class);
                        intent.putExtra("url", CommonParams.WealthProtocal);
                        startActivity(intent);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                }).create());
    }

    @Override
    public int initViews() {
        return R.layout.activity_withdrawals;
    }

    @Override
    public void loadData() {
        getRechargeInfo();
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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.btn_withdrawals_commit, R.id.btn_alipay_getvcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_withdrawals_commit:
                if (ed_alipay_account.isEnabled() && ed_alipay_name.isEnabled()) {
                    bindCashInfo();
                }
                else {
                    if (TextUtils.isEmpty(ed_withdrawals_money.getText().toString())) {
                        Toast.makeText(this, "请输入待提现的金额", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    charge();
                }
                break;
            case R.id.btn_alipay_getvcode:
                getVCode();
                break;
        }
    }

    private void getRechargeInfo() {
        EmployerCashAvaliableRequest request=new EmployerCashAvaliableRequest();
        EmployerCashAvaliableRequest.ParamBean paramBean=new EmployerCashAvaliableRequest.ParamBean();
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .rechargeInfo(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployerCashAvaliableResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmployerCashAvaliableResponse value) {
                dismissNetworkDialog();

                tv_withdrawals_lastmoney.setText("可用余额: "+value.getCashAvaiable());
                if (!TextUtils.isEmpty(value.getPayeeAccount())) {
                    ed_alipay_account.setText(value.getPayeeAccount());
                    ed_alipay_account.setEnabled(false);
                }
                if (!TextUtils.isEmpty(value.getPayeeRealName())) {
                    ed_alipay_name.setText(value.getPayeeRealName());
                    ed_alipay_name.setEnabled(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(WithdrawalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getVCode() {
        VCodeRequest request=new VCodeRequest();
        VCodeRequest.ParamBean paramBean=new VCodeRequest.ParamBean();
        paramBean.setPhone(ACache.get(this).getAsString(CommonParams.USER_PHONE));
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

                Toast.makeText(WithdrawalsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                btn_alipay_getvcode.setEnabled(false);
                vcode_disposable= Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                            btn_alipay_getvcode.setText(""+(60-aLong));
                            if (60-1-aLong==0) {
                                btn_alipay_getvcode.setEnabled(true);
                                btn_alipay_getvcode.setText("获取验证码");
                            }
                        });
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(WithdrawalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void bindCashInfo() {
        BindCashInfoRequest request=new BindCashInfoRequest();
        BindCashInfoRequest.ParamBean paramBean=new BindCashInfoRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        paramBean.setCaptcha(ed_alipay_code.getText().toString());
        paramBean.setPayeeAccount(ed_alipay_account.getText().toString());
        paramBean.setPayeeRealName(ed_alipay_name.getText().toString());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .bindCashInfo(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                charge();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(WithdrawalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void charge() {
        ChargeRequest request=new ChargeRequest();
        ChargeRequest.ParamBean paramBean=new ChargeRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        paramBean.setAmount(Integer.parseInt(ed_withdrawals_money.getText().toString()));
        paramBean.setCaptcha(ed_alipay_code.getText().toString());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .charge(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(WithdrawalsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(WithdrawalsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vcode_disposable!=null) {
            vcode_disposable.dispose();
        }
    }
}
