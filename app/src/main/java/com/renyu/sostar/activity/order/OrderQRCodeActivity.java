package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.StartMyOrderSignResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by renyu on 2017/3/21.
 */

public class OrderQRCodeActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.iv_orderqrcode)
    ImageView iv_orderqrcode;
    @BindView(R.id.tv_orderqrcode_orderid)
    TextView tv_orderqrcode_orderid;
    @BindView(R.id.tv_orderqrcode_time)
    TextView tv_orderqrcode_time;
    @BindView(R.id.tv_orderqrcode_tip)
    TextView tv_orderqrcode_tip;

    Disposable d;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("扫码签到");
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);

        tv_orderqrcode_orderid.setText("订单编号  "+getIntent().getStringExtra("orderId"));
        tv_orderqrcode_time.setText("工作时间  "+getIntent().getStringExtra("startTime")+"-"+getIntent().getStringExtra("endTime"));
        String[] periodTime=getIntent().getStringExtra("periodTime").split(",");

        // 判断当前是不是开工时间
        boolean isWorking=false;

        long currentTime=System.currentTimeMillis();

        for (String s : periodTime) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                long startTime=format.parse(s+" "+getIntent().getStringExtra("startTime")).getTime();
                long endTime=format.parse(s+" "+getIntent().getStringExtra("endTime")).getTime();
                if (startTime<currentTime && currentTime<endTime) {
                    isWorking=true;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (isWorking) {
            tv_orderqrcode_tip.setText("提示：已经开工");
        }
        else {
            refreshTime();
            Observable.timer(1, TimeUnit.MINUTES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            OrderQRCodeActivity.this.d=d;
                        }

                        @Override
                        public void onNext(Long value) {
                            refreshTime();
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

    private void refreshTime() {
        String[] periodTime=getIntent().getStringExtra("periodTime").split(",");
        long currentTime1 =System.currentTimeMillis();
        for (String s : periodTime) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                long startTime=format.parse(s+" "+getIntent().getStringExtra("startTime")).getTime();
                if (currentTime1 <startTime) {
                    int minute= (int) ((startTime- currentTime1)/(1000*60));
                    tv_orderqrcode_tip.setText("提示：距离开工还有 "+minute+" 分钟\n请尽快提醒你的雇员扫码签到开工");
                    break;
                }
                else {
                    tv_orderqrcode_tip.setText("提示：已经开工");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderqrcode;
    }

    @Override
    public void loadData() {
        startMyOrderSign();
    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    private void startMyOrderSign() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .startMyOrderSign(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<StartMyOrderSignResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StartMyOrderSignResponse value) {
                Observable.just(value.getTag()).map(s -> QRCodeEncoder.syncEncodeQRCode(s,
                        BGAQRCodeUtil.dp2px(OrderQRCodeActivity.this, 210)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> iv_orderqrcode.setImageBitmap(bitmap));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderQRCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick({R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d!=null) {
            d.dispose();
        }
    }
}
