package com.renyu.sostar.activity.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.qrcodelibrary.ZBarQRScanActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.ReleaseOrderRequest;
import com.renyu.sostar.bean.StaffSignRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by renyu on 2017/3/10.
 */

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.layout_orderdetail_nav)
    LinearLayout layout_orderdetail_nav;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.ib_nav_right)
    ImageButton ib_nav_right;
    @BindView(R.id.vp_orderdetail)
    ViewPager vp_orderdetail;
    OrderDetailAdapter adapter;
    @BindView(R.id.indicator_orderdetail)
    CircleIndicator indicator_orderdetail;
    @BindView(R.id.tv_orderdetail_type)
    TextView tv_orderdetail_type;
    @BindView(R.id.tv_orderdetail_person)
    TextView tv_orderdetail_person;
    @BindView(R.id.tv_orderdetail_priceall)
    TextView tv_orderdetail_priceall;
    @BindView(R.id.tv_orderdetail_time)
    TextView tv_orderdetail_time;
    @BindView(R.id.tv_orderdetail_price)
    TextView tv_orderdetail_price;
    @BindView(R.id.tv_orderdetail_price_type)
    TextView tv_orderdetail_price_type;
    @BindView(R.id.iv_orderdetail_logo)
    SimpleDraweeView iv_orderdetail_logo;
    @BindView(R.id.tv_orderdetail_comp)
    TextView tv_orderdetail_comp;
    @BindView(R.id.tv_orderdetail_orderid)
    TextView tv_orderdetail_orderid;
    @BindView(R.id.tv_orderdetail_address)
    TextView tv_orderdetail_address;
    @BindView(R.id.tv_orderdetail_sex)
    TextView tv_orderdetail_sex;
    @BindView(R.id.tv_orderdetail_paytype)
    TextView tv_orderdetail_paytype;
    @BindView(R.id.tv_orderdetail_desp)
    TextView tv_orderdetail_desp;
    @BindView(R.id.layout_orderdetail_info3)
    LinearLayout layout_orderdetail_info3;
    @BindView(R.id.tv_orderdetail_employeenum)
    TextView tv_orderdetail_employeenum;
    @BindView(R.id.layout_orderdetail_employer)
    LinearLayout layout_orderdetail_employer;
    @BindView(R.id.btn_orderdetail_commit)
    Button btn_orderdetail_commit;
    @BindView(R.id.btn_orderdetail_cancel)
    Button btn_orderdetail_cancel;
    @BindView(R.id.tv_orderdetail_tip)
    TextView tv_orderdetail_tip;

    ArrayList<String> images;
    ArrayList<View> views;

    Disposable disposable;

    // 订单当前状态
    OrderResponse orderResponse;

    // 是否是未接单状态
    boolean isNotReceive=false;

    PopupWindow popupWindow;

    String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    public void initParams() {
        layout_orderdetail_nav.setBackgroundColor(Color.parseColor("#60000000"));
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("订单详情");
        ib_nav_right.setImageResource(R.mipmap.ic_order_more);
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);

        btn_orderdetail_commit.setVisibility(View.GONE);
        btn_orderdetail_cancel.setVisibility(View.GONE);
        tv_orderdetail_tip.setVisibility(View.GONE);

        BarUtils.adjustStatusBar(this, layout_orderdetail_nav, -1);

        images=new ArrayList<>();

        isNotReceive=getIntent().getBooleanExtra("typeIsCommit", false);

        EventBus.getDefault().register(this);
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderdetail;
    }

    @Override
    public void loadData() {
        getOrderDetail();
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    public class OrderDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initPop(View popView) {
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
    }

    @OnClick({R.id.ib_nav_left, R.id.ib_nav_right, R.id.layout_orderdetail_info3, R.id.btn_orderdetail_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.ib_nav_right:
                popupWindow.showAsDropDown(ib_nav_right, 0, -SizeUtils.dp2px(15));
                break;
            case R.id.layout_orderdetail_info3:
                Intent intent_employees=new Intent(OrderDetailActivity.this, EmployeeListActivity.class);
                intent_employees.putExtra("orderId", getIntent().getStringExtra("orderId"));
                startActivity(intent_employees);
                break;
            case R.id.btn_orderdetail_commit:
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    if (orderResponse.getOrderStatus().equals("-1")) {
                        receiveOrder();
                    }
                    else if (orderResponse.getOrderStatus().equals("1")) {
                        sign();
                    }
                    else if (orderResponse.getOrderStatus().equals("4") ||
                            orderResponse.getOrderStatus().equals("5") ||
                            orderResponse.getOrderStatus().equals("8") ||
                            orderResponse.getOrderStatus().equals("9") ||
                            orderResponse.getOrderStatus().equals("10") ||
                            orderResponse.getOrderStatus().equals("12") ||
                            orderResponse.getOrderStatus().equals("13") ||
                            orderResponse.getOrderStatus().equals("14")) {
                        Intent intent=new Intent(OrderDetailActivity.this, OrderProcessActivity.class);
                        intent.putExtra("params", orderResponse);
                        intent.putExtra("process", orderResponse.getOrderStatus());
                        startActivity(intent);
                    }
                }
                else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    if (orderResponse.getOrderStatus().equals("0")) {
                        Intent intent=new Intent(OrderDetailActivity.this, ReleaseOrderActivity.class);
                        intent.putExtra("value", orderResponse);
                        startActivity(intent);
                    }
                    else if (orderResponse.getOrderStatus().equals("2")) {
                        startOrder();
                    }
                    else if (orderResponse.getOrderStatus().equals("3") ||
                            orderResponse.getOrderStatus().equals("4") ||
                            orderResponse.getOrderStatus().equals("5") ||
                            orderResponse.getOrderStatus().equals("9")) {
                        Intent intent=new Intent(OrderDetailActivity.this, OrderProcessActivity.class);
                        intent.putExtra("params", orderResponse);
                        intent.putExtra("process", orderResponse.getOrderStatus());
                        startActivity(intent);
                    }
                }
        }
    }

    private void getOrderDetail() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        Observable observable=null;
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            if (isNotReceive) {
                observable=retrofit.create(RetrofitImpl.class)
                        .employeeOrderDetail(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                        .compose(Retrofit2Utils.background());
            }
            else {
                observable=retrofit.create(RetrofitImpl.class)
                        .staffOrderDetail(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                        .compose(Retrofit2Utils.background());
            }
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            observable=retrofit.create(RetrofitImpl.class)
                    .employeeOrderDetail(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background());
        }
        observable.subscribe(new Observer<OrderResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(OrderResponse value) {
                if (isNotReceive) {
                    value.setOrderStatus("-1");
                }
                orderResponse=value;

                // popupWindow的处理
                View popView = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.view_pop, null, false);
                LinearLayout layout_pop= (LinearLayout) popView.findViewById(R.id.layout_pop);

                tv_orderdetail_type.setText(value.getJobType());
                tv_orderdetail_person.setText(""+value.getStaffAccount());
                int dayNum=value.getPeriodTime().split(",").length;
                // 按天
                if (value.getUnitPriceType().equals("1")) {
                    tv_orderdetail_priceall.setText("总价"+dayNum*value.getUnitPrice()*dayNum);
                }
                // 按小时
                else if (value.getUnitPriceType().equals("2")) {
                    String startTime=value.getStartTime().split(":")[0]+value.getStartTime().split(":")[1];
                    String endTime=value.getEndTime().split(":")[0]+value.getEndTime().split(":")[1];
                    double timeNum=(Integer.parseInt(endTime)-Integer.parseInt(startTime))*1.0f/100;
                    tv_orderdetail_priceall.setText("总价"+dayNum*value.getUnitPrice()*dayNum*timeNum);
                }
                String times="";
                String[] timeTemps=value.getPeriodTime().split(",");
                for (String timeTemp : timeTemps) {
                    times+=timeTemp+"  "+value.getStartTime()+"-"+value.getEndTime()+"\n";
                }
                times=times.substring(0, times.length()-1);
                tv_orderdetail_time.setText(times);
                tv_orderdetail_price.setText(""+value.getUnitPrice());
                if (value.getUnitPriceType().equals("1")) {
                    tv_orderdetail_price_type.setText("/天");
                }
                else if (value.getUnitPriceType().equals("2")) {
                    tv_orderdetail_price_type.setText("/小时");
                }
                if (!TextUtils.isEmpty(value.getLogoPath())) {
                    DraweeController logoDraweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(value.getLogoPath())).setAutoPlayAnimations(true).build();
                    iv_orderdetail_logo.setController(logoDraweeController);
                }
                tv_orderdetail_comp.setText(value.getCompanyName());
                tv_orderdetail_orderid.setText("订单号:"+value.getOrderId());
                tv_orderdetail_address.setText(value.getAddress());
                if (value.getSex().equals("0")) {
                    tv_orderdetail_sex.setText("男女不限");
                }
                else if (value.getSex().equals("1")) {
                    tv_orderdetail_sex.setText("男");
                }
                else if (value.getSex().equals("2")) {
                    tv_orderdetail_sex.setText("女");
                }
                if (value.getPaymentType().equals("1")) {
                    tv_orderdetail_paytype.setText("日结");
                }
                else if (value.getPaymentType().equals("2")) {
                    tv_orderdetail_paytype.setText("订单结");
                }
                tv_orderdetail_desp.setText(value.getDescription());

                if (ACache.get(OrderDetailActivity.this).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    tv_orderdetail_person.setVisibility(View.GONE);
                    // 订单状态
                    // -1.未接单
                    if (value.getOrderStatus().equals("-1")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("接单");
                        tv_orderdetail_tip.setVisibility(View.VISIBLE);
                    }
                    // 0.未确认
                    else if (value.getOrderStatus().equals("0")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("待确认");
                        tv_orderdetail_tip.setVisibility(View.GONE);
                    }
                    // 1.已确认
                    else if (value.getOrderStatus().equals("1")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("立即签到");
                        layout_pop.addView(getPopupTextView("取消订单", v -> {
                            cancelEmployeeOrder();
                            popupWindow.dismiss();
                        }));
                    }
                    // 2.已被拒绝
                    else if (value.getOrderStatus().equals("2")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("已拒绝");
                    }
                    // 3.主动取消
                    else if (value.getOrderStatus().equals("3")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("已取消");
                    }
                    // 4.已完成(待支付)  5.已完成(已支付)  8.已开始  9.雇主取消定单
                    // 10.雇主解雇员工  12.离职  13.超过时间签到自动终止  14.雇主超时未开工订单自动取消
                    else if (value.getOrderStatus().equals("4") ||
                            value.getOrderStatus().equals("5") ||
                            value.getOrderStatus().equals("8") ||
                            value.getOrderStatus().equals("9") ||
                            value.getOrderStatus().equals("10") ||
                            value.getOrderStatus().equals("12") ||
                            value.getOrderStatus().equals("13") ||
                            value.getOrderStatus().equals("14")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("查看进度");
                        if (value.getOrderStatus().equals("8")) {
                            layout_pop.addView(getPopupTextView("申请离职", v -> {
                                staffApplyOff();
                                popupWindow.dismiss();
                            }));
                            layout_pop.addView(getPopupTextView("立即签到", v -> {
                                sign();
                                popupWindow.dismiss();
                            }));
                        }
                    }
                    // 11.申请离职中
                    else if (value.getOrderStatus().equals("11")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("申请离职中");
                    }
                }
                else if (ACache.get(OrderDetailActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    tv_orderdetail_priceall.setVisibility(View.GONE);
                    layout_orderdetail_info3.setVisibility(View.VISIBLE);
                    layout_orderdetail_employer.removeAllViews();
                    for (String s : value.getPicStaffArray()) {
                        View view= LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.adapter_orderdetail_employee, null, false);
                        DraweeController draweeController;
                        if (!TextUtils.isEmpty(s)) {
                            draweeController = Fresco.newDraweeControllerBuilder()
                                    .setUri(Uri.parse(s)).setAutoPlayAnimations(true).build();
                        }
                        else {
                            draweeController = Fresco.newDraweeControllerBuilder()
                                    .setUri(Uri.parse("res:///"+R.mipmap.ic_avatar_small)).setAutoPlayAnimations(true).build();
                        }
                        ((SimpleDraweeView) view).setController(draweeController);
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(SizeUtils.dp2px(30), SizeUtils.dp2px(30));
                        params.leftMargin= SizeUtils.dp2px(2);
                        params.rightMargin= SizeUtils.dp2px(2);
                        params.topMargin= SizeUtils.dp2px(2);
                        params.bottomMargin= SizeUtils.dp2px(2);
                        view.setLayoutParams(params);
                        layout_orderdetail_employer.addView(view);
                    }
                    tv_orderdetail_employeenum.setText(value.getOkStaffAccount()+"/"+value.getStaffAccount());
                    // 订单状态
                    // 已发单
                    if (value.getOrderStatus().equals("1")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        layout_pop.addView(getPopupTextView("取消订单", v -> {
                            cancleMyOrder();
                            popupWindow.dismiss();
                        }));
                    }
                    // 2.已成单  3.已开始  4.已过期  5.已完成  9.已取消
                    else if (value.getOrderStatus().equals("4") ||
                            value.getOrderStatus().equals("5") ||
                            value.getOrderStatus().equals("9") ||
                            value.getOrderStatus().equals("3") ||
                            value.getOrderStatus().equals("2")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        if (value.getOrderStatus().equals("2")) {
                            btn_orderdetail_commit.setText("签到开工");
                        }
                        else {
                            btn_orderdetail_commit.setText("查看进度");
                        }
                        if (value.getOrderStatus().equals("3")) {
                            layout_pop.addView(getPopupTextView("订单二维码", v -> {
                                showQRCode();
                                popupWindow.dismiss();
                            }));
                            layout_pop.addView(getPopupTextView("取消订单", v -> {
                                cancleMyOrder();
                                popupWindow.dismiss();
                            }));
                        }
                        if (value.getOrderStatus().equals("2")) {
                            layout_pop.addView(getPopupTextView("取消订单", v -> {
                                cancleMyOrder();
                                popupWindow.dismiss();
                            }));
                        }
                    }
                    // 草稿
                    else if (value.getOrderStatus().equals("0")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("重发");
                    }
                }
                views=new ArrayList<>();
                for (String image : value.getPicListArray()) {
                    View view= LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.adapter_orderdetail, null, false);
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(image)).setAutoPlayAnimations(true).build();
                    ((SimpleDraweeView) view).setController(draweeController);
                    views.add(view);
                }
                adapter=new OrderDetailAdapter();
                vp_orderdetail.setAdapter(adapter);
                indicator_orderdetail.setViewPager(vp_orderdetail);

                initPop(popView);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @NonNull
    private TextView getPopupTextView(String title, View.OnClickListener listener) {
        TextView textView=new TextView(OrderDetailActivity.this);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(Color.parseColor("#999999"));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(SizeUtils.dp2px(12), SizeUtils.dp2px(12), SizeUtils.dp2px(12), SizeUtils.dp2px(12));
        textView.setText(title);
        textView.setOnClickListener(listener);
        return textView;
    }

    // 雇主取消订单
    private void cancleMyOrder() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .cancleMyOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 雇员取消订单
    private void cancelEmployeeOrder() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .cancelEmployeeOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 接单
    private void receiveOrder() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .receiveOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                isNotReceive=false;
                getOrderDetail();

                // 雇员接单状态变化以刷新
                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 雇主签到开始
    private void startOrder() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .startMyOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                EventBus.getDefault().post(new OrderResponse());
                showQRCode();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 雇员签到开始
    private void staffSign(String value) {
        StaffSignRequest request=new StaffSignRequest();
        StaffSignRequest.ParamBean paramBean=new StaffSignRequest.ParamBean();
        paramBean.setTag(value);
        paramBean.setSignTime(""+new Date().getTime());
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .staffSign(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                getOrderDetail();

                Intent intent=new Intent(OrderDetailActivity.this, OrderProcessActivity.class);
                intent.putExtra("process", "1");
                startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 申请离职
    private void staffApplyOff() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .applyOff(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(OrderDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (requestCode==CommonParams.RESULT_QRCODE) {
                staffSign(data.getStringExtra("result"));
            }
        }
    }

    // 发单成功以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ReleaseOrderRequest request) {
        getOrderDetail();
    }

    // 支付状态发生变化以刷新
    // 员工状态变化以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderResponse response) {
        getOrderDetail();
    }

    // 显示二维码
    private void showQRCode() {
        Intent intent=new Intent(OrderDetailActivity.this, OrderQRCodeActivity.class);
        intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
        intent.putExtra("startTime", orderResponse.getStartTime());
        intent.putExtra("endTime", orderResponse.getEndTime());
        intent.putExtra("periodTime", orderResponse.getPeriodTime());
        startActivity(intent);
    }

    // 去签到
    private void sign() {
        checkPermission(permissions, getResources().getString(R.string.permission_camera), new OnPermissionCheckedListener() {
            @Override
            public void checked(boolean flag) {

            }

            @Override
            public void grant() {
                Intent intent=new Intent(OrderDetailActivity.this, ZBarQRScanActivity.class);
                intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                intent.putExtra("startTime", orderResponse.getStartTime());
                intent.putExtra("endTime", orderResponse.getEndTime());
                intent.putExtra("periodTime", orderResponse.getPeriodTime());
                startActivityForResult(intent, CommonParams.RESULT_QRCODE);
            }

            @Override
            public void denied() {

            }
        });
    }
}
