package com.renyu.sostar.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateAddressInfoActivity;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.activity.other.UpdateTextInfoWithPicActivity;
import com.renyu.sostar.activity.other.UpdateTextinfoWithLabelActivity;
import com.renyu.sostar.activity.other.UpdateTimeInfoActivity;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/7.
 */

public class ReleaseOrderActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.tv_releaseorder_type)
    TextView tv_releaseorder_type;
    @BindView(R.id.tv_releaseorder_person)
    TextView tv_releaseorder_person;
    @BindView(R.id.tv_releaseorder_sex)
    TextView tv_releaseorder_sex;
    @BindView(R.id.tv_releaseorder_address)
    TextView tv_releaseorder_address;
    @BindView(R.id.tv_releaseorder_desp)
    TextView tv_releaseorder_desp;
    @BindView(R.id.tv_releaseorder_price)
    TextView tv_releaseorder_price;
    @BindView(R.id.tv_releaseorder_paytype)
    TextView tv_releaseorder_paytype;

    ArrayList<String> picPath;
    ArrayList<String> timeBeans;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("发布订单");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("保存");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        picPath=new ArrayList<>();
        timeBeans=new ArrayList<>();
    }

    @Override
    public int initViews() {
        return R.layout.activity_releaseorder;
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

    @OnClick({R.id.layout_releaseorder_type, R.id.layout_releaseorder_person, R.id.layout_releaseorder_sex,
            R.id.layout_releaseorder_address, R.id.layout_releaseorder_desp, R.id.layout_releaseorder_price,
            R.id.layout_releaseorder_paytype, R.id.layout_releaseorder_time, R.id.layout_releaseorder_worktime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_releaseorder_type:
                Intent intent_type=new Intent(ReleaseOrderActivity.this, UpdateTextinfoWithLabelActivity.class);
                intent_type.putExtra("title", "用工类型");
                startActivityForResult(intent_type, CommonParams.RESULT_UPDATELABELINFO);
                break;
            case R.id.layout_releaseorder_person:
                Intent intent_person=new Intent(ReleaseOrderActivity.this, UpdateTextInfoActivity.class);
                intent_person.putExtra("title", "需求人数");
                intent_person.putExtra("param", "staffAccount");
                intent_person.putExtra("needcommit", false);
                intent_person.putExtra("source", tv_releaseorder_person.getText().toString());
                startActivityForResult(intent_person, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_releaseorder_sex:
                choiceSex();
                break;
            case R.id.layout_releaseorder_address:
                Intent intent_address=new Intent(ReleaseOrderActivity.this, UpdateAddressInfoActivity.class);
                intent_address.putExtra("title", "工作地点");
                startActivityForResult(intent_address, CommonParams.RESULT_UPDATEADDRESSINFO);
                break;
            case R.id.layout_releaseorder_desp:
                Intent intent_desp=new Intent(ReleaseOrderActivity.this, UpdateTextInfoWithPicActivity.class);
                intent_desp.putExtra("title", "工作描述");
                intent_desp.putExtra("picPath", picPath);
                intent_desp.putExtra("ed", tv_releaseorder_desp.getText().toString());
                startActivityForResult(intent_desp, CommonParams.RESULT_UPDATEPICINFO);
                break;
            case R.id.layout_releaseorder_price:
                Intent intent_price=new Intent(ReleaseOrderActivity.this, UpdateTextInfoActivity.class);
                intent_price.putExtra("title", "工作报酬");
                intent_price.putExtra("param", "unitPrice");
                intent_price.putExtra("needcommit", false);
                intent_price.putExtra("source", tv_releaseorder_price.getText().toString()
                        .substring(0, tv_releaseorder_price.getText().toString().indexOf("/")));
                startActivityForResult(intent_price, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_releaseorder_paytype:
                choicePayType();
                break;
            case R.id.layout_releaseorder_time:
                Intent intent_time=new Intent(ReleaseOrderActivity.this, UpdateTimeInfoActivity.class);
                intent_time.putExtra("title", "用工日期");
                intent_time.putStringArrayListExtra("source", timeBeans);
                startActivityForResult(intent_time, CommonParams.RESULT_UPDATETIMEINFO);
                break;
            case R.id.layout_releaseorder_worktime:
                ArrayList<String> hours=new ArrayList<>();
                for (int i=0;i<24;i++) {
                    hours.add(i<10?"0"+i:""+i);
                }
                ArrayList<String> minutes=new ArrayList<>();
                for (int i=0;i<60;i++) {
                    minutes.add(i<10?"0"+i:""+i);
                }
                View view_timechoice= LayoutInflater.from(ReleaseOrderActivity.this)
                        .inflate(R.layout.view_actionsheet_timechoice, null, false);
                LoopView pop_wheel_timelayout_hour_start= (LoopView) view_timechoice.findViewById(R.id.pop_wheel_timelayout_hour_start);
                LoopView pop_wheel_timelayout_minute_start= (LoopView) view_timechoice.findViewById(R.id.pop_wheel_timelayout_minute_start);
                LoopView pop_wheel_timelayout_hour_end= (LoopView) view_timechoice.findViewById(R.id.pop_wheel_timelayout_hour_end);
                LoopView pop_wheel_timelayout_minute_end= (LoopView) view_timechoice.findViewById(R.id.pop_wheel_timelayout_minute_end);
                ActionSheetFragment.build(getSupportFragmentManager())
                        .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                        .setTitle("请选择用工时间")
                        .setOkTitle("确认")
                        .setCancelTitle("取消")
                        .setOnOKListener(value -> Log.d("ReleaseOrderActivity", hours.get(pop_wheel_timelayout_hour_start.getSelectedItem()) + ":" +
                                minutes.get(pop_wheel_timelayout_minute_start.getSelectedItem()) + "--" +
                                hours.get(pop_wheel_timelayout_hour_end.getSelectedItem()) + ":" +
                                minutes.get(pop_wheel_timelayout_minute_end.getSelectedItem())))
                        .setOnCancelListener(() -> {

                        })
                        .setCustomerView(view_timechoice)
                        .show();
                pop_wheel_timelayout_hour_start.setNotLoop();
                pop_wheel_timelayout_hour_start.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
                pop_wheel_timelayout_hour_start.setItems(hours);
                pop_wheel_timelayout_hour_start.setTextSize(18);
                pop_wheel_timelayout_minute_start.setNotLoop();
                pop_wheel_timelayout_minute_start.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
                pop_wheel_timelayout_minute_start.setItems(minutes);
                pop_wheel_timelayout_minute_start.setTextSize(18);
                pop_wheel_timelayout_hour_end.setNotLoop();
                pop_wheel_timelayout_hour_end.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
                pop_wheel_timelayout_hour_end.setItems(hours);
                pop_wheel_timelayout_hour_end.setTextSize(18);
                pop_wheel_timelayout_minute_end.setNotLoop();
                pop_wheel_timelayout_minute_end.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
                pop_wheel_timelayout_minute_end.setItems(minutes);
                pop_wheel_timelayout_minute_end.setTextSize(18);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_UPDATELABELINFO && resultCode==RESULT_OK) {
            tv_releaseorder_type.setText(data.getStringExtra("value"));
            tv_releaseorder_type.setVisibility(View.VISIBLE);
        }
        if (requestCode==CommonParams.RESULT_UPDATEUSERINFO && resultCode==RESULT_OK) {
            if (data.getStringExtra("param").equals("staffAccount")) {
                tv_releaseorder_person.setText(data.getStringExtra("value"));
            }
            else if (data.getStringExtra("param").equals("unitPrice")) {
                tv_releaseorder_price.setText(data.getStringExtra("value"));
            }
        }
        if (requestCode==CommonParams.RESULT_UPDATEPICINFO && resultCode==RESULT_OK) {
            picPath=data.getStringArrayListExtra("picPath");
            tv_releaseorder_desp.setText(data.getStringExtra("ed"));
        }
        if (requestCode==CommonParams.RESULT_UPDATEADDRESSINFO && resultCode==RESULT_OK) {
            tv_releaseorder_address.setText(data.getStringExtra("value"));
        }
        if (requestCode==CommonParams.RESULT_UPDATETIMEINFO && resultCode==RESULT_OK) {
            ArrayList<String> beans=data.getStringArrayListExtra("value");
            timeBeans.addAll(beans);
        }
    }

    private void choiceSex() {
        View view_clearmessage= LayoutInflater.from(ReleaseOrderActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置性别")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("男");
        pop_three_choice1.setOnClickListener(v -> {
            tv_releaseorder_sex.setText("男");
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("女");
        pop_three_choice2.setOnClickListener(v -> {
            tv_releaseorder_sex.setText("女");
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("不限");
        pop_three_cancel.setOnClickListener(v -> {
            tv_releaseorder_sex.setText("不限");
            actionSheetFragment.dismiss();
        });
    }

    private void choicePayType() {
        View view_clearmessage= LayoutInflater.from(ReleaseOrderActivity.this)
                .inflate(R.layout.view_actionsheet_button_2, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("结算方式")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_double_choice= (TextView) view_clearmessage.findViewById(R.id.pop_double_choice);
        pop_double_choice.setTextColor(Color.parseColor("#333333"));
        pop_double_choice.setText("日结");
        pop_double_choice.setOnClickListener(v -> {
            tv_releaseorder_paytype.setText("日结");
            actionSheetFragment.dismiss();
        });
        TextView pop_double_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_double_cancel);
        pop_double_cancel.setText("定单结");
        pop_double_cancel.setOnClickListener(v -> {
            tv_releaseorder_paytype.setText("定单结");
            actionSheetFragment.dismiss();
        });
    }
}
