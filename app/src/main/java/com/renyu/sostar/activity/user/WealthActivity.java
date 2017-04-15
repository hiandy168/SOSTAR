package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.RechargeInfoResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/4/9.
 */

public class WealthActivity extends BaseActivity {

    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.layout_wealth_top)
    RelativeLayout layout_wealth_top;
    @BindView(R.id.tv_wealth_tip)
    TextView tv_wealth_tip;
    @BindView(R.id.tv_wealth_recharge)
    TextView tv_wealth_recharge;
    @BindView(R.id.tv_wealth_billing)
    TextView tv_wealth_billing;
    @BindView(R.id.swipy_wealth)
    SwipyRefreshLayout swipy_wealth;
    @BindView(R.id.rv_wealth)
    RecyclerView rv_wealth;
    @BindView(R.id.tv_wealth_money)
    TextView tv_wealth_money;

    Disposable disposable;

    @Override
    public void initParams() {
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);
        tv_nav_title.setText("钱包");
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_right.setText("规则");
        tv_nav_right.setTextColor(Color.WHITE);
        BarUtils.adjustStatusBar(this, (ViewGroup) layout_wealth_top.getChildAt(1), ContextCompat.getColor(this, R.color.colorPrimary));
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            tv_wealth_tip.setText("可用余额不包括冻结金额");
        }
        else {
            tv_wealth_tip.setText("预计下笔订单收入为 0");
            tv_wealth_recharge.setVisibility(View.GONE);
            tv_wealth_billing.setVisibility(View.GONE);
        }
        swipy_wealth.setOnRefreshListener(direction -> {
            if (direction==SwipyRefreshLayoutDirection.TOP) {

            }
            else if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

            }
        });
        swipy_wealth.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        rv_wealth.setHasFixedSize(true);
        rv_wealth.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public int initViews() {
        return R.layout.activity_wealth;
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
    protected void onResume() {
        super.onResume();
        getRechargeInfo();
    }

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right, R.id.tv_wealth_recharge, R.id.tv_wealth_withdrawals,
            R.id.tv_wealth_billing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_nav_right:
                break;
            case R.id.tv_wealth_recharge:
                startActivity(new Intent(WealthActivity.this, RechargeActivity.class));
                break;
            case R.id.tv_wealth_withdrawals:
                startActivity(new Intent(WealthActivity.this, WithdrawalsActivity.class));
                break;
            case R.id.tv_wealth_billing:
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
                .compose(Retrofit2Utils.background()).subscribe(new Observer<RechargeInfoResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(RechargeInfoResponse value) {
                tv_wealth_money.setText(""+value.getCashAvaiable());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
