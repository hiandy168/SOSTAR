package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
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
import com.renyu.sostar.bean.OrderMessageRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/24.
 */

public class OrderBroadcastActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.et_orderbroadcast)
    EditText et_orderbroadcast;
    @BindView(R.id.tv_orderbroadcast_numbers)
    TextView tv_orderbroadcast_numbers;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("发送广播");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderbroadcast;
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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.btn_orderbroadcast})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_orderbroadcast:
                setMessage();
                break;
        }
    }

    @OnTextChanged(value = R.id.et_orderbroadcast, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length()>50) {
            et_orderbroadcast.setText(s.toString().substring(0, 50));
            et_orderbroadcast.setSelection(50);
            tv_orderbroadcast_numbers.setText("50/50");
        }
        else {
            tv_orderbroadcast_numbers.setText(s.length()+"/50");
        }
    }

    private void setMessage() {
        OrderMessageRequest request=new OrderMessageRequest();
        OrderMessageRequest.ParamBean paramBean=new OrderMessageRequest.ParamBean();
        paramBean.setMsg(et_orderbroadcast.getText().toString());
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setReceiverId("");
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setMsg(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(OrderBroadcastActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderBroadcastActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
