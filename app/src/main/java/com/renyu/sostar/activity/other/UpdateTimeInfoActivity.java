package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFragment;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetUtils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.ReleaseOrderRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.renyu.commonlibrary.views.actionsheet.ActionSheetUtils.showAfterDateWithoutDismiss;

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

    ArrayList<ReleaseOrderRequest.ParamBean.PeriodTimeListBean> beans;

    int maxHeight=0;

    int viewHeight=0;

    // 临时使用
    ActionSheetFragment actionSheetFragment;

    @Override
    public void initParams() {
        beans= (ArrayList<ReleaseOrderRequest.ParamBean.PeriodTimeListBean>) getIntent().getSerializableExtra("source");

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        layout_updatetimeinfo_root.post(() -> maxHeight=layout_updatetimeinfo_root.getMeasuredHeight()- SizeUtils.dp2px(122));
        if (beans.size()==0) {
            add("0", null);
        }
        else {
            for (int i = 0; i < beans.size(); i++) {
                add(""+i, beans.get(i));
            }
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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    private void add(String tag, ReleaseOrderRequest.ParamBean.PeriodTimeListBean bean) {
        View view= LayoutInflater.from(this).inflate(R.layout.view_updatetimeinfo, null, false);
        view.setTag(tag);
        TextView tv_updatetimeinfo_start= (TextView) view.findViewById(R.id.tv_updatetimeinfo_start);
        TextView tv_updatetimeinfo_end= (TextView) view.findViewById(R.id.tv_updatetimeinfo_end);
        TextView tv_updatetimeinfo_all= (TextView) view.findViewById(R.id.tv_updatetimeinfo_all);
        if (bean!=null) {
            {
                tv_updatetimeinfo_start.setText(bean.getStartTime());
                String[] values=bean.getStartTime().split("/");
                tv_updatetimeinfo_start.setTag(values[0]+values[1]+values[2]);
            }
            {
                tv_updatetimeinfo_end.setText(bean.getEndTime());
                String[] values=bean.getEndTime().split("/");
                tv_updatetimeinfo_end.setTag(values[0]+values[1]+values[2]);
            }
            {
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                try {
                    long startDate=dateFormat.parse(bean.getStartTime()).getTime();
                    long endDate=dateFormat.parse(bean.getEndTime()).getTime();
                    tv_updatetimeinfo_all.setText(((endDate-startDate)/(24*3600*1000)+1)+"天");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        tv_updatetimeinfo_start.setOnClickListener(v -> actionSheetFragment=showAfterDateWithoutDismiss(UpdateTimeInfoActivity.this.getSupportFragmentManager(),
                false,
                "开始日期",
                "取消",
                "完成",
                value -> {
                    String string = value.toString();
                    String year = string.split("-")[0];
                    String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                    String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                    tv_updatetimeinfo_start.setTag(year+month+day);
                    if (tv_updatetimeinfo_end.getTag()!=null && !tv_updatetimeinfo_end.getTag().toString().equals("")) {
                        if (Integer.parseInt(tv_updatetimeinfo_start.getTag().toString())>
                                Integer.parseInt(tv_updatetimeinfo_end.getTag().toString())) {
                            Toast.makeText(UpdateTimeInfoActivity.this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 是否有时间重叠部分
                            if (checkStartTime(tv_updatetimeinfo_start, tv_updatetimeinfo_end, year + "/" + month + "/" + day)) {
                                Toast.makeText(UpdateTimeInfoActivity.this, "时间重合", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                tv_updatetimeinfo_start.setText(year + "/" + month + "/" + day);
                                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                                try {
                                    long startDate=dateFormat.parse(tv_updatetimeinfo_start.getText().toString()).getTime();
                                    long endDate=dateFormat.parse(tv_updatetimeinfo_end.getText().toString()).getTime();
                                    tv_updatetimeinfo_all.setText(((endDate-startDate)/(24*3600*1000)+1)+"天");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                actionSheetFragment.dismiss();
                            }
                        }
                    }
                    else {
                        // 是否有时间重叠部分
                        if (checkStartTime(tv_updatetimeinfo_start, tv_updatetimeinfo_end, year + "/" + month + "/" + day)) {
                            Toast.makeText(UpdateTimeInfoActivity.this, "时间重合", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            actionSheetFragment.dismiss();
                            tv_updatetimeinfo_start.setText(year + "/" + month + "/" + day);
                            tv_updatetimeinfo_all.setText("");
                        }

                    }
                }, () -> {
                    if (TextUtils.isEmpty(tv_updatetimeinfo_start.getText().toString())) {
                        tv_updatetimeinfo_start.setTag("");
                    }
                    else {
                        String[] temp=tv_updatetimeinfo_start.getText().toString().split("/");
                        tv_updatetimeinfo_start.setTag(temp[0]+temp[1]+temp[2]);
                    }
                }));
        tv_updatetimeinfo_end.setOnClickListener(v -> actionSheetFragment=ActionSheetUtils.showAfterDateWithoutDismiss(UpdateTimeInfoActivity.this.getSupportFragmentManager(),
                false,
                "结束日期",
                "取消",
                "完成",
                value -> {
                    String string = value.toString();
                    String year = string.split("-")[0];
                    String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                    String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                    tv_updatetimeinfo_end.setTag(year+month+day);
                    if (tv_updatetimeinfo_start.getTag()!=null && !tv_updatetimeinfo_start.getTag().toString().equals("")) {
                        if (Integer.parseInt(tv_updatetimeinfo_start.getTag().toString())>
                                Integer.parseInt(tv_updatetimeinfo_end.getTag().toString())) {
                            Toast.makeText(UpdateTimeInfoActivity.this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 是否有时间重叠部分
                            if (checkEndTime(tv_updatetimeinfo_start, tv_updatetimeinfo_end, year + "/" + month + "/" + day)) {
                                Toast.makeText(UpdateTimeInfoActivity.this, "时间重合", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                tv_updatetimeinfo_end.setText(year + "/" + month + "/" + day);
                                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                                try {
                                    long startDate=dateFormat.parse(tv_updatetimeinfo_start.getText().toString()).getTime();
                                    long endDate=dateFormat.parse(tv_updatetimeinfo_end.getText().toString()).getTime();
                                    tv_updatetimeinfo_all.setText(((endDate-startDate)/(24*3600*1000)+1)+"天");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                actionSheetFragment.dismiss();
                            }
                        }
                    }
                    else {
                        // 是否有时间重叠部分
                        if (checkEndTime(tv_updatetimeinfo_start, tv_updatetimeinfo_end, year + "/" + month + "/" + day)) {
                            Toast.makeText(UpdateTimeInfoActivity.this, "时间重合", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            actionSheetFragment.dismiss();
                            tv_updatetimeinfo_end.setText(year + "/" + month + "/" + day);
                            tv_updatetimeinfo_all.setText("");
                        }
                    }
                }, () -> {
                    if (TextUtils.isEmpty(tv_updatetimeinfo_end.getText().toString())) {
                        tv_updatetimeinfo_end.setTag("");
                    }
                    else {
                        String[] temp=tv_updatetimeinfo_end.getText().toString().split("/");
                        tv_updatetimeinfo_end.setTag(temp[0]+temp[1]+temp[2]);
                    }
                }));
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
                beans.clear();
                for (int i=0;i<layout_updatetimeinfo_content.getChildCount();i++) {
                    View view1=layout_updatetimeinfo_content.getChildAt(i);
                    TextView tv_updatetimeinfo_start= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_start);
                    TextView tv_updatetimeinfo_end= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_end);
                    TextView tv_updatetimeinfo_all= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_all);
                    if (!TextUtils.isEmpty(tv_updatetimeinfo_all.getText().toString())) {
                        ReleaseOrderRequest.ParamBean.PeriodTimeListBean bean=new ReleaseOrderRequest.ParamBean.PeriodTimeListBean();
                        bean.setStartTime(tv_updatetimeinfo_start.getText().toString());
                        bean.setEndTime(tv_updatetimeinfo_end.getText().toString());
                        beans.add(bean);
                    }
                }
                Intent intent=new Intent();
                intent.putExtra("value", beans);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_updatetimeinfo_add:
                if (layout_updatetimeinfo_content.getChildCount()==0) {
                    add("0", null);
                }
                else {
                    String tag=layout_updatetimeinfo_content
                            .getChildAt(layout_updatetimeinfo_content.getChildCount()-1).getTag().toString();
                    add((Integer.parseInt(tag)+1)+"", null);
                }
                break;
        }
    }

    /**
     * 检查起始时间不能包括在之前已选的里面
     * @param tv_updatetimeinfo_start
     * @param time
     * @return
     */
    private boolean checkStartTime(TextView tv_updatetimeinfo_start, TextView tv_updatetimeinfo_end, String time) {
        boolean timeRepeat=false;
        for (int i=0;i<layout_updatetimeinfo_content.getChildCount();i++) {
            View view1=layout_updatetimeinfo_content.getChildAt(i);
            // 过滤自己
            TextView tv_updatetimeinfo_start1= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_start);
            if (tv_updatetimeinfo_start1==tv_updatetimeinfo_start) {
                continue;
            }
            TextView tv_updatetimeinfo_end1= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_end);
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
            try {
                long start=format.parse(tv_updatetimeinfo_start1.getText().toString()).getTime();
                long end=format.parse(tv_updatetimeinfo_end1.getText().toString()).getTime();
                long current=format.parse(time).getTime();
                // 起始时间不能包括在之前已选的里面
                if (current>=start && current<=end) {
                    timeRepeat=true;
                    break;
                }
                // 当存在终止时间的时候，起始时间不能包括在之前已选的
                if (!TextUtils.isEmpty(tv_updatetimeinfo_end.getText().toString())) {
                    long current2=format.parse(tv_updatetimeinfo_end.getText().toString()).getTime();
                    if (current2>=end && current<=end) {
                        timeRepeat=true;
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeRepeat;
    }

    /**
     * 检查结束时间不能包括在之前已选的里面
     * @param tv_updatetimeinfo_start
     * @param time
     * @return
     */
    private boolean checkEndTime(TextView tv_updatetimeinfo_start, TextView tv_updatetimeinfo_end, String time) {
        boolean timeRepeat=false;
        for (int i=0;i<layout_updatetimeinfo_content.getChildCount();i++) {
            View view1=layout_updatetimeinfo_content.getChildAt(i);
            // 过滤自己
            TextView tv_updatetimeinfo_end1= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_end);
            if (tv_updatetimeinfo_end1==tv_updatetimeinfo_end) {
                continue;
            }
            TextView tv_updatetimeinfo_start1= (TextView) view1.findViewById(R.id.tv_updatetimeinfo_start);
            SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
            try {
                long start=format.parse(tv_updatetimeinfo_start1.getText().toString()).getTime();
                long end=format.parse(tv_updatetimeinfo_end1.getText().toString()).getTime();
                long current=format.parse(time).getTime();
                // 结束时间不能包括在之前已选的里面
                if (current>=start && current<=end) {
                    timeRepeat=true;
                    break;
                }
                // 当存在起始时间的时候，结束时间不能包括在之前已选的
                if (!TextUtils.isEmpty(tv_updatetimeinfo_start.getText().toString())) {
                    long current2=format.parse(tv_updatetimeinfo_start.getText().toString()).getTime();
                    if (current2<start && current<start) {

                    }
                    else if (current2>end && current>end) {

                    }
                    else {
                        timeRepeat=true;
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeRepeat;
    }
}
