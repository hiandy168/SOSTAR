package com.renyu.sostar.activity.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/28.
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ed_feedback)
    EditText ed_feedback;
    @BindView(R.id.tv_feedback_numbers)
    TextView tv_feedback_numbers;

    Disposable disposable;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("意见反馈");
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.btn_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_feedback:
                sendFeedback();
                break;
        }
    }

    @OnTextChanged(value = R.id.ed_feedback, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length()>120) {
            ed_feedback.setText(s.toString().substring(0, 120));
            ed_feedback.setSelection(120);
            tv_feedback_numbers.setText("120/120");
        }
        else {
            tv_feedback_numbers.setText(s.length()+"/120");
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
