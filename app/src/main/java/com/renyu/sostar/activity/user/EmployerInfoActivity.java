package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.UploadResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/25.
 */

public class EmployerInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.iv_employerinfo_avatar)
    SimpleDraweeView iv_employerinfo_avatar;
    @BindView(R.id.tv_employerinfo_name)
    TextView tv_employerinfo_name;
    @BindView(R.id.tv_employerinfo_phone)
    TextView tv_employerinfo_phone;
    @BindView(R.id.tv_employerinfo_web)
    TextView tv_employerinfo_web;
    @BindView(R.id.tv_employerinfo_summary)
    TextView tv_employerinfo_summary;
    @BindView(R.id.tv_employerinfo_evaluate)
    TextView tv_employerinfo_evaluate;
    @BindView(R.id.tv_employerinfo_completionrate)
    TextView tv_employerinfo_completionrate;
    @BindView(R.id.tv_employerinfo_auth)
    TextView tv_employerinfo_auth;
    @BindView(R.id.iv_employerinfo_auth)
    ImageView iv_employerinfo_auth;

    MyCenterEmployerResponse myCenterResponse;

    Disposable disposable;

    @Override
    public void initParams() {
        myCenterResponse= (MyCenterEmployerResponse) getIntent().getSerializableExtra("response");
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("企业信息");

        if (!TextUtils.isEmpty(myCenterResponse.getLogoPath())) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(myCenterResponse.getLogoPath())).setAutoPlayAnimations(true).build();
            iv_employerinfo_avatar.setController(draweeController);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getCompanyName())) {
            tv_employerinfo_name.setText(myCenterResponse.getCompanyName());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getContactPhone())) {
            tv_employerinfo_phone.setText(myCenterResponse.getContactPhone());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getIntroduction())) {
            tv_employerinfo_summary.setText(myCenterResponse.getIntroduction());
        }
        if (TextUtils.isEmpty(myCenterResponse.getAuthentication()) || myCenterResponse.getAuthentication().equals("0")) {
            tv_employerinfo_auth.setText("未认证");
            iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfonoauth);
        }
        else if (myCenterResponse.getAuthentication().equals("1")) {
            tv_employerinfo_auth.setText("已认证");
            iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfoauthed);
        }
        else if (myCenterResponse.getAuthentication().equals("2")) {
            tv_employerinfo_auth.setText("认证中");
            iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfoauthing);
        }
        tv_employerinfo_web.setText(myCenterResponse.getWebAddress());
        tv_employerinfo_evaluate.setText(TextUtils.isEmpty(myCenterResponse.getStar())?"0":myCenterResponse.getStar());
        tv_employerinfo_completionrate.setText(TextUtils.isEmpty(myCenterResponse.getCloseRate())?"0%":myCenterResponse.getCloseRate()+"%");
    }

    @Override
    public int initViews() {
        return R.layout.activity_employerinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.layout_employerinfo_name, R.id.layout_employerinfo_avatar,
            R.id.layout_employerinfo_web, R.id.layout_employerinfo_auth, R.id.layout_employerinfo_summary,
            R.id.layout_employerinfo_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                onBackPressed();
                break;
            case R.id.layout_employerinfo_name:
                Intent intent_updatename=new Intent(EmployerInfoActivity.this, UpdateTextInfoActivity.class);
                intent_updatename.putExtra("title", "企业名称");
                intent_updatename.putExtra("param", "companyName");
                intent_updatename.putExtra("needcommit", true);
                intent_updatename.putExtra("source", tv_employerinfo_name.getText().toString());
                startActivityForResult(intent_updatename, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerinfo_avatar:
                choicePic();
                break;
            case R.id.layout_employerinfo_phone:
                Intent intent_updatephone=new Intent(EmployerInfoActivity.this, UpdateTextInfoActivity.class);
                intent_updatephone.putExtra("title", "联系方式");
                intent_updatephone.putExtra("param", "contactPhone");
                intent_updatephone.putExtra("needcommit", true);
                intent_updatephone.putExtra("source", tv_employerinfo_phone.getText().toString());
                startActivityForResult(intent_updatephone, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerinfo_web:
                Intent intent_web=new Intent(EmployerInfoActivity.this, UpdateTextInfoActivity.class);
                intent_web.putExtra("title", "公司网站");
                intent_web.putExtra("param", "webAddress");
                intent_web.putExtra("needcommit", true);
                intent_web.putExtra("source", tv_employerinfo_web.getText().toString());
                startActivityForResult(intent_web, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerinfo_auth:
                Intent intent_auth=new Intent(EmployerInfoActivity.this, EmployerAuthActivity.class);
                intent_auth.putExtra("response", myCenterResponse);
                startActivityForResult(intent_auth, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerinfo_summary:
                Intent intent_summary=new Intent(EmployerInfoActivity.this, UpdateTextInfoActivity.class);
                intent_summary.putExtra("title", "简介");
                intent_summary.putExtra("param", "introduction");
                intent_summary.putExtra("needcommit", true);
                intent_summary.putExtra("source", tv_employerinfo_summary.getText().toString());
                startActivityForResult(intent_summary, CommonParams.RESULT_UPDATEUSERINFO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            Utils.cropImage(path, EmployerInfoActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {
            ArrayList<String> filePaths=data.getExtras().getStringArrayList("choiceImages");
            if (filePaths==null) {
                return;
            }
            if (filePaths.size()!=1) {
                return;
            }
            Utils.cropImage(filePaths.get(0), EmployerInfoActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_CROP && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            uploadFile(path);
        }
        if (requestCode==CommonParams.RESULT_UPDATEUSERINFO && resultCode==RESULT_OK) {
            if (TextUtils.isEmpty(data.getStringExtra("param"))) {
                myCenterResponse= (MyCenterEmployerResponse) data.getSerializableExtra("value");
                if (TextUtils.isEmpty(myCenterResponse.getAuthentication()) || myCenterResponse.getAuthentication().equals("0")) {
                    tv_employerinfo_auth.setText("未认证");
                    iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfonoauth);
                }
                else if (myCenterResponse.getAuthentication().equals("1")) {
                    tv_employerinfo_auth.setText("已认证");
                    iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfoauthed);
                }
                else if (myCenterResponse.getAuthentication().equals("2")) {
                    tv_employerinfo_auth.setText("认证中");
                    iv_employerinfo_auth.setImageResource(R.mipmap.ic_userinfoauthing);
                }
                tv_employerinfo_name.setText(myCenterResponse.getCompanyName());
                tv_employerinfo_phone.setText(myCenterResponse.getContactPhone());
            }
            else if (data.getStringExtra("param").equals("companyName")) {
                tv_employerinfo_name.setText(data.getStringExtra("value"));
                myCenterResponse.setCompanyName(data.getStringExtra("value"));
            }
            else if (data.getStringExtra("param").equals("introduction")) {
                tv_employerinfo_summary.setText(data.getStringExtra("value"));
                myCenterResponse.setIntroduction(data.getStringExtra("value"));
            }
            else if (data.getStringExtra("param").equals("contactPhone")) {
                tv_employerinfo_phone.setText(data.getStringExtra("value"));
                myCenterResponse.setContactPhone(data.getStringExtra("value"));
            }
            else if (data.getStringExtra("param").equals("webAddress")) {
                tv_employerinfo_web.setText(data.getStringExtra("value"));
                myCenterResponse.setWebAddress(data.getStringExtra("value"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent=new Intent();
        intent.putExtra("value", myCenterResponse);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateTextInfo(String param, String paramValue) {
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceId", com.renyu.commonlibrary.commonutils.Utils.getUniquePsuedoID());
            jsonObject.put("ver", BuildConfig.VERSION_NAME);
            JSONObject childJsonObject=new JSONObject();
            childJsonObject.put(param, paramValue);
            childJsonObject.put("userId", ACache.get(this).getAsString(CommonParams.USER_ID));
            jsonObject.put("param", childJsonObject);
            retrofit.create(RetrofitImpl.class)
                    .setEmployerInfo(Retrofit2Utils.postJsonPrepare(jsonObject.toString()))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable=d;
                }

                @Override
                public void onNext(EmptyResponse value) {
                    Toast.makeText(EmployerInfoActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                    if (param.equals("logoPath")) {
                        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                .setUri(Uri.parse(paramValue)).setAutoPlayAnimations(true).build();
                        iv_employerinfo_avatar.setController(draweeController);
                        myCenterResponse.setLogoPath(paramValue);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(EmployerInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(String path) {
        HashMap<String, File> fileHashMap=new HashMap<>();
        fileHashMap.put("image", new File(path));
        OKHttpHelper helper=new OKHttpHelper();
        helper.asyncUpload(fileHashMap, "http://114.215.18.160:9333/submit", new HashMap<>(), new OKHttpHelper.StartListener() {
            @Override
            public void onStart() {

            }
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Gson gson=new Gson();
                UploadResponse response=gson.fromJson(string, UploadResponse.class);
                String imageUrl="http://114.215.18.160:8081/"+response.getFid();
                updateTextInfo("logoPath", imageUrl);
                myCenterResponse.setLogoPath(imageUrl);
            }

            @Override
            public void onError() {
                Toast.makeText(EmployerInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choicePic() {
        View view_clearmessage= LayoutInflater.from(EmployerInfoActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置头像")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("拍照");
        pop_three_choice1.setOnClickListener(v -> {
            Utils.takePicture(EmployerInfoActivity.this, CommonParams.RESULT_TAKEPHOTO);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("从相册获取");
        pop_three_choice2.setOnClickListener(v -> {
            Utils.choicePic(EmployerInfoActivity.this, 1, CommonParams.RESULT_ALUMNI);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("取消");
        pop_three_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
    }
}
