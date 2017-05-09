package com.renyu.sostar.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.WebActivity;
import com.renyu.sostar.activity.sign.SignInSignUpActivity;
import com.renyu.sostar.bean.NotificationChangeRequest;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.PushResponse;
import com.renyu.sostar.bean.SetUserTypeRequest;
import com.renyu.sostar.bean.SigninRequest;
import com.renyu.sostar.bean.SigninResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/27.
 */

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_settings_cache)
    TextView tv_settings_cache;
    @BindView(R.id.sb_settings_message)
    SwitchButton sb_settings_message;
    @BindView(R.id.tv_settings_rulechange)
    TextView tv_settings_rulechange;

    // 手动切换推送开关
    boolean userChange=true;

    boolean canChangeRole=false;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("通用设置");

        canChangeRole=getIntent().getBooleanExtra("canChangeRole", false);

        sb_settings_message.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (userChange) {
                userChange=false;
                return;
            }
            changeNotificationState();
        });
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            tv_settings_rulechange.setText("我是雇员");
        }
        else {
            tv_settings_rulechange.setText("我是雇主");
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_settings;
    }

    @Override
    public void loadData() {
        getPushInfo();
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

    @OnClick({R.id.layout_settings_cache, R.id.layout_settings_rolechange, R.id.btn_settings_sign_out,
            R.id.ib_nav_left, R.id.layout_settings_feedback, R.id.layout_settings_protocal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_settings_cache:
                Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT).show();
                Fresco.getImagePipeline().clearCaches();
                tv_settings_cache.setText("0MB");
                break;
            case R.id.layout_settings_rolechange:
                if (!canChangeRole) {
                    Toast.makeText(this, "请先完成身份认证，认证通过之后才可以切换身份", Toast.LENGTH_SHORT).show();
                    return;
                }
                View view_clearmessage= LayoutInflater.from(SettingsActivity.this)
                        .inflate(R.layout.view_actionsheet_button_2, null, false);
                ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                        .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                        .setTitle("切换身份")
                        .setCustomerView(view_clearmessage)
                        .show();
                TextView pop_double_choice= (TextView) view_clearmessage.findViewById(R.id.pop_double_choice);
                pop_double_choice.setTextColor(Color.parseColor("#333333"));
                pop_double_choice.setText(ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")?"切换雇主":"切换雇员");
                pop_double_choice.setOnClickListener(v -> {
                    actionSheetFragment.dismiss();
                    changeRole();
                });
                TextView pop_double_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_double_cancel);
                pop_double_cancel.setText("取消");
                pop_double_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
                break;
            case R.id.btn_settings_sign_out:
                // 清除登录状态
                ACache.get(this).remove(CommonParams.USER_SIGNIN);
                // 关闭极光推送alias相关
                JPushInterface.setAlias(SettingsActivity.this, "", null);
                // 回到登录注册页
                Intent intent=new Intent(this, SignInSignUpActivity.class);
                intent.putExtra(CommonParams.FROM, CommonParams.NOTHING);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.layout_settings_feedback:
                startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
                break;
            case R.id.layout_settings_protocal:
                Intent intent_protocal=new Intent(SettingsActivity.this, WebActivity.class);
                intent_protocal.putExtra("url", CommonParams.ServiceProtocal);
                startActivity(intent_protocal);
                break;
        }
    }

    private void changeRole() {
        SetUserTypeRequest request=new SetUserTypeRequest();
        SetUserTypeRequest.ParamBean paramBean=new SetUserTypeRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            paramBean.setUserType("0");
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            paramBean.setUserType("1");
        }
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setUserType(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(SettingsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                signin();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void signin() {
        SigninRequest request=new SigninRequest();
        SigninRequest.ParamBean paramBean=new SigninRequest.ParamBean();
        paramBean.setPassword(ACache.get(this).getAsString(CommonParams.USER_PASSWORD));
        paramBean.setPhone(ACache.get(this).getAsString(CommonParams.USER_PHONE));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .signin(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<SigninResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(SigninResponse value) {
                dismissNetworkDialog();

                ACache.get(SettingsActivity.this).put(CommonParams.USER_TYPE, value.getUserType());
                ACache.get(SettingsActivity.this).put(CommonParams.USER_ID, value.getUserId());

                Intent intent_main=new Intent(SettingsActivity.this, SignInSignUpActivity.class);
                intent_main.putExtra(CommonParams.FROM, CommonParams.INDEX);
                intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_main);
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String showCacheSize() {
        File dir=new File(InitParams.CACHE_PATH + File.separator + InitParams.FRESCO_CACHE_NAME);
        long cacheSize = FileUtils.getDirLength(dir);
        if (1< cacheSize && cacheSize<1024) {
            return cacheSize+"B";
        }
        else if (1024 <cacheSize &&cacheSize<1024*1024) {
            return cacheSize/1024+"KB";
        }
        else if (1024*1024 <cacheSize &&cacheSize<1024*1024*1024) {
            return cacheSize/1024/1024+"MB";
        }
        else {
            return "0MB";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_settings_cache.setText(showCacheSize());
    }

    private void changeNotificationState() {
        NotificationChangeRequest request=new NotificationChangeRequest();
        NotificationChangeRequest.ParamBean paramBean=new NotificationChangeRequest.ParamBean();
        paramBean.setMsgFlg(sb_settings_message.isChecked()?"1":"0");
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setNotificationState(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(SettingsActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                userChange=true;
                if (sb_settings_message.isChecked()) {
                    sb_settings_message.setChecked(false);
                }
                else {
                    sb_settings_message.setChecked(true);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getPushInfo() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getPushInfo(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<PushResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PushResponse value) {
                if (!value.getMsgFlg().equals("1")) {
                    userChange=true;
                }
                else {
                    userChange=false;
                }
                sb_settings_message.post(() -> sb_settings_message.setChecked(value.getMsgFlg().equals("1")?true:false));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
