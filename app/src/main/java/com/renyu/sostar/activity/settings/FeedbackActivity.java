package com.renyu.sostar.activity.settings;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.FeedbackRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/28.
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.ed_feedback)
    EditText ed_feedback;

    Disposable disposable;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_feedback;
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

    @OnClick({R.id.btn_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_feedback:
                sendFeedback();
                break;
        }
    }

    private void sendFeedback() {
        if (TextUtils.isEmpty(ed_feedback.getText().toString())) {
            Toast.makeText(this, "请填写相关意见", Toast.LENGTH_SHORT).show();
            return;
        }
        FeedbackRequest request=new FeedbackRequest();
        FeedbackRequest.ParamBean paramBean=new FeedbackRequest.ParamBean();
        paramBean.setSuggest(ed_feedback.getText().toString());
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setSuggest(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(FeedbackActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
