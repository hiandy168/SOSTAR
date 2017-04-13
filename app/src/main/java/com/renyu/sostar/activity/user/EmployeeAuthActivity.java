package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.bean.EmployeeAuthRequest;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.UploadResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/24.
 */

public class EmployeeAuthActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_userauth_name)
    TextView tv_userauth_name;
    @BindView(R.id.tv_userauth_phone)
    TextView tv_userauth_phone;
    @BindView(R.id.tv_userauth_id)
    TextView tv_userauth_id;
    @BindView(R.id.iv_userauth_positive)
    SimpleDraweeView iv_userauth_positive;
    @BindView(R.id.tv_userauth_positive)
    TextView tv_userauth_positive;
    @BindView(R.id.iv_userauth_negative)
    SimpleDraweeView iv_userauth_negative;
    @BindView(R.id.tv_userauth_negative)
    TextView tv_userauth_negative;
    @BindView(R.id.btn_userauth_commit)
    Button btn_userauth_commit;

    // 当前选中图片位置
    private int choicePicPosition=-1;

    Disposable disposable;

    MyCenterEmployeeResponse myCenterResponse;

    @Override
    public void initParams() {
        myCenterResponse= (MyCenterEmployeeResponse) getIntent().getSerializableExtra("response");
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("个人认证");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        if (!TextUtils.isEmpty(myCenterResponse.getName())) {
            tv_userauth_name.setText(myCenterResponse.getName());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getCertificateId())) {
            tv_userauth_id.setText(myCenterResponse.getCertificateId());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getPhone())) {
            tv_userauth_phone.setText(myCenterResponse.getPhone());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getPicCerpos())) {
            iv_userauth_positive.setTag(myCenterResponse.getPicCerpos());
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(myCenterResponse.getPicCerpos())).setAutoPlayAnimations(true).build();
            iv_userauth_positive.setController(draweeController);
            tv_userauth_positive.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getPicCerOppo())) {
            iv_userauth_negative.setTag(myCenterResponse.getPicCerOppo());
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(myCenterResponse.getPicCerOppo())).setAutoPlayAnimations(true).build();
            iv_userauth_negative.setController(draweeController);
            tv_userauth_negative.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
            btn_userauth_commit.setVisibility(View.GONE);
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_employeeauth;
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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ib_nav_left, R.id.btn_userauth_commit, R.id.layout_userauth_negative,
            R.id.layout_userauth_positive, R.id.tv_userauth_id, R.id.tv_userauth_phone,
            R.id.tv_userauth_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_userauth_name:
                Intent intent_userauth_name=new Intent(EmployeeAuthActivity.this, UpdateTextInfoActivity.class);
                intent_userauth_name.putExtra("title", "真实姓名");
                intent_userauth_name.putExtra("param", "name");
                intent_userauth_name.putExtra("needcommit", false);
                intent_userauth_name.putExtra("source", tv_userauth_name.getText().toString());
                startActivityForResult(intent_userauth_name, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.tv_userauth_phone:
                Intent intent_userauth_phone=new Intent(EmployeeAuthActivity.this, UpdateTextInfoActivity.class);
                intent_userauth_phone.putExtra("title", "联系电话");
                intent_userauth_phone.putExtra("param", "phone");
                intent_userauth_phone.putExtra("needcommit", false);
                intent_userauth_phone.putExtra("source", tv_userauth_phone.getText().toString());
                startActivityForResult(intent_userauth_phone, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.tv_userauth_id:
                Intent intent_userauth_id=new Intent(EmployeeAuthActivity.this, UpdateTextInfoActivity.class);
                intent_userauth_id.putExtra("title", "身份证号");
                intent_userauth_id.putExtra("param", "certificateId");
                intent_userauth_id.putExtra("needcommit", false);
                intent_userauth_id.putExtra("source", tv_userauth_id.getText().toString());
                startActivityForResult(intent_userauth_id, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_userauth_positive:
                choicePicPosition=1;
                choicePic();
                break;
            case R.id.layout_userauth_negative:
                choicePicPosition=2;
                choicePic();
                break;
            case R.id.btn_userauth_commit:
                commitAuth();
                break;
        }
    }

    private void commitAuth() {
        if (TextUtils.isEmpty(tv_userauth_phone.getText().toString())) {
            Toast.makeText(this, "请填写联系电话", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_userauth_name.getText().toString())) {
            Toast.makeText(this, "请填写真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_userauth_id.getText().toString())) {
            Toast.makeText(this, "请填写身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv_userauth_positive.getTag()==null) {
            Toast.makeText(this, "请添加正面照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv_userauth_negative.getTag()==null) {
            Toast.makeText(this, "请添加反面照片", Toast.LENGTH_SHORT).show();
            return;
        }
        EmployeeAuthRequest request=new EmployeeAuthRequest();
        EmployeeAuthRequest.ParamBean paramBean=new EmployeeAuthRequest.ParamBean();
        paramBean.setPhone(tv_userauth_phone.getText().toString());
        paramBean.setCertificateId(tv_userauth_id.getText().toString());
        paramBean.setName(tv_userauth_name.getText().toString());
        paramBean.setPicCerpos(iv_userauth_positive.getTag().toString());
        paramBean.setPicCerOppo(iv_userauth_negative.getTag().toString());
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setStaffAuthentica(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(EmployeeAuthActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                myCenterResponse.setAuthentication("2");
                myCenterResponse.setName(tv_userauth_name.getText().toString());
                myCenterResponse.setCertificateId(tv_userauth_id.getText().toString());
                myCenterResponse.setPhone(tv_userauth_phone.getText().toString());
                myCenterResponse.setPicCerpos(iv_userauth_positive.getTag().toString());
                myCenterResponse.setPicCerOppo(iv_userauth_negative.getTag().toString());
                onBackPressed();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(EmployeeAuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==CommonParams.RESULT_UPDATEUSERINFO) {
            if (data.getStringExtra("param").equals("name")) {
                tv_userauth_name.setText(data.getStringExtra("value"));
            }
            if (data.getStringExtra("param").equals("phone")) {
                tv_userauth_phone.setText(data.getStringExtra("value"));
                myCenterResponse.setPhone(tv_userauth_phone.getText().toString());
            }
            if (data.getStringExtra("param").equals("certificateId")) {
                tv_userauth_id.setText(data.getStringExtra("value"));
            }
        }
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            Utils.cropImage(path, EmployeeAuthActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {
            ArrayList<String> filePaths=data.getExtras().getStringArrayList("choiceImages");
            if (filePaths==null) {
                return;
            }
            if (filePaths.size()!=1) {
                return;
            }
            Utils.cropImage(filePaths.get(0), EmployeeAuthActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_CROP && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            uploadFile(path, choicePicPosition);
        }
    }

    private void uploadFile(String path, int uploadPicPosition) {
        if (uploadPicPosition==1) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse("file://"+path)).setAutoPlayAnimations(true).build();
            iv_userauth_positive.setController(draweeController);
            tv_userauth_positive.setVisibility(View.GONE);
        }
        else if (uploadPicPosition==2) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse("file://"+path)).setAutoPlayAnimations(true).build();
            iv_userauth_negative.setController(draweeController);
            tv_userauth_negative.setVisibility(View.GONE);
        }
        String url="http://114.215.18.160:9333/submit"+"?pos="+uploadPicPosition;
        HashMap<String, File> fileHashMap=new HashMap<>();
        fileHashMap.put("image", new File(path));
        OKHttpHelper helper=new OKHttpHelper();
        helper.asyncUpload(fileHashMap, url, new HashMap<>(), () -> {
            helper.cancel(url);
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Toast.makeText(EmployeeAuthActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                Gson gson=new Gson();
                UploadResponse response=gson.fromJson(string, UploadResponse.class);
                String imageUrl="http://114.215.18.160:8081/"+response.getFid();
                if (uploadPicPosition==1) {
                    iv_userauth_positive.setTag(imageUrl);
                }
                else if (uploadPicPosition==2) {
                    iv_userauth_negative.setTag(imageUrl);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(EmployeeAuthActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                if (uploadPicPosition==1) {
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse("res:///"+R.drawable.shape_rounded_solid_stroke_white)).setAutoPlayAnimations(true).build();
                    iv_userauth_positive.setController(draweeController);
                    tv_userauth_positive.setVisibility(View.VISIBLE);
                    iv_userauth_positive.setTag("");
                }
                else if (uploadPicPosition==2) {
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse("res:///"+R.drawable.shape_rounded_solid_stroke_white)).setAutoPlayAnimations(true).build();
                    iv_userauth_negative.setController(draweeController);
                    tv_userauth_negative.setVisibility(View.VISIBLE);
                    tv_userauth_negative.setTag("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("value", myCenterResponse);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void choicePic() {
        View view_clearmessage= LayoutInflater.from(EmployeeAuthActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置头像")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("拍照");
        pop_three_choice1.setOnClickListener(v -> {
            Utils.takePicture(EmployeeAuthActivity.this, CommonParams.RESULT_TAKEPHOTO);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("从相册获取");
        pop_three_choice2.setOnClickListener(v -> {
            Utils.choicePic(EmployeeAuthActivity.this, 1, CommonParams.RESULT_ALUMNI);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("取消");
        pop_three_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
    }
}
