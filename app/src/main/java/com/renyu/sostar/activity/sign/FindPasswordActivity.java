package com.renyu.sostar.activity.sign;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.views.ClearEditText;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/13.
 */

public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.findpwd_phone)
    ClearEditText findpwd_phone;
    @BindView(R.id.findpwd_pwd1)
    ClearEditText findpwd_pwd1;
    @BindView(R.id.findpwd_getvcode)
    Button findpwd_getvcode;

    Disposable disposable;

    @Override
    public void initParams() {
        findpwd_phone.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_findpassword;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(FindPasswordActivity.this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @OnClick({R.id.findpwd_commit, R.id.findpwd_getvcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.findpwd_commit:
                ACache.get(FindPasswordActivity.this).put(CommonParams.USER_PASSWORD, findpwd_pwd1.getText().toString());
                Intent intent=new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.findpwd_getvcode:
                findpwd_getvcode.setEnabled(false);
                disposable=Observable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    findpwd_getvcode.setText(""+(60-aLong));
                    if (60-1-aLong==0) {
                        findpwd_getvcode.setEnabled(true);
                        findpwd_getvcode.setText("获取验证码");
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null) {
            disposable.dispose();
        }
    }
}
