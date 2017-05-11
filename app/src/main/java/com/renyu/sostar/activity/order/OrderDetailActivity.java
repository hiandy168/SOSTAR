package com.renyu.sostar.activity.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.views.LocalImageHolderView;
import com.renyu.jpushlibrary.bean.NotificationBean;
import com.renyu.qrcodelibrary.ZBarQRScanActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.user.InfoActivity;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.ReleaseOrderRequest;
import com.renyu.sostar.bean.StaffSignRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    ConvenientBanner vp_orderdetail;
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

    // 订单当前状态
    OrderResponse orderResponse;

    // 是否是未接单状态
    boolean isNotReceive=false;

    PopupWindow popupWindow;

    String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    // 当前时间在哪个环节
    long lastStatueTime=-1;

    Disposable orderDisposable;

    @Override
    public void initParams() {
        layout_orderdetail_nav.setBackgroundColor(Color.parseColor("#60000000"));
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("订单详情");
        ib_nav_right.setImageResource(R.mipmap.ic_order_more);
        ib_nav_right.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();

        vp_orderdetail.startTurning(5000);

        needStateRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();

        vp_orderdetail.stopTurning();
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

    @OnClick({R.id.ib_nav_left, R.id.ib_nav_right, R.id.layout_orderdetail_info3, R.id.btn_orderdetail_commit,
            R.id.layout_orderdetail_employerinfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.ib_nav_right:
                if (popupWindow!=null)
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
                break;
            case R.id.layout_orderdetail_employerinfo:
                // 雇员点击后可以查看到雇主信息
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0") &&
                        orderResponse!=null) {
                    Intent intent=new Intent(OrderDetailActivity.this, InfoActivity.class);
                    intent.putExtra("userId", orderResponse.getCreateUserId());
                    if (orderResponse.getOrderStatus().equals("1") ||
                            orderResponse.getOrderStatus().equals("4") ||
                            orderResponse.getOrderStatus().equals("8") ||
                            orderResponse.getOrderStatus().equals("11")) {
                        intent.putExtra("canphone", true);
                    }
                    else {
                        intent.putExtra("canphone", false);
                    }
                    startActivity(intent);
                }
                break;
        }
    }

    private void getOrderDetail() {
        if (orderDisposable!=null && !orderDisposable.isDisposed()) {
            orderDisposable.dispose();
        }
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
                    .employerOrderDetail(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background());
        }
        observable.subscribe(new Observer<OrderResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                orderDisposable=d;
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
                tv_orderdetail_person.setText(""+value.getStaffAccount()+"人");
                int dayNum=value.getPeriodTime().split(",").length;
                // 按天
                if (value.getUnitPriceType().equals("1")) {
                    BigDecimal bigDecimal1 = new BigDecimal(""+dayNum);
                    BigDecimal bigDecimal2 = new BigDecimal(Double.toString(value.getUnitPrice()));
                    BigDecimal bigDecimal3 = new BigDecimal(""+value.getStaffAccount());
                    tv_orderdetail_priceall.setText("总价"+ Utils.removeZero(bigDecimal1.multiply(bigDecimal2).multiply(bigDecimal3).toString())+"元");
                }
                // 按小时
                else if (value.getUnitPriceType().equals("2")) {
                    try {
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String currentTime=format.format(new Date());
                        long startTime=format.parse(currentTime.split(" ")[0]+" "+value.getStartTime()).getTime();
                        long endTime=format.parse(currentTime.split(" ")[0]+" "+value.getEndTime()).getTime();
                        // 计算每天工作时间
                        double workTime=((double) (endTime-startTime)/1000)/3600;
                        BigDecimal bigDecimal1 = new BigDecimal(""+dayNum);
                        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(value.getUnitPrice()));
                        BigDecimal bigDecimal3 = new BigDecimal(""+value.getStaffAccount());
                        BigDecimal bigDecimal4 = new BigDecimal(""+workTime);
                        tv_orderdetail_priceall.setText("总价"+ Utils.removeZero(""+bigDecimal1.multiply(bigDecimal2).multiply(bigDecimal3).multiply(bigDecimal4))+"元");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                String times="";
                String[] timeTemps=value.getPeriodTime().split(",");
                for (String timeTemp : timeTemps) {
                    times+=timeTemp+"  "+value.getStartTime()+"-"+value.getEndTime()+"\n";
                }
                times=times.substring(0, times.length()-1);
                tv_orderdetail_time.setText(times);
                tv_orderdetail_price.setText(Utils.removeZero(""+value.getUnitPrice()));
                if (value.getUnitPriceType().equals("1")) {
                    tv_orderdetail_price_type.setText("元/天");
                }
                else if (value.getUnitPriceType().equals("2")) {
                    tv_orderdetail_price_type.setText("元/小时");
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
                    tv_orderdetail_priceall.setVisibility(View.GONE);
                    // 订单状态
                    // -1.未接单
                    if (value.getOrderStatus().equals("-1")) {
                        btn_orderdetail_cancel.setVisibility(View.GONE);
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("接单");
                        tv_orderdetail_tip.setVisibility(View.VISIBLE);
                        ib_nav_right.setVisibility(View.GONE);
                    }
                    // 0.未确认
                    else if (value.getOrderStatus().equals("0")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("待确认");
                        tv_orderdetail_tip.setVisibility(View.GONE);
                        ib_nav_right.setVisibility(View.GONE);
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
                        ib_nav_right.setVisibility(View.VISIBLE);
                    }
                    // 2.已被拒绝
                    else if (value.getOrderStatus().equals("2")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("已拒绝");
                        ib_nav_right.setVisibility(View.GONE);
                    }
                    // 3.主动取消
                    else if (value.getOrderStatus().equals("3")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("已取消");
                        ib_nav_right.setVisibility(View.GONE);
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
                            ib_nav_right.setVisibility(View.VISIBLE);
                        }
                        else {
                            ib_nav_right.setVisibility(View.GONE);
                        }
                    }
                    // 11.申请离职中
                    else if (value.getOrderStatus().equals("11")) {
                        btn_orderdetail_commit.setVisibility(View.GONE);
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("申请离职中");
                        ib_nav_right.setVisibility(View.GONE);
                    }
                }
                else if (ACache.get(OrderDetailActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    tv_orderdetail_person.setVisibility(View.GONE);
                    layout_orderdetail_info3.setVisibility(View.VISIBLE);
                    layout_orderdetail_employer.removeAllViews();
                    for (String s : value.getPicStaffArray()) {
                        View view= LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.adapter_orderdetail_employee, null, false);
                        ImageRequest request;
                        if (!TextUtils.isEmpty(s)) {
                            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(s))
                                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(30), SizeUtils.dp2px(30))).build();
                        }
                        else {
                            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+R.mipmap.ic_avatar_small))
                                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(30), SizeUtils.dp2px(30))).build();
                        }
                        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request).setAutoPlayAnimations(true).build();
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
                        ib_nav_right.setVisibility(View.VISIBLE);
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
                            ib_nav_right.setVisibility(View.VISIBLE);
                        }
                        else if (value.getOrderStatus().equals("2")) {
                            layout_pop.addView(getPopupTextView("取消订单", v -> {
                                cancleMyOrder();
                                popupWindow.dismiss();
                            }));
                            ib_nav_right.setVisibility(View.VISIBLE);
                        }
                        else {
                            ib_nav_right.setVisibility(View.GONE);
                        }
                    }
                    // 草稿
                    else if (value.getOrderStatus().equals("0")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("重发");
                        ib_nav_right.setVisibility(View.GONE);
                    }
                }
                ArrayList<Uri> imagesList=new ArrayList<Uri>();
                for (String image : value.getPicListArray()) {
                    imagesList.add(Uri.parse(image));
                }
                vp_orderdetail.setPages(
                        new CBViewHolderCreator<LocalImageHolderView>() {
                            @Override
                            public LocalImageHolderView createHolder() {
                                return new LocalImageHolderView();
                            }
                        }, imagesList)
                        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                        .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });

                initPop(popView);

                lastStatueTime=System.currentTimeMillis();
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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(OrderDetailActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                isNotReceive=false;
                getOrderDetail();

                // 雇员接单状态变化以刷新
                OrderResponse orderResponse=new OrderResponse();
                orderResponse.setOrderId(getIntent().getStringExtra("orderId"));
                EventBus.getDefault().post(orderResponse);
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(Object value) {
                dismissNetworkDialog();

                EventBus.getDefault().post(new OrderResponse());
                showQRCode();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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
        paramBean.setOrderId(orderResponse.getOrderId());
        paramBean.setTag(value);
        paramBean.setSignTime(""+new Date().getTime());
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .staffSign(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(Object value) {
                dismissNetworkDialog();

                getOrderDetail();

                Intent intent=new Intent(OrderDetailActivity.this, OrderProcessActivity.class);
                intent.putExtra("process", "1");
                startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(Object value) {
                dismissNetworkDialog();

                EventBus.getDefault().post(new OrderResponse());
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

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

    // 收到推送以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NotificationBean response) {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(response.getExtra());
            // 相同订单进行刷新
            if (orderResponse!=null && jsonObject.getString("orderId").equals(orderResponse.getOrderId())) {
                getOrderDetail();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    /**
     * 时间发生变化，估摸着订单状态也发生变化了，请求主动刷新
     */
    private void needStateRefresh() {
        if (orderResponse!=null) {
            // 判断当前时间是否有任务
            boolean hasJob=false;
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
            String today=format.format(new Date());
            for (String s : orderResponse.getPeriodTime().split(",")) {
                if (s.equals(today)) {
                    hasJob=true;
                }
            }
            // 一直没有任务，直接忽略
            if (!hasJob && lastStatueTime==-1) {

            }
            // 之前没有任务，现在发现有任务了，需要刷新
            else if (hasJob && lastStatueTime==-1) {
                getOrderDetail();
            }
            // 之前有任务，但是现在没有任务了，需要刷新
            else if (!hasJob && lastStatueTime!=-1) {
                getOrderDetail();
            }
            // 之前有任务，现在也有任务了，根据条件判断
            else if (hasJob && lastStatueTime!=-1) {
                SimpleDateFormat lastStateFormat=new SimpleDateFormat("HHmm");
                Date lastStatueDate=new Date(lastStatueTime);
                // 最后一次状态获取时间
                int lastStatueTimeHM=Integer.parseInt(lastStateFormat.format(lastStatueDate));
                int startTimeHM=Integer.parseInt(orderResponse.getStartTime().split(":")[0]+orderResponse.getStartTime().split(":")[1]);
                int endTimeHM=Integer.parseInt(orderResponse.getEndTime().split(":")[0]+orderResponse.getEndTime().split(":")[1]);
                // 最后一次状态
                int lastState=-1;
                if (lastStatueTimeHM<startTimeHM) {
                    lastState=0;
                }
                else if (lastStatueTimeHM>endTimeHM) {
                    lastState=1;
                }
                else {
                    lastState=2;
                }

                // 当前状态获取时间
                SimpleDateFormat currentStateFormat=new SimpleDateFormat("HHmm");
                Date currentStateDate=new Date(System.currentTimeMillis());
                int currentStatueTimeHM=Integer.parseInt(currentStateFormat.format(currentStateDate));
                // 当前状态
                int currentState=-1;
                if (currentStatueTimeHM<startTimeHM) {
                    currentState=0;
                }
                else if (currentStatueTimeHM>endTimeHM) {
                    currentState=1;
                }
                else {
                    currentState=2;
                }

                // 不一致则刷新
                if (currentState!=lastState) {
                    getOrderDetail();
                }
                // 如果一致，要判断当前时间与最后一次状态获取时间是不是同一天，不是同一天还是要刷新
                else {
                    SimpleDateFormat compareFormat=new SimpleDateFormat("yyyy-MM-dd");
                    // 同一天不刷新
                    if (!compareFormat.format(lastStatueDate).equals(compareFormat.format(currentStateDate))) {
                        getOrderDetail();
                    }
                }
            }
        }
    }
}
