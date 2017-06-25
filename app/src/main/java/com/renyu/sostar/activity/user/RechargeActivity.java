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
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.alipay.AliPayActivity;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.bean.RechargeRequest;
import com.renyu.sostar.bean.RechargeResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/4/9.
 */

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_recharge_lastmoney)
    TextView tv_recharge_lastmoney;
    @BindView(R.id.ed_recharge_money)
    EditText ed_recharge_money;
    @BindView(R.id.tv_recharge_protocal)
    TextView tv_recharge_protocal;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("账户充值");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_recharge_protocal.setMovementMethod(LinkMovementMethod.getInstance());
        tv_recharge_protocal.setText(new SpannableStringUtils.Builder()
                .append("请以实际到账时间为准  参见").append("《开工啦钱包使用规则》")
                .setForegroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent=new Intent(RechargeActivity.this, WebActivity.class);
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
        return R.layout.activity_recharge;
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

    @OnClick({R.id.ib_nav_left, R.id.btn_recharge_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_recharge_commit:
                if (TextUtils.isEmpty(ed_recharge_money.getText().toString())) {
                    Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                recharge();
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

                tv_recharge_lastmoney.setText("可用余额: "+value.getCashAvaiable());
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void recharge() {
        RechargeRequest request=new RechargeRequest();
        RechargeRequest.ParamBean paramBean=new RechargeRequest.ParamBean();
        paramBean.setAmount(Integer.parseInt(ed_recharge_money.getText().toString()));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .recharge(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<RechargeResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(RechargeResponse value) {
                dismissNetworkDialog();

                Intent intent=new Intent(RechargeActivity.this, AliPayActivity.class);
                intent.putExtra("payinfo", value.getOrderInfo());
                startActivityForResult(intent, CommonParams.RESULT_PAY);
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(RechargeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==CommonParams.RESULT_PAY) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
    }
}
