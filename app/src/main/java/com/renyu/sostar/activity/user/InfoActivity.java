package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpannableStringUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
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

public class InfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;

    @BindView(R.id.iv_info_avatar)
    SimpleDraweeView iv_info_avatar;
    @BindView(R.id.tv_info_auth)
    ImageView tv_info_auth;
    @BindView(R.id.tv_info_nickname)
    TextView tv_info_nickname;
    @BindView(R.id.tv_info_userId)
    TextView tv_info_userId;
    @BindView(R.id.layout_info_name)
    LinearLayout layout_info_name;
    @BindView(R.id.tv_info_name)
    TextView tv_info_name;
    @BindView(R.id.layout_info_compname)
    LinearLayout layout_info_compname;
    @BindView(R.id.tv_info_compname)
    TextView tv_info_compname;
    @BindView(R.id.layout_info_sex)
    LinearLayout layout_info_sex;
    @BindView(R.id.tv_info_sex)
    TextView tv_info_sex;
    @BindView(R.id.layout_info_compphone)
    LinearLayout layout_info_compphone;
    @BindView(R.id.tv_info_compphone)
    TextView tv_info_compphone;
    @BindView(R.id.layout_info_age)
    LinearLayout layout_info_age;
    @BindView(R.id.tv_info_age)
    TextView tv_info_age;
    @BindView(R.id.layout_info_web)
    LinearLayout layout_info_web;
    @BindView(R.id.tv_info_web)
    TextView tv_info_web;
    @BindView(R.id.tv_info_summary)
    TextView tv_info_summary;
    @BindView(R.id.tv_info_evaluate)
    TextView tv_info_evaluate;
    @BindView(R.id.tv_info_completionrate)
    TextView tv_info_completionrate;
    @BindView(R.id.tv_info_call)
    ImageView tv_info_call;

    MyCenterEmployeeResponse employeeResponse;
    MyCenterEmployerResponse employerResponse;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tv_nav_title.setTextColor(Color.WHITE);
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_write_left);
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            layout_info_compname.setVisibility(View.GONE);
            layout_info_compphone.setVisibility(View.GONE);
            layout_info_web.setVisibility(View.GONE);
            tv_nav_title.setText("个人详情");
        }
        else {
            layout_info_name.setVisibility(View.GONE);
            layout_info_sex.setVisibility(View.GONE);
            layout_info_age.setVisibility(View.GONE);
            tv_nav_title.setText("公司详情");
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_info;
    }

    @Override
    public void loadData() {
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            getEmployeeInfo();
        }
        else {
            getEmployerInfo();
        }
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
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(MyCenterEmployeeResponse value) {
                dismissNetworkDialog();

                employeeResponse=value;
                if (!TextUtils.isEmpty(value.getNickName())) {
                    tv_info_nickname.setText(value.getNickName());
                }
                tv_info_userId.setText("普工:"+getIntent().getStringExtra("userId"));
                if (!TextUtils.isEmpty(value.getPicPath())) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(value.getPicPath()))
                            .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(60), SizeUtils.dp2px(60))).build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request).setAutoPlayAnimations(true).build();
                    iv_info_avatar.setController(draweeController);
                }
                if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
                    tv_info_auth.setVisibility(View.INVISIBLE);
                }
                else if (value.getAuthentication().equals("1")) {
                    tv_info_auth.setVisibility(View.VISIBLE);
                }
                else if (value.getAuthentication().equals("2")) {
                    tv_info_auth.setVisibility(View.INVISIBLE);
                }
                tv_info_name.setText(value.getName());
                if (value.getSex()==null) {
                    tv_info_sex.setText("未知");
                }
                else {
                    tv_info_sex.setText(value.getSex().equals("1")?"男":"女");
                }
                tv_info_age.setText(value.getAge());
                tv_info_summary.setText(value.getIntroduction());
                tv_info_evaluate.setText(TextUtils.isEmpty(value.getEvaluateLevel())?"0":value.getEvaluateLevel());
                tv_info_completionrate.setText(TextUtils.isEmpty(value.getCloseRate())?"0%":value.getCloseRate());
                if (getIntent().getBooleanExtra("canphone", false)) {
                    tv_info_call.setVisibility(View.VISIBLE);
                }
                else {
                    tv_info_call.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(InfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getEmployerInfo() {
        if (TextUtils.isEmpty(ACache.get(this).getAsString(CommonParams.USER_ID))) {
            return;
        }
        MyCenterRequest request=new MyCenterRequest();
        MyCenterRequest.ParamBean paramBean=new MyCenterRequest.ParamBean();
        paramBean.setUserId(getIntent().getStringExtra("userId"));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getMyEmployerCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyCenterEmployerResponse>()  {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(MyCenterEmployerResponse value) {
                dismissNetworkDialog();

                employerResponse=value;
                if (!TextUtils.isEmpty(value.getCompanyName())) {
                    tv_info_nickname.setText(value.getCompanyName());
                }
                tv_info_userId.setText("机构识别代码:"+value.getCompanyCode());
                if (!TextUtils.isEmpty(value.getLogoPath())) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(value.getLogoPath()))
                            .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request).setAutoPlayAnimations(true).build();
                    iv_info_avatar.setController(draweeController);
                }
                if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
                    tv_info_auth.setVisibility(View.INVISIBLE);
                }
                else if (value.getAuthentication().equals("1")) {
                    tv_info_auth.setVisibility(View.VISIBLE);
                }
                else if (value.getAuthentication().equals("2")) {
                    tv_info_auth.setVisibility(View.INVISIBLE);
                }
                tv_info_compname.setText(value.getCompanyName());
                tv_info_compphone.setText(value.getContactPhone());
                tv_info_web.setMovementMethod(LinkMovementMethod.getInstance());
                tv_info_web.setText(new SpannableStringUtils.Builder().append(value.getWebAddress()).setUnderline()
                        .setClickSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Intent intent=new Intent(InfoActivity.this, WebActivity.class);
                                intent.putExtra("url", value.getWebAddress());
                                startActivity(intent);
                            }
                        }).create());
                tv_info_summary.setText(value.getIntroduction());
                tv_info_evaluate.setText(TextUtils.isEmpty(value.getStar())?"0":value.getStar());
                tv_info_completionrate.setText(TextUtils.isEmpty(value.getCloseRate())?"0%":value.getCloseRate());
                if (getIntent().getBooleanExtra("canphone", false)) {
                    tv_info_call.setVisibility(View.VISIBLE);
                    tv_info_compphone.setVisibility(View.VISIBLE);
                }
                else {
                    tv_info_call.setVisibility(View.GONE);
                    tv_info_compphone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(InfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick({R.id.tv_info_call, R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_info_call:
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    if (employeeResponse!=null && !TextUtils.isEmpty(employeeResponse.getPhone())) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+employeeResponse.getPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                else {
                    if (employerResponse!=null && !TextUtils.isEmpty(employerResponse.getContactPhone())) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+employerResponse.getContactPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}
