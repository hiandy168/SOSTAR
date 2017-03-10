package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.FlowLayout;
import com.renyu.sostar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/7.
 */

public class UpdateTextinfoWithLabelActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.et_updatetextinfowithlabel)
    EditText et_updatetextinfowithlabel;
    @BindView(R.id.fl_updatetextinfowithlabel)
    FlowLayout fl_updatetextinfowithlabel;

    String[] choiceArray={"品质检测", "机械操作", "普工", "技工"};

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("完成");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        et_updatetextinfowithlabel.setText(getIntent().getStringExtra("source"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_updatetextinfowithlabel;
    }

    @Override
    public void loadData() {
        for (String s : choiceArray) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTextColor(Color.parseColor("#999999"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.shape_rounded_corner_gray);
            textView.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(8), 0);
            textView.setText(s);
            textView.setOnClickListener(v -> {
                et_updatetextinfowithlabel.setText(s);
                et_updatetextinfowithlabel.setSelection(s.length());
            });
            ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin= SizeUtils.dp2px(4);
            params.rightMargin= SizeUtils.dp2px(4);
            params.bottomMargin= SizeUtils.dp2px(4);
            params.height= SizeUtils.dp2px(30);
            textView.setLayoutParams(params);
            fl_updatetextinfowithlabel.addView(textView);

        }
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

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                if (et_updatetextinfowithlabel.getText().toString().equals("")) {
                    Toast.makeText(this, "请填写用工类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent();
                intent.putExtra("value", et_updatetextinfowithlabel.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ib_nav_left:
                finish();
        }
    }
}
