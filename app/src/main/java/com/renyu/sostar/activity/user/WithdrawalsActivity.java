package com.renyu.sostar.activity.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.R;
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
    @BindView(R.id.view_alipay_account)
    View view_alipay_account;
    @BindView(R.id.layout_alipay_name)
    LinearLayout layout_alipay_name;
    @BindView(R.id.ed_alipay_name)
    EditText ed_alipay_name;
    @BindView(R.id.view_alipay_name)
    View view_alipay_name;
    @BindView(R.id.layout_alipay_code)
    LinearLayout layout_alipay_code;
    @BindView(R.id.ed_alipay_code)
    EditText ed_alipay_code;
    @BindView(R.id.btn_alipay_getvcode)
    Button btn_alipay_getvcode;

    Disposable disposable;
    Disposable network_disposable;
    Disposable vcode_disposable;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("账户提现");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_withdrawals;
    }

    @Override
    public void loadData() {
        getEmployerCashAvaliable();
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

    @OnClick({R.id.ib_nav_left, R.id.btn_withdrawals_commit, R.id.btn_alipay_getvcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_withdrawals_commit:
                charge();
                break;
            case R.id.btn_alipay_getvcode:
                getVCode();
                break;
        }
    }

    private void getEmployerCashAvaliable() {
        EmployerCashAvaliableRequest request=new EmployerCashAvaliableRequest();
        EmployerCashAvaliableRequest.ParamBean paramBean=new EmployerCashAvaliableRequest.ParamBean();
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getEmployerCashAvaiable(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployerCashAvaliableResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmployerCashAvaliableResponse value) {
                tv_withdrawals_lastmoney.setText("可用余额: "+value.getCashAvaiable());
            }

            @Override
            public void onError(Throwable e) {
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
                network_disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
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
        paramBean.setPayeeAccount(ed_alipay_account.getText().toString());
        paramBean.setPayeeRealName(ed_alipay_name.getText().toString());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .charge(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(WithdrawalsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

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
        if (network_disposable!=null) {
            network_disposable.dispose();
        }
    }
}
