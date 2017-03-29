package com.renyu.sostar.activity.order;

import android.content.Intent;
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
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.views.FlowLayout;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.EvaluateRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    String[] desp_emoloyee={"态度端正", "工作标杆", "技能过硬", "awesome", "厉害了我的哥", "形象佳"};
    String[] desp_emoloyer={"工作环境优", "诚信", "交通方便", "制度规范", "安全"};

    // 评价点击的TextView
    TextView lastTextView;

    // 进度条值
    int rating;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("评价");
        tv_evaluate_orderinfo.setText(getIntent().getStringExtra("userName")+" 订单编号 "+getIntent().getStringExtra("orderId"));
        rb_evaluate.setMax(5);
        rb_evaluate.setStepSize(1);
        rb_evaluate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            EvaluateActivity.this.rating= (int) rating;
        });
        String[] desp=desp_emoloyer;
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            desp=desp_emoloyer;
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            desp=desp_emoloyee;
        }
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
                evaluateStaff();
                break;
            case R.id.ib_nav_left:
                finish();
        }
    }

    private void evaluateStaff() {
        if (lastTextView==null) {
            Toast.makeText(this, "请选择评价内容", Toast.LENGTH_SHORT).show();
            return;
        }
        EvaluateRequest request=new EvaluateRequest();
        EvaluateRequest.ParamBean paramBean=new EvaluateRequest.ParamBean();
        paramBean.setEvaluate(lastTextView.getText().toString());
        paramBean.setOrderId(Integer.parseInt(getIntent().getStringExtra("orderId")));
        paramBean.setStar(rating);
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            paramBean.setType("2");
            paramBean.setFromUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
            paramBean.setToUserId(0);
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            paramBean.setType("1");
            paramBean.setFromUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
            paramBean.setToUserId(Integer.parseInt(getIntent().getStringExtra("userId")));
        }
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .evaluateStaff(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(EvaluateActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(EvaluateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
