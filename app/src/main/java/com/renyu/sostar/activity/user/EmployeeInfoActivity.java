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

import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.commonlibrary.views.ActionSheetUtils;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
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
 * Created by renyu on 2017/2/17.
 */

public class EmployeeInfoActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_employeeinfo_name)
    TextView tv_employeeinfo_name;
    @BindView(R.id.tv_employeeinfo_summary)
    TextView tv_employeeinfo_summary;
    @BindView(R.id.tv_employeeinfo_age)
    TextView tv_employeeinfo_age;
    @BindView(R.id.tv_employeeinfo_evaluate)
    TextView tv_employeeinfo_evaluate;
    @BindView(R.id.tv_employeeinfo_completionrate)
    TextView tv_employeeinfo_completionrate;
    @BindView(R.id.iv_employeeinfo_avatar)
    SimpleDraweeView iv_employeeinfo_avatar;
    @BindView(R.id.tv_employeeinfo_auth)
    TextView tv_employeeinfo_auth;
    @BindView(R.id.tv_employeeinfo_sex)
    TextView tv_employeeinfo_sex;
    @BindView(R.id.iv_employeeinfo_auth)
    ImageView iv_employeeinfo_auth;

    MyCenterEmployeeResponse myCenterResponse;

    @Override
    public void initParams() {
        myCenterResponse= (MyCenterEmployeeResponse) getIntent().getSerializableExtra("response");
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("个人资料");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        if (!TextUtils.isEmpty(myCenterResponse.getPicPath())) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myCenterResponse.getPicPath()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(27), SizeUtils.dp2px(27))).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employeeinfo_avatar.setController(draweeController);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getNickName())) {
            tv_employeeinfo_name.setText(myCenterResponse.getNickName());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getIntroduction())) {
            tv_employeeinfo_summary.setText(myCenterResponse.getIntroduction());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getAge())) {
            tv_employeeinfo_age.setText(myCenterResponse.getAge());
        }
        if (TextUtils.isEmpty(myCenterResponse.getAuthentication()) || myCenterResponse.getAuthentication().equals("0")) {
            tv_employeeinfo_auth.setText("未认证");
            iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfonoauth);
        }
        else if (myCenterResponse.getAuthentication().equals("1")) {
            tv_employeeinfo_auth.setText("已认证");
            iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfoauthed);
        }
        else if (myCenterResponse.getAuthentication().equals("2")) {
            tv_employeeinfo_auth.setText("认证中");
            iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfoauthing);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getSex())) {
            if (myCenterResponse.getSex().equals("1")) {
                tv_employeeinfo_sex.setText("男");
            }
            else {
                tv_employeeinfo_sex.setText("女");
            }
        }
        tv_employeeinfo_evaluate.setText(TextUtils.isEmpty(myCenterResponse.getEvaluateLevel())?"0":myCenterResponse.getEvaluateLevel());
        tv_employeeinfo_completionrate.setText(TextUtils.isEmpty(myCenterResponse.getCloseRate())?"0%":myCenterResponse.getCloseRate());
    }

    @Override
    public int initViews() {
        return R.layout.activity_employeeinfo;
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

    @OnClick({R.id.ib_nav_left, R.id.layout_employeeinfo_age, R.id.layout_employeeinfo_avatar,
            R.id.layout_employeeinfo_name, R.id.layout_employeeinfo_summary,
            R.id.layout_employeeinfo_auth, R.id.tv_employeeinfo_sex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                onBackPressed();
                break;
            case R.id.layout_employeeinfo_age:
                ActionSheetUtils.showBeforeDate(EmployeeInfoActivity.this.getSupportFragmentManager(),
                        "生日",
                        "取消",
                        "完成",
                        value -> {
                            String string = value.toString();
                            String year = string.split("-")[0];
                            String month = Integer.parseInt(string.split("-")[1]) < 10 ? "0" + string.split("-")[1] : string.split("-")[1];
                            String day = Integer.parseInt(string.split("-")[2]) < 10 ? "0" + string.split("-")[2] : string.split("-")[2];
                            updateTextInfo("age", year + "-" + month + "-" + day);
                        }, () -> {

                        }
                );
                break;
            case R.id.layout_employeeinfo_avatar:
                choicePic();
                break;
            case R.id.layout_employeeinfo_name:
                Intent intent_updatename=new Intent(EmployeeInfoActivity.this, UpdateTextInfoActivity.class);
                intent_updatename.putExtra("title", "昵称");
                intent_updatename.putExtra("param", "nickName");
                intent_updatename.putExtra("needcommit", true);
                intent_updatename.putExtra("source", tv_employeeinfo_name.getText().toString());
                startActivityForResult(intent_updatename, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employeeinfo_summary:
                Intent intent_summary=new Intent(EmployeeInfoActivity.this, UpdateTextInfoActivity.class);
                intent_summary.putExtra("title", "简介");
                intent_summary.putExtra("param", "introduction");
                intent_summary.putExtra("needcommit", true);
                intent_summary.putExtra("source", tv_employeeinfo_summary.getText().toString());
                startActivityForResult(intent_summary, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employeeinfo_auth:
                Intent intent_info=new Intent(EmployeeInfoActivity.this, EmployeeAuthActivity.class);
                intent_info.putExtra("response", myCenterResponse);
                startActivityForResult(intent_info, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.tv_employeeinfo_sex:
                ActionSheetUtils.showList(EmployeeInfoActivity.this.getSupportFragmentManager(), "性别",
                        new String[]{"男", "女"}, position -> updateTextInfo("sex", ""+(position+1)),
                        () -> {

                        });
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            Utils.cropImage(path, EmployeeInfoActivity.this, CommonParams.RESULT_CROP, 1);
        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {
            ArrayList<String> filePaths=data.getExtras().getStringArrayList("choiceImages");
            if (filePaths==null) {
                return;
            }
            if (filePaths.size()!=1) {
                return;
            }
            Utils.cropImage(filePaths.get(0), EmployeeInfoActivity.this, CommonParams.RESULT_CROP, 1);
        }
        if (requestCode==CommonParams.RESULT_CROP && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            uploadFile(path);
        }
        if (requestCode==CommonParams.RESULT_UPDATEUSERINFO && resultCode==RESULT_OK) {
            if (TextUtils.isEmpty(data.getStringExtra("param"))) {
                myCenterResponse= (MyCenterEmployeeResponse) data.getSerializableExtra("value");
                if (TextUtils.isEmpty(myCenterResponse.getAuthentication()) || myCenterResponse.getAuthentication().equals("0")) {
                    tv_employeeinfo_auth.setText("未认证");
                    iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfonoauth);
                }
                else if (myCenterResponse.getAuthentication().equals("1")) {
                    tv_employeeinfo_auth.setText("已认证");
                    iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfoauthed);
                }
                else if (myCenterResponse.getAuthentication().equals("2")) {
                    tv_employeeinfo_auth.setText("认证中");
                    iv_employeeinfo_auth.setImageResource(R.mipmap.ic_userinfoauthing);
                }
            }
            else if (data.getStringExtra("param").equals("nickName")) {
                tv_employeeinfo_name.setText(data.getStringExtra("value"));
                myCenterResponse.setNickName(data.getStringExtra("value"));
            }
            else if (data.getStringExtra("param").equals("introduction")) {
                tv_employeeinfo_summary.setText(data.getStringExtra("value"));
                myCenterResponse.setIntroduction(data.getStringExtra("value"));
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
                    .setStaffInfo(Retrofit2Utils.postJsonPrepare(jsonObject.toString()))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    showNetworkDialog("正在操作，请稍后");
                }

                @Override
                public void onNext(EmptyResponse value) {
                    dismissNetworkDialog();

                    Toast.makeText(EmployeeInfoActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                    if (param.equals("picPath")) {
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(paramValue))
                                .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(27), SizeUtils.dp2px(27))).build();
                        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request).setAutoPlayAnimations(true).build();
                        iv_employeeinfo_avatar.setController(draweeController);
                        myCenterResponse.setPicPath(paramValue);
                    }
                    if (param.equals("age")) {
                        tv_employeeinfo_age.setText(paramValue);
                        myCenterResponse.setAge(paramValue);
                    }
                    if (param.equals("sex")) {
                        if (paramValue.equals("1")) {
                            tv_employeeinfo_sex.setText("男");
                        }
                        else {
                            tv_employeeinfo_sex.setText("女");
                        }
                        myCenterResponse.setSex(param);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    dismissNetworkDialog();

                    Toast.makeText(EmployeeInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        helper.asyncUpload(fileHashMap, "http://106.15.46.105:9333/submit", new HashMap<>(), () -> {

        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Gson gson=new Gson();
                UploadResponse response=gson.fromJson(string, UploadResponse.class);
                String imageUrl="http://106.15.46.105:8081/"+response.getFid();
                updateTextInfo("picPath", imageUrl);
                myCenterResponse.setPicPath(imageUrl);
            }

            @Override
            public void onError() {
                Toast.makeText(EmployeeInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choicePic() {
        View view_clearmessage= LayoutInflater.from(EmployeeInfoActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置头像")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("拍照");
        pop_three_choice1.setOnClickListener(v -> {
            Utils.takePicture(EmployeeInfoActivity.this, CommonParams.RESULT_TAKEPHOTO);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("从相册获取");
        pop_three_choice2.setOnClickListener(v -> {
            Utils.choicePic(EmployeeInfoActivity.this, 1, CommonParams.RESULT_ALUMNI);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("取消");
        pop_three_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
    }
}
