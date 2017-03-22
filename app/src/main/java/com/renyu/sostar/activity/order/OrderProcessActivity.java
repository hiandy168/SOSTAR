package com.renyu.sostar.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    @BindView(R.id.ib_nav_right)
    ImageButton ib_nav_right;
    @BindView(R.id.tv_orderprocess)
    TextView tv_orderprocess;
    @BindView(R.id.btn_orderprocess_commit)
    Button btn_orderprocess_commit;

    int process;

    @Override
    public void initParams() {
        process=Integer.parseInt(getIntent().getStringExtra("process"));

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        if (process==1) {
            tv_orderprocess.setText("签到成功！\n请准时到岗开始工作");
            tv_nav_title.setText("扫码签到");
            btn_orderprocess_commit.setText("确认");
        }
        if (process==3) {
            tv_nav_title.setText("订单进度");
            btn_orderprocess_commit.setText("确认");
            ib_nav_right.setTag("加班");

            tv_orderprocess.setText("订单正在进行中。\n已进行"+getWorkTime());
        }
        else if (process==8) {
            tv_nav_title.setText("订单进度");
            btn_orderprocess_commit.setText("确认");

            tv_orderprocess.setText("订单正在进行中，请再接再厉！\n已工作"+getWorkTime());
        }
    }

    private String getWorkTime() {
        OrderResponse orderResponse= (OrderResponse) getIntent().getSerializableExtra("params");
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

    @OnClick({R.id.ib_nav_left, R.id.btn_orderprocess_commit, R.id.ib_nav_right})
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
            case R.id.ib_nav_right:
                if (ACache.get(OrderProcessActivity.this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    Intent intent=new Intent(OrderProcessActivity.this, OverTimeActivity.class);
                    intent.putExtra("params", getIntent().getSerializableExtra("params"));
                    startActivity(intent);
                }
                break;
        }
    }
}
