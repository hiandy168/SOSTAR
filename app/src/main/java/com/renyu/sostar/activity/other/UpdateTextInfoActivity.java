package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/23.
 */

public class UpdateTextInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.et_updatetextinfo)
    EditText et_updatetextinfo;

    Disposable disposable;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        et_updatetextinfo.setText(getIntent().getStringExtra("source"));
        et_updatetextinfo.setSelection(getIntent().getStringExtra("source").length());
    }

    @Override
    public int initViews() {
        return R.layout.activity_updatetextinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_right:
                updateTextInfo();
                break;
            case R.id.ib_nav_left:
                finish();
        }
    }

    private void updateTextInfo() {
        if (TextUtils.isEmpty(et_updatetextinfo.getText().toString())) {
            Toast.makeText(this, "请输入" + getIntent().getStringExtra("title"), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!getIntent().getBooleanExtra("needcommit", false)) {
            Intent intent=new Intent();
            intent.putExtra("param", getIntent().getStringExtra("param"));
            intent.putExtra("value", et_updatetextinfo.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceId", Utils.getUniquePsuedoID());
            jsonObject.put("ver", BuildConfig.VERSION_NAME);
            JSONObject childJsonObject=new JSONObject();
            childJsonObject.put(getIntent().getStringExtra("param"), et_updatetextinfo.getText().toString());
            childJsonObject.put("userId", ACache.get(this).getAsString(CommonParams.USER_ID));
            jsonObject.put("param", childJsonObject);
            if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                retrofit.create(RetrofitImpl.class)
                        .setEmployerInfo(Retrofit2Utils.postJsonPrepare(jsonObject.toString()))
                        .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(EmptyResponse value) {
                        Toast.makeText(UpdateTextInfoActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("param", getIntent().getStringExtra("param"));
                        intent.putExtra("value", et_updatetextinfo.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UpdateTextInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
            else {
                retrofit.create(RetrofitImpl.class)
                        .setStaffInfo(Retrofit2Utils.postJsonPrepare(jsonObject.toString()))
                        .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(EmptyResponse value) {
                        Toast.makeText(UpdateTextInfoActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("param", getIntent().getStringExtra("param"));
                        intent.putExtra("value", et_updatetextinfo.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UpdateTextInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
