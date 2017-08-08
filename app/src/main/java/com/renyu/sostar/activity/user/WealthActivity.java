package com.renyu.sostar.activity.user;

import android.content.Intent;
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
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.adapter.WealthAdapter;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.bean.FlowResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    WealthAdapter adapter;
    @BindView(R.id.tv_wealth_money)
    TextView tv_wealth_money;

    List<Object> beans;

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        ib_nav_left.setImageResource(R.mipmap.ic_arrow_black_left);
        tv_nav_title.setText("钱包");
        tv_nav_title.setTextColor(ContextCompat.getColor(this, R.color.colorText1));
        tv_nav_right.setText("规则");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorText1));
        BarUtils.adjustStatusBar(this, (ViewGroup) layout_wealth_top.getChildAt(1), ContextCompat.getColor(this, R.color.colorPrimary));
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            tv_wealth_tip.setText("可用余额不包括冻结金额");
            tv_wealth_billing.setVisibility(View.GONE);
        }
        else {
            tv_wealth_tip.setText("预计下笔订单收入为 0元");
            tv_wealth_recharge.setVisibility(View.GONE);
            tv_wealth_billing.setVisibility(View.GONE);
        }
        swipy_wealth.setOnRefreshListener(direction -> {
            if (direction==SwipyRefreshLayoutDirection.TOP) {
                getFlowList();
            }
        });
        swipy_wealth.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        rv_wealth.setHasFixedSize(true);
        rv_wealth.setLayoutManager(new LinearLayoutManager(this));
        adapter=new WealthAdapter(this, beans);
        rv_wealth.setAdapter(adapter);
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
        getFlowList();
    }

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right, R.id.tv_wealth_recharge, R.id.tv_wealth_withdrawals,
            R.id.tv_wealth_billing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_nav_right:
                Intent intent=new Intent(WealthActivity.this, WebActivity.class);
                intent.putExtra("url", CommonParams.WealthProtocal);
                startActivity(intent);
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
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployerCashAvaliableResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmployerCashAvaliableResponse value) {
                dismissNetworkDialog();

                tv_wealth_money.setText(Utils.removeZero(value.getCashAvaiable()));
                if (ACache.get(WealthActivity.this).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    tv_wealth_tip.setText("预计下笔订单收入为 "+ Utils.removeZero(value.getForcastCash())+" 元");
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getFlowList() {
        EmployerCashAvaliableRequest request=new EmployerCashAvaliableRequest();
        EmployerCashAvaliableRequest.ParamBean paramBean=new EmployerCashAvaliableRequest.ParamBean();
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .flowList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.backgroundList()).subscribe(new Observer<List<FlowResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<FlowResponse> value) {
                beans.clear();
                // 第一条记录的年份
                String lastYear="";
                // 第一条记录的月份
                String lastMonth="";
                for (int i = 0; i < value.size(); i++) {
                    SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");
                    SimpleDateFormat momnthFormat=new SimpleDateFormat("MM");
                    SimpleDateFormat timeFormat1=new SimpleDateFormat("MM月dd日");
                    SimpleDateFormat timeFormat2=new SimpleDateFormat("HH:mm");
                    Date date=new Date();
                    date.setTime(Long.parseLong(value.get(i).getDate()));
                    if (i==0) {

                    }
                    else {
                        String currentYear=yearFormat.format(date);
                        String currentMonth=momnthFormat.format(date);
                        // 如果当前年份与月份都相同，则归并添加
                        if (currentYear.equals(lastYear) && currentMonth.equals(lastMonth)) {

                        }
                        // 如果当前年份相同，月份不同，则添加显示空行
                        else if (currentYear.equals(lastYear) && !currentMonth.equals(lastMonth)) {
                            beans.add("");
                        }
                        // 如果当前年份与月份都不相同，则添加显示年份
                        else if (currentYear.equals(lastYear) && currentMonth.equals(lastMonth)) {
                            beans.add(currentYear+"年");
                        }
                    }
                    lastYear=yearFormat.format(date);
                    lastMonth=momnthFormat.format(date);
                    FlowResponse response=value.get(i);
                    response.setDate(timeFormat1.format(date)+"\n"+timeFormat2.format(date));
                    beans.add(response);
                }
                adapter.notifyDataSetChanged();
                swipy_wealth.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipy_wealth.setRefreshing(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
