package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/16.
 */

public class EmployeeInfo2Activity extends BaseActivity {

    @BindView(R.id.iv_employeeinfo2_avatar)
    SimpleDraweeView iv_employeeinfo2_avatar;
    @BindView(R.id.tv_employeeinfo2_auth)
    ImageView tv_employeeinfo2_auth;
    @BindView(R.id.tv_employeeinfo2_nickname)
    TextView tv_employeeinfo2_nickname;
    @BindView(R.id.tv_employeeinfo2_userId)
    TextView tv_employeeinfo2_userId;
    @BindView(R.id.tv_employeeinfo2_name)
    TextView tv_employeeinfo2_name;
    @BindView(R.id.tv_employeeinfo2_sex)
    TextView tv_employeeinfo2_sex;
    @BindView(R.id.tv_employeeinfo2_age)
    TextView tv_employeeinfo2_age;
    @BindView(R.id.tv_employeeinfo2_summary)
    TextView tv_employeeinfo2_summary;
    @BindView(R.id.tv_employeeinfo2_evaluate)
    TextView tv_employeeinfo2_evaluate;
    @BindView(R.id.tv_employeeinfo2_completionrate)
    TextView tv_employeeinfo2_completionrate;

    MyCenterEmployeeResponse response;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_employeeinfo2;
    }

    @Override
    public void loadData() {
        getEmployeeInfo();
    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    private void getEmployeeInfo() {
        if (TextUtils.isEmpty(ACache.get(this).getAsString(CommonParams.USER_ID))) {
            return;
        }
        MyCenterRequest request=new MyCenterRequest();
        MyCenterRequest.ParamBean paramBean=new MyCenterRequest.ParamBean();
        paramBean.setUserId(getIntent().getStringExtra("userId"));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getMyEmployeeCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyCenterEmployeeResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyCenterEmployeeResponse value) {
                response=value;
                if (!TextUtils.isEmpty(value.getNickName())) {
                    tv_employeeinfo2_nickname.setText(value.getNickName());
                }
                tv_employeeinfo2_userId.setText(getIntent().getStringExtra("userId"));
                if (!TextUtils.isEmpty(value.getPicPath())) {
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(value.getPicPath())).setAutoPlayAnimations(true).build();
                    iv_employeeinfo2_avatar.setController(draweeController);
                }
                if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
                    tv_employeeinfo2_auth.setVisibility(View.INVISIBLE);
                }
                else if (value.getAuthentication().equals("1")) {
                    tv_employeeinfo2_auth.setVisibility(View.VISIBLE);
                }
                else if (value.getAuthentication().equals("2")) {
                    tv_employeeinfo2_auth.setVisibility(View.INVISIBLE);
                }
                tv_employeeinfo2_name.setText(value.getName());
                tv_employeeinfo2_sex.setText(value.getSex().equals("1")?"男":"女");
                tv_employeeinfo2_age.setText(value.getAge());
                tv_employeeinfo2_summary.setText(value.getIntroduction());
                tv_employeeinfo2_evaluate.setText(TextUtils.isEmpty(value.getEvaluateLevel())?"0":value.getEvaluateLevel());
                tv_employeeinfo2_completionrate.setText(TextUtils.isEmpty(value.getCloseRate())?"0%":value.getCloseRate()+"%");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(EmployeeInfo2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick({R.id.tv_employeeinfo2_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_employeeinfo2_call:
                if (response!=null && !TextUtils.isEmpty(response.getPhone())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+response.getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }
}
