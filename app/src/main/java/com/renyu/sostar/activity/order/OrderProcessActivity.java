package com.renyu.sostar.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.params.CommonParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/21.
 */

public class OrderProcessActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.iv_orderprocess)
    ImageView iv_orderprocess;
    @BindView(R.id.tv_orderprocess)
    TextView tv_orderprocess;
    @BindView(R.id.btn_orderprocess_commit)
    Button btn_orderprocess_commit;
    @BindView(R.id.layout_orderprocess_money)
    LinearLayout layout_orderprocess_money;
    @BindView(R.id.tv_orderprocess_hint)
    TextView tv_orderprocess_hint;

    int process;
    OrderResponse orderResponse;

    @Override
    public void initParams() {
        process=Integer.parseInt(getIntent().getStringExtra("process"));

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (process==1) {
            iv_orderprocess.setImageResource(R.mipmap.ic_pay_comp);
            tv_orderprocess.setText("签到成功！\n请准时到岗开始工作");
            tv_nav_title.setText("扫码签到");
            btn_orderprocess_commit.setText("确认");
        }
        if ((process==3 || process==4 || process==5 || process==9) && ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            orderResponse= (OrderResponse) getIntent().getSerializableExtra("params");
            iv_orderprocess.setImageResource(R.mipmap.ic_order_working);
            tv_nav_title.setText("订单进度");
            if ((process!=4 && process!=9) && orderResponse.getPayFlg()!=1) {
                tv_nav_right.setText("加班");
            }

            // 未支付(只有订单完成的情况下才会出现未支付)
            if (orderResponse.getPayFlg()==0) {
                iv_orderprocess.setImageResource(R.mipmap.ic_order_comp);
                tv_orderprocess.setText("");
                layout_orderprocess_money.setVisibility(View.VISIBLE);
                tv_orderprocess_hint.setVisibility(View.VISIBLE);
                btn_orderprocess_commit.setText("确认支付");
                btn_orderprocess_commit.setVisibility(View.VISIBLE);
            }
            // 已支付
            else if (orderResponse.getPayFlg()==1) {
                iv_orderprocess.setImageResource(R.mipmap.ic_pay_comp);
                if (process==4) {
                    tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已过期");
                }
                else if (process==5) {
                    tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已完成");
                    btn_orderprocess_commit.setText("评价雇员");
                    btn_orderprocess_commit.setVisibility(View.VISIBLE);
                    btn_orderprocess_commit.setOnClickListener(v -> {
                        Intent intent_employees=new Intent(OrderProcessActivity.this, EmployeeListActivity.class);
                        intent_employees.putExtra("orderId", orderResponse.getOrderId());
                        startActivity(intent_employees);
                    });
                }
                else if (process==9) {
                    tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已取消");
                }
            }
            // 不可支付
            else {
                iv_orderprocess.setImageResource(R.mipmap.ic_order_working);
                tv_orderprocess.setText("订单正在进行中。\n已进行"+getWorkTime());
            }
        }
        if ((process==4 || process==5 || process==8) && ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            orderResponse= (OrderResponse) getIntent().getSerializableExtra("params");
            tv_nav_title.setText("订单进度");

            // 未支付
            if (orderResponse.getPayFlg()==0) {
                iv_orderprocess.setImageResource(R.mipmap.ic_order_comp);
                tv_orderprocess.setText("等待支付完成\n订单号"+orderResponse.getOrderId()+"  任务已完成");
            }
            // 已支付
            else if (orderResponse.getPayFlg()==1) {
                iv_orderprocess.setImageResource(R.mipmap.ic_pay_comp);
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已完成");
                if (process==4 || process==5) {
                    btn_orderprocess_commit.setText("评价雇主");
                    btn_orderprocess_commit.setVisibility(View.VISIBLE);
                    btn_orderprocess_commit.setOnClickListener(v -> {
                        Intent intent_employees=new Intent(OrderProcessActivity.this, EvaluateActivity.class);
                        intent_employees.putExtra("orderId", orderResponse.getOrderId());
                        intent_employees.putExtra("userName", "");
                        intent_employees.putExtra("userId", ACache.get(this).getAsString(CommonParams.USER_ID));
                        startActivityForResult(intent_employees, CommonParams.RESULT_EVALUATE);
                    });
                }
            }
            // 不可支付
            else {
                iv_orderprocess.setImageResource(R.mipmap.ic_order_working);
                tv_orderprocess.setText("订单正在进行中，请再接再厉！\n已工作"+getWorkTime());
            }
        }
        if (process==9 || process==10 || process==12 || process==13 || process==14) {
            orderResponse= (OrderResponse) getIntent().getSerializableExtra("params");
            tv_nav_title.setText("订单进度");

            iv_orderprocess.setImageResource(R.mipmap.ic_pay_comp);
            if (process==9) {
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已取消");
            }
            else if (process==10) {
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务您已被解雇");
            }
            else if (process==12) {
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务您已离职");
            }
            else if (process==13) {
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已取消");
            }
            else if (process==14) {
                tv_orderprocess.setText("支付成功\n订单号"+orderResponse.getOrderId()+"  任务已取消");
            }
        }
    }

    private String getWorkTime() {
        long currentTime=System.currentTimeMillis();
        // 寻找最接近的任务开始时间
        // 累计工作天时数
        int totalWork=-1;
        String[] periodTime=orderResponse.getPeriodTime().split(",");
        for (int i = 0; i < periodTime.length; i++) {
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String strTime=periodTime[i]+" "+orderResponse.getStartTime();
            try {
                long longTime=format.parse(strTime).getTime();
                // 当前时间超过所选择的开始时间
                if (currentTime>longTime) {
                    // 已经到了最后一天
                    if (i==periodTime.length-1) {
                        totalWork=periodTime.length-1;
                    }
                    // 没有到达最后一天则继续
                    else {
                        continue;
                    }
                }
                else {
                    totalWork=i-1;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (totalWork==-1) {
            return "0小时0分钟";
        }
        else {
            String startHour=orderResponse.getStartTime().split(":")[0];
            String startMinute=orderResponse.getStartTime().split(":")[1];
            String endHour=orderResponse.getEndTime().split(":")[0];
            String endMinute=orderResponse.getEndTime().split(":")[1];
            double perHour=((double) Integer.parseInt(endHour)-Integer.parseInt(startHour))
                    +((double) Integer.parseInt(endMinute)-Integer.parseInt(startMinute))/60;
            double totalHour=perHour*totalWork;
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Calendar cal=Calendar.getInstance();
            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH) + 1;
            int day=cal.get(Calendar.DAY_OF_MONTH);
            String endTime=year+"/"+month+"/"+day+" "+orderResponse.getEndTime();
            try {
                // 今日任务已结束
                if (currentTime>format.parse(endTime).getTime()) {
                    totalHour+=perHour;
                    if (totalHour-(int) totalHour>0) {
                        return (int) totalHour+"小时30分钟";
                    }
                    else {
                        return (int) totalHour+"小时";
                    }
                }
                else {
                    cal.setTime(new Date(currentTime));
                    int hour=cal.get(Calendar.HOUR_OF_DAY);
                    int minute=cal.get(Calendar.MINUTE);
                    int extraHour=0;
                    int extraMinute=0;
                    if (minute<Integer.parseInt(startMinute)) {
                        extraHour=(hour-1)-Integer.parseInt(startHour);
                        extraMinute=60+minute-Integer.parseInt(startMinute);
                    }
                    else {
                        extraHour=hour-Integer.parseInt(startHour);
                        extraMinute=minute-Integer.parseInt(startMinute);
                    }
                    if (totalHour-(int) totalHour>0) {
                        if (extraMinute>=30) {
                            return ((int) totalHour+1+extraHour)+"小时"+(extraMinute-30)+"分钟";
                        }
                        else {
                            return ((int) totalHour+extraHour)+"小时"+(extraMinute+30)+"分钟";
                        }
                    }
                    else {
                        return ((int) totalHour+extraHour)+"小时"+(extraMinute)+"分钟";
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderprocess;
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
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.btn_orderprocess_commit, R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_orderprocess_commit:
                if (ACache.get(OrderProcessActivity.this).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    if (process==1 || process==8) {
                        finish();
                    }
                }
                else if (ACache.get(OrderProcessActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    if (process==3) {
                        finish();
                    }
                }
                break;
            case R.id.tv_nav_right:
                if (ACache.get(OrderProcessActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    Intent intent=new Intent(OrderProcessActivity.this, OverTimeActivity.class);
                    intent.putExtra("params", getIntent().getSerializableExtra("params"));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==CommonParams.RESULT_EVALUATE) {
                btn_orderprocess_commit.setVisibility(View.GONE);
            }
        }
    }
}
