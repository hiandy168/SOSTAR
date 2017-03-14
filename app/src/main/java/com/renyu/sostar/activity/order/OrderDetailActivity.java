package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;

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

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.TRANSPARENT);
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("订单详情");
        ib_nav_right.setImageResource(R.mipmap.ic_order_more);
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);

        btn_orderdetail_commit.setVisibility(View.GONE);
        btn_orderdetail_cancel.setVisibility(View.GONE);
        tv_orderdetail_tip.setVisibility(View.GONE);

        BarUtils.adjustStatusBar(this, (ViewGroup) findViewById(R.id.layout_orderdetail_nav), -1);

        images=new ArrayList<>();
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

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_nav_right:
                break;
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
            observable=retrofit.create(RetrofitImpl.class)
                    .staffOrderDetail(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background());
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
                tv_orderdetail_type.setText(value.getJobType());
                tv_orderdetail_person.setText(""+value.getStaffAccount());
                int dayNum=value.getPeriodTime().split(",").length;
                // 按天
                if (value.getUnitPriceType().equals("1")) {
                    tv_orderdetail_priceall.setText("总价"+dayNum*value.getUnitPrice()*dayNum);
                }
                // 按小时
                else if (value.getUnitPriceType().equals("2")) {
                    double timeNum=Integer.parseInt(value.getEndTime())-Integer.parseInt(value.getStartTime())*1.0f/100;
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
                DraweeController logoDraweeController = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(value.getLogoPath())).setAutoPlayAnimations(true).build();
                iv_orderdetail_logo.setController(logoDraweeController);
                tv_orderdetail_comp.setText(value.getCompanyName());
                tv_orderdetail_orderid.setText(value.getOrderId());
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
                    layout_orderdetail_info3.setVisibility(View.GONE);
                    // 未接单
                    if (value.getOrderStatus().equals("1")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("接单");
                        tv_orderdetail_tip.setVisibility(View.VISIBLE);
                    }
                    // 已成单
                    else if (value.getOrderStatus().equals("2")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("立即签到");
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("取消订单");
                    }
                    // 已开始
                    else if (value.getOrderStatus().equals("4")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("查看进度");
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("申请离职");
                    }
                    // 已完成
                    else if (value.getOrderStatus().equals("3")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("评价企业");
                    }
                    // 已拒绝
                    else if (value.getOrderStatus().equals("5")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("接单");
                        tv_orderdetail_tip.setVisibility(View.VISIBLE);
                    }
                }
                else if (ACache.get(OrderDetailActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    tv_orderdetail_priceall.setVisibility(View.GONE);
                    for (String s : value.getPicStaffArray()) {
                        View view= LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.adapter_orderdetail_employee, null, false);
                        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                .setUri(Uri.parse(s)).setAutoPlayAnimations(true).build();
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
                    // 已发单
                    if (value.getOrderStatus().equals("1")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("立即签到");
                        btn_orderdetail_cancel.setVisibility(View.VISIBLE);
                        btn_orderdetail_cancel.setText("取消订单");
                    }
                    // 已开始
                    else if (value.getOrderStatus().equals("2")) {
                        btn_orderdetail_commit.setVisibility(View.VISIBLE);
                        btn_orderdetail_commit.setText("查看进度");
                    }
                    // 已完成
                    else if (value.getOrderStatus().equals("3")) {

                    }
                    // 草稿
                    else if (value.getOrderStatus().equals("0")) {

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
