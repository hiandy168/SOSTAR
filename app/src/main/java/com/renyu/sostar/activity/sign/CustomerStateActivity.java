package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.UserTypeRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/17.
 */

public class CustomerStateActivity extends BaseActivity {

    Disposable disposable;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_customerstate;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.layout_state_employer, R.id.layout_state_employee})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_state_employer:
                choice(1);
                break;
            case R.id.layout_state_employee:
                choice(0);
                break;
        }
    }

    private void choice(int state) {
        UserTypeRequest request=new UserTypeRequest();
        UserTypeRequest.ParamBean paramBean=new UserTypeRequest.ParamBean();
        paramBean.setUserId(ACache.get(CustomerStateActivity.this).getAsString(CommonParams.USER_ID));
        paramBean.setPhone(ACache.get(CustomerStateActivity.this).getAsString(CommonParams.USER_PHONE));
        paramBean.setUserType(""+state);
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setUserState(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(CustomerStateActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                ACache.get(CustomerStateActivity.this).put(CommonParams.USER_TYPE, ""+state);

                Intent intent_sisu=new Intent(CustomerStateActivity.this, SignInSignUpActivity.class);
                intent_sisu.putExtra(CommonParams.FROM, CommonParams.INDEX);
                intent_sisu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_sisu);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CustomerStateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
