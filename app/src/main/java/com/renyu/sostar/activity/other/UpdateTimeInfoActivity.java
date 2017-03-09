package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.ActionSheetUtils;
import com.renyu.sostar.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/8.
 */

public class UpdateTimeInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.scroll_updatetimeinfo)
    ScrollView scroll_updatetimeinfo;
    @BindView(R.id.layout_updatetimeinfo_content)
    LinearLayout layout_updatetimeinfo_content;
    @BindView(R.id.layout_updatetimeinfo_root)
    LinearLayout layout_updatetimeinfo_root;

    ArrayList<String> beans;

    int maxHeight=0;

    int viewHeight=0;

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        layout_updatetimeinfo_root.post(() -> maxHeight=layout_updatetimeinfo_root.getMeasuredHeight()- SizeUtils.dp2px(122));
        for (int i = 0; i < beans.size(); i++) {
            add(""+i);
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_updatetimeinfo;
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

    private void add(String tag) {
        View view= LayoutInflater.from(this).inflate(R.layout.view_updatetimeinfo, null, false);
        view.setTag(tag);
        TextView tv_updatetimeinfo_start= (TextView) view.findViewById(R.id.tv_updatetimeinfo_start);
        TextView tv_updatetimeinfo_end= (TextView) view.findViewById(R.id.tv_updatetimeinfo_end);
        TextView tv_updatetimeinfo_all= (TextView) view.findViewById(R.id.tv_updatetimeinfo_all);
        tv_updatetimeinfo_start.setOnClickListener(v -> ActionSheetUtils.showAfterDate(UpdateTimeInfoActivity.this.getSupportFragmentManager(),
                "开始日期",
                "取消",
                "完成",
                value -> {
                    String string = value.toString();
                    String year = string.split("-")[0];
                    String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                    String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                    tv_updatetimeinfo_start.setText(year + "/" + month + "/" + day);
                    tv_updatetimeinfo_start.setTag(year+month+day);
                    if (tv_updatetimeinfo_end.getTag()!=null && !tv_updatetimeinfo_end.getTag().toString().equals("")) {
                        if (Integer.parseInt(tv_updatetimeinfo_start.getTag().toString())>
                                Integer.parseInt(tv_updatetimeinfo_end.getTag().toString())) {
                            Toast.makeText(this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                            tv_updatetimeinfo_start.setText("");
                            tv_updatetimeinfo_start.setTag("");
                            tv_updatetimeinfo_all.setText("");
                        }
                        else {
                            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                            try {
                                long startDate=dateFormat.parse(tv_updatetimeinfo_start.getText().toString()).getTime();
                                long endDate=dateFormat.parse(tv_updatetimeinfo_end.getText().toString()).getTime();
                                tv_updatetimeinfo_all.setText(((endDate-startDate)/(24*3600*1000)+1)+"天");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        tv_updatetimeinfo_all.setText("");
                    }
                }, () -> {

                }
        ));
        tv_updatetimeinfo_end.setOnClickListener(v -> ActionSheetUtils.showAfterDate(UpdateTimeInfoActivity.this.getSupportFragmentManager(),
                "结束日期",
                "取消",
                "完成",
                value -> {
                    String string = value.toString();
                    String year = string.split("-")[0];
                    String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                    String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                    tv_updatetimeinfo_end.setText(year + "/" + month + "/" + day);
                    tv_updatetimeinfo_end.setTag(year+month+day);
                    if (tv_updatetimeinfo_start.getTag()!=null && !tv_updatetimeinfo_start.getTag().toString().equals("")) {
                        if (Integer.parseInt(tv_updatetimeinfo_start.getTag().toString())>
                                Integer.parseInt(tv_updatetimeinfo_end.getTag().toString())) {
                            Toast.makeText(this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                            tv_updatetimeinfo_end.setText("");
                            tv_updatetimeinfo_end.setTag("");
                            tv_updatetimeinfo_all.setText("");
                        }
                        else {
                            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                            try {
                                long startDate=dateFormat.parse(tv_updatetimeinfo_start.getText().toString()).getTime();
                                long endDate=dateFormat.parse(tv_updatetimeinfo_end.getText().toString()).getTime();
                                tv_updatetimeinfo_all.setText(((endDate-startDate)/(24*3600*1000)+1)+"天");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        tv_updatetimeinfo_all.setText("");
                    }
                }, () -> {

                }
        ));
        Button btn_updatetimeinfo_delete= (Button) view.findViewById(R.id.btn_updatetimeinfo_delete);
        btn_updatetimeinfo_delete.setOnClickListener(v -> {
            layout_updatetimeinfo_content.removeView(view);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) scroll_updatetimeinfo.getLayoutParams();
            if (viewHeight*layout_updatetimeinfo_content.getChildCount()>maxHeight) {
                params.height=maxHeight;
            }
            else {
                params.height=viewHeight*layout_updatetimeinfo_content.getChildCount();
            }
            scroll_updatetimeinfo.setLayoutParams(params);
        });
        layout_updatetimeinfo_content.addView(view);
        view.post(() -> {
            viewHeight=view.getMeasuredHeight();
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) scroll_updatetimeinfo.getLayoutParams();
            if (view.getMeasuredHeight()*layout_updatetimeinfo_content.getChildCount()>maxHeight) {
                params.height=maxHeight;
                scroll_updatetimeinfo.setLayoutParams(params);
                scroll_updatetimeinfo.scrollTo(0, layout_updatetimeinfo_content.getMeasuredHeight()-maxHeight);
            }
            else {
                params.height=viewHeight*layout_updatetimeinfo_content.getChildCount();
                scroll_updatetimeinfo.setLayoutParams(params);
            }
        });
    }

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right, R.id.btn_updatetimeinfo_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                for (int i=0;i<layout_updatetimeinfo_content.getChildCount();i++) {
                    View view1=layout_updatetimeinfo_content.getChildAt(i);
                    TextView tv_updatetimeinfo_start= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_start);
                    TextView tv_updatetimeinfo_end= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_end);
                    TextView tv_updatetimeinfo_all= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_all);
                    beans.clear();
                    if (tv_updatetimeinfo_all.getTag()!=null && !tv_updatetimeinfo_all.getTag().toString().equals("")) {
                        beans.add(tv_updatetimeinfo_start.getTag().toString()+"-"+tv_updatetimeinfo_end.getTag().toString());
                    }
                    Intent intent=new Intent();
                    intent.putStringArrayListExtra("value", beans);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_updatetimeinfo_add:
                if (layout_updatetimeinfo_content.getChildCount()==0) {
                    add("0");
                }
                else {
                    String tag=layout_updatetimeinfo_content
                            .getChildAt(layout_updatetimeinfo_content.getChildCount()-1).getTag().toString();
                    add((Integer.parseInt(tag)+1)+"");
                }
                break;
        }
    }
}
