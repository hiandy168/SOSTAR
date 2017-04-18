package com.renyu.sostar.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.OverTimeRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/22.
 */

public class OverTimeActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.tv_overtime_time)
    TextView tv_overtime_time;
    @BindView(R.id.tv_overtime_money)
    TextView tv_overtime_money;
    @BindView(R.id.tv_overtime_num)
    TextView tv_overtime_num;
    @BindView(R.id.tv_overtime_allmoney)
    TextView tv_overtime_allmoney;
    @BindView(R.id.tv_overtime_needmoney)
    TextView tv_overtime_needmoney;
    @BindView(R.id.tv_overtime_avaliablemoney)
    TextView tv_overtime_avaliablemoney;

    OrderResponse orderResponse;

    Disposable disposable;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("申请加班");

        orderResponse= (OrderResponse) getIntent().getSerializableExtra("params");
        tv_overtime_num.setText(""+orderResponse.getOkStaffAccount());
    }

    @Override
    public int initViews() {
        return R.layout.activity_overtime;
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

    @OnClick({R.id.ib_nav_left, R.id.layout_overtime_time, R.id.layout_overtime_money, R.id.btn_overtime_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_overtime_time:
                Intent intent_time=new Intent(OverTimeActivity.this, UpdateTextInfoActivity.class);
                intent_time.putExtra("title", "加班时长");
                intent_time.putExtra("param", "time");
                intent_time.putExtra("needcommit", false);
                intent_time.putExtra("source",
                        tv_overtime_time.getText().toString().substring(0, tv_overtime_time.getText().toString().indexOf("小时")));
                startActivityForResult(intent_time, CommonParams.RESULT_OVERTIME);
                break;
            case R.id.layout_overtime_money:
                Intent intent_money=new Intent(OverTimeActivity.this, UpdateTextInfoActivity.class);
                intent_money.putExtra("title", "加班报酬");
                intent_money.putExtra("param", "money");
                intent_money.putExtra("needcommit", false);
                intent_money.putExtra("source",
                        tv_overtime_money.getText().toString().substring(0, tv_overtime_money.getText().toString().indexOf("元")));
                startActivityForResult(intent_money, CommonParams.RESULT_OVERTIME);
                break;
            case R.id.btn_overtime_commit:
                setExtrawork();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==CommonParams.RESULT_OVERTIME) {
                if (data.getStringExtra("param").equals("time")) {
                    tv_overtime_time.setText(data.getStringExtra("value")+"小时");
                    caculateMoney();
                }
                if (data.getStringExtra("param").equals("money")) {
                    tv_overtime_time.setText(data.getStringExtra("value")+"元/小时");
                    caculateMoney();
                }
            }
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
                disposable=d;
            }

            @Override
            public void onNext(EmployerCashAvaliableResponse value) {
                tv_overtime_avaliablemoney.setText(""+value.getCashAvaiable());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void caculateMoney() {
        int time=Integer.parseInt(tv_overtime_time.getText().toString().substring(0, tv_overtime_time.getText().toString().indexOf("小时")));
        int money=Integer.parseInt(tv_overtime_money.getText().toString().substring(0, tv_overtime_money.getText().toString().indexOf("元")));
        int allMoney=time*money*orderResponse.getOkStaffAccount();
        tv_overtime_allmoney.setText(""+allMoney);
        tv_overtime_needmoney.setText(""+allMoney);
    }

    private void setExtrawork() {
        OverTimeRequest request=new OverTimeRequest();
        OverTimeRequest.ParamBean paramBean=new OverTimeRequest.ParamBean();
        paramBean.setOrderId(Integer.parseInt(orderResponse.getOrderId()));
        int time=Integer.parseInt(tv_overtime_time.getText().toString().substring(0, tv_overtime_time.getText().toString().indexOf("小时")));
        paramBean.setExtraTime(time);
        int money=Integer.parseInt(tv_overtime_money.getText().toString().substring(0, tv_overtime_money.getText().toString().indexOf("元")));
        paramBean.setExtraPrice(money);
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setExtrawork(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(OverTimeActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OverTimeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
