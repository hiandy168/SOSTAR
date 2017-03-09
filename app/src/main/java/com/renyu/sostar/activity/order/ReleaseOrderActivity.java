package com.renyu.sostar.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.OKHttpHelper;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateAddressInfoActivity;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.activity.other.UpdateTextInfoWithPicActivity;
import com.renyu.sostar.activity.other.UpdateTextinfoWithLabelActivity;
import com.renyu.sostar.activity.other.UpdateTimeInfoActivity;
import com.renyu.sostar.bean.EmployerCashAvaliableRequest;
import com.renyu.sostar.bean.EmployerCashAvaliableResponse;
import com.renyu.sostar.bean.ReleaseOrderRequest;
import com.renyu.sostar.bean.UploadResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.service.LocationService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

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
    @BindView(R.id.tv_releaseorder_time)
    TextView tv_releaseorder_time;
    @BindView(R.id.tv_releaseorder_worktime)
    TextView tv_releaseorder_worktime;
    @BindView(R.id.btn_releaseorder_needmoney)
    TextView btn_releaseorder_needmoney;
    @BindView(R.id.btn_releaseorder_avaliablemoney)
    TextView btn_releaseorder_avaliablemoney;
    @BindView(R.id.sb_releaseorder)
    SwitchButton sb_releaseorder;

    ArrayList<String> picPath;
    ArrayList<ReleaseOrderRequest.ParamBean.PeriodTimeListBean> timeBeans;

    Disposable disposable;

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
        getEmployerCashAvaliable();
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
            R.id.layout_releaseorder_paytype, R.id.layout_releaseorder_time, R.id.layout_releaseorder_worktime,
            R.id.btn_releaseorder_commit})
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
                intent_time.putExtra("source", timeBeans);
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
                        .setOnOKListener(value -> {
                            String temp=hours.get(pop_wheel_timelayout_hour_start.getSelectedItem()) + ":" +
                                    minutes.get(pop_wheel_timelayout_minute_start.getSelectedItem()) + "-" +
                                    hours.get(pop_wheel_timelayout_hour_end.getSelectedItem()) + ":" +
                                    minutes.get(pop_wheel_timelayout_minute_end.getSelectedItem());
                            tv_releaseorder_worktime.setText(temp);
                        })
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
            case R.id.btn_releaseorder_commit:
                uploadPic();
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
            ArrayList<ReleaseOrderRequest.ParamBean.PeriodTimeListBean> beans= (ArrayList<ReleaseOrderRequest.ParamBean.PeriodTimeListBean>) data.getSerializableExtra("value");
            timeBeans.clear();
            timeBeans.addAll(beans);
            if (timeBeans.size()==0) {
                tv_releaseorder_time.setText("");
            }
            else if (timeBeans.size()==1) {
                tv_releaseorder_time.setText(timeBeans.get(0).getStartTime()+"-"+timeBeans.get(0).getEndTime());
            }
            else {
                tv_releaseorder_time.setText(timeBeans.get(0).getStartTime()+"-"+timeBeans.get(0).getEndTime()+"...");
            }
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

    private void getEmployerCashAvaliable() {
        EmployerCashAvaliableRequest request=new EmployerCashAvaliableRequest();
        EmployerCashAvaliableRequest.ParamBean paramBean=new EmployerCashAvaliableRequest.ParamBean();
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getEmployerCashAvaiable(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployerCashAvaliableResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmployerCashAvaliableResponse value) {
                btn_releaseorder_avaliablemoney.setText("可用余额：¥"+value.getCashAvaiable());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void uploadPic() {
        OKHttpHelper helper=new OKHttpHelper();
        String url="http://114.215.18.160:9333/submit";
        Observable.create((ObservableOnSubscribe<ArrayList<String>>) e -> {
            ArrayList<String> images=new ArrayList<>();
            for (String s : picPath) {
                HashMap<String, File> fileHashMap=new HashMap<>();
                fileHashMap.put("image", new File(s));
                Response resp=helper.syncUpload(fileHashMap, url, new HashMap<>());
                if (resp.isSuccessful()) {
                    Gson gson=new Gson();
                    UploadResponse response=gson.fromJson(resp.body().string(), UploadResponse.class);
                    String imageUrl="http://114.215.18.160:8081/"+response.getFid();
                    images.add(imageUrl);
                }
            }
            e.onNext(images);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ArrayList<String> value) {
                releaseOrder(value);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ReleaseOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void releaseOrder(ArrayList<String> images) {
        if (TextUtils.isEmpty(tv_releaseorder_type.getText().toString())) {
            Toast.makeText(this, "请选择用工类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeBeans.size()==0) {
            Toast.makeText(this, "请添加用工日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_worktime.getText().toString())) {
            Toast.makeText(this, "请选择工作时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_person.getText().toString())) {
            Toast.makeText(this, "请填写需求人数", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_sex.getText().toString())) {
            Toast.makeText(this, "请选择性别要求", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_address.getText().toString())) {
            Toast.makeText(this, "请填写工作地点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_desp.getText().toString())) {
            Toast.makeText(this, "请填写详细描述", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_price.getText().toString())) {
            Toast.makeText(this, "请填写工作报酬", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_releaseorder_type.getText().toString())) {
            Toast.makeText(this, "请选择结算方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (LocationService.lastBdLocation==null) {
            Toast.makeText(this, "暂无定位数据", Toast.LENGTH_SHORT).show();
            return;
        }
        ReleaseOrderRequest request=new ReleaseOrderRequest();
        ReleaseOrderRequest.ParamBean paramBean=new ReleaseOrderRequest.ParamBean();
        paramBean.setEndTime(tv_releaseorder_worktime.getText().toString().split("-")[1]);
        paramBean.setStartTime(tv_releaseorder_worktime.getText().toString().split("-")[0]);
        paramBean.setAddress(tv_releaseorder_address.getText().toString());
        paramBean.setConfirmFlg(sb_releaseorder.isChecked()?"1":"0");
        paramBean.setDescription(tv_releaseorder_desp.getText().toString());
        paramBean.setJobType(tv_releaseorder_type.getText().toString());
        paramBean.setLatitude(""+LocationService.lastBdLocation.getLatitude());
        paramBean.setLongitude(""+LocationService.lastBdLocation.getLongitude());
        paramBean.setPaymentType(tv_releaseorder_type.getText().toString().equals("日结")?"1":"2");
        paramBean.setPicListArray(images);
        if (tv_releaseorder_sex.getText().toString().equals("男")) {
            paramBean.setSex("1");
        }
        else if (tv_releaseorder_sex.getText().toString().equals("女")) {
            paramBean.setSex("2");
        }
        else {
            paramBean.setSex("0");
        }
        paramBean.setStaffAccount(Integer.parseInt(tv_releaseorder_person.getText().toString()));
        paramBean.setUnitPrice(Integer.parseInt(tv_releaseorder_price.getText().toString().substring(0, tv_releaseorder_price.getText().toString().indexOf("/"))));
        paramBean.setPeriodTimeList(timeBeans);
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .releaseOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(ReleaseOrderActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ReleaseOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
