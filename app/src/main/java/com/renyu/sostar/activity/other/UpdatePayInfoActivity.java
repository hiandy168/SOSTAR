package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.sostar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/10.
 */

public class UpdatePayInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.ed_updatepayinfo_money)
    EditText ed_updatepayinfo_money;
    @BindView(R.id.tv_updatepayinfo_type)
    TextView tv_updatepayinfo_type;

    String source;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        source=getIntent().getStringExtra("source");
        if (source.indexOf("/")!=-1) {
            ed_updatepayinfo_money.setText(source.split("/")[0]);
            tv_updatepayinfo_type.setText("每"+source.split("/")[1]);
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_updatepayinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right, R.id.layout_updatepayinfo_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                commitPayInfo();
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_updatepayinfo_type:
                View view_clearmessage= LayoutInflater.from(UpdatePayInfoActivity.this)
                        .inflate(R.layout.view_actionsheet_button_2, null, false);
                ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                        .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                        .setTitle("计算单位")
                        .setCustomerView(view_clearmessage)
                        .show();
                TextView pop_double_choice= (TextView) view_clearmessage.findViewById(R.id.pop_double_choice);
                pop_double_choice.setTextColor(Color.parseColor("#333333"));
                pop_double_choice.setText("每天");
                pop_double_choice.setOnClickListener(v -> {
                    tv_updatepayinfo_type.setText("每天");
                    actionSheetFragment.dismiss();
                });
                TextView pop_double_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_double_cancel);
                pop_double_cancel.setText("每小时");
                pop_double_cancel.setOnClickListener(v -> {
                    tv_updatepayinfo_type.setText("每小时");
                    actionSheetFragment.dismiss();
                });
                break;
        }
    }

    private void commitPayInfo() {
        if (ed_updatepayinfo_money.getText().toString().equals("")) {
            Toast.makeText(this, "请填写报酬单价", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_updatepayinfo_type.getText().toString())) {
            Toast.makeText(this, "请选择计算单位", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent();
        intent.putExtra("value", ed_updatepayinfo_money.getText().toString()+"/"+tv_updatepayinfo_type.getText().toString().substring(1));
        setResult(RESULT_OK, intent);
        finish();
    }
}
