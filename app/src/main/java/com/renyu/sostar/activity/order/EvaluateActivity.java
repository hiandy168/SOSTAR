package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.FlowLayout;
import com.renyu.sostar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/26.
 */

public class EvaluateActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_evaluate_orderinfo)
    TextView tv_evaluate_orderinfo;
    @BindView(R.id.rb_evaluate)
    RatingBar rb_evaluate;
    @BindView(R.id.fl_evaluate)
    FlowLayout fl_evaluate;

    String[] desp={"态度端正", "工作标杆", "技能过硬", "awesome", "厉害了我的哥", "形象佳"};

    // 评价点击的TextView
    TextView lastTextView;

    // 进度条值
    float rating;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("评价");
        tv_evaluate_orderinfo.setText(getIntent().getStringExtra("userName")+" 订单编号 "+getIntent().getStringExtra("orderId"));
        rb_evaluate.setMax(5);
        rb_evaluate.setStepSize((float) 0.5);
        rb_evaluate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            EvaluateActivity.this.rating=rating;
        });
        for (String s : desp) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTextColor(Color.parseColor("#999999"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.shape_rounded_corner_gray);
            textView.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(8), 0);
            textView.setText(s);
            textView.setOnClickListener(v -> {
                if (lastTextView!=null) {
                    lastTextView.setTextColor(Color.parseColor("#999999"));
                    lastTextView.setBackgroundResource(R.drawable.shape_rounded_corner_gray);
                }
                lastTextView=textView;
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                textView.setBackgroundResource(R.drawable.shape_rounded_corner_green);
            });
            ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin= SizeUtils.dp2px(8);
            params.rightMargin= SizeUtils.dp2px(8);
            params.bottomMargin= SizeUtils.dp2px(13);
            params.height= SizeUtils.dp2px(30);
            textView.setLayoutParams(params);
            fl_evaluate.addView(textView);
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_evaluate;
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

    @OnClick({R.id.ib_nav_left, R.id.btn_evaluate_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_evaluate_commit:

                break;
            case R.id.ib_nav_left:
                finish();
        }
    }
}
