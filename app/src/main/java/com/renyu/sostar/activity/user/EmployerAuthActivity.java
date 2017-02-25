package com.renyu.sostar.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.networkutils.OKHttpHelper;
import com.renyu.commonlibrary.views.ActionSheetUtils;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.UploadResponse;
import com.renyu.sostar.params.CommonParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/25.
 */

public class EmployerAuthActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_employerauth_name)
    TextView tv_employerauth_name;
    @BindView(R.id.tv_employerauth_phone)
    TextView tv_employerauth_phone;
    @BindView(R.id.tv_employerauth_compcode)
    TextView tv_employerauth_compcode;
    @BindView(R.id.iv_employerauth_pic1)
    SimpleDraweeView iv_employerauth_pic1;
    @BindView(R.id.iv_employerauth_pic2)
    SimpleDraweeView iv_employerauth_pic2;
    @BindView(R.id.iv_employerauth_pic3)
    SimpleDraweeView iv_employerauth_pic3;
    @BindView(R.id.tv_employerauth_pic1)
    TextView tv_employerauth_pic1;
    @BindView(R.id.tv_employerauth_pic2)
    TextView tv_employerauth_pic2;
    @BindView(R.id.tv_employerauth_pic3)
    TextView tv_employerauth_pic3;

    // 当前选中图片位置
    private int uploadPicPosition=-1;

    Disposable disposable;

    MyCenterEmployerResponse myCenterResponse;

    @Override
    public void initParams() {
        myCenterResponse= (MyCenterEmployerResponse) getIntent().getSerializableExtra("response");
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("企业认证");

        if (!TextUtils.isEmpty(myCenterResponse.getCompanyName())) {
            tv_employerauth_name.setText(myCenterResponse.getCompanyName());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getContactPhone())) {
            tv_employerauth_phone.setText(myCenterResponse.getContactPhone());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getCompanyId())) {
            tv_employerauth_compcode.setText(myCenterResponse.getCompanyId());
        }
    }

    @Override
    public int initViews() {
        return R.layout.activity_employerauth;
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

    @OnClick({R.id.btn_employerauth_commit, R.id.layout_employerauth_pic1,
            R.id.layout_employerauth_pic2, R.id.layout_employerauth_pic3,
            R.id.layout_employerauth_name, R.id.layout_employerauth_phone,
            R.id.layout_employerauth_compcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_employerauth_commit:
                commitAuth();
                break;
            case R.id.layout_employerauth_pic1:
                uploadPicPosition=1;
                ActionSheetUtils.showCamera(EmployerAuthActivity.this.getSupportFragmentManager(),
                        "添加1", new String[]{"拍照", "从相册获取"},
                        position -> {
                            if (position==0) {
                                Utils.takePicture(EmployerAuthActivity.this, CommonParams.RESULT_TAKEPHOTO);
                            }
                            else if (position==1) {
                                Utils.choicePic(EmployerAuthActivity.this, 1, CommonParams.RESULT_ALUMNI);
                            }
                        }, "取消", () -> {

                        });
                break;
            case R.id.layout_employerauth_pic2:
                ActionSheetUtils.showCamera(EmployerAuthActivity.this.getSupportFragmentManager(),
                        "添加2", new String[]{"拍照", "从相册获取"},
                        position -> {
                            if (position==0) {
                                Utils.takePicture(EmployerAuthActivity.this, CommonParams.RESULT_TAKEPHOTO);
                            }
                            else if (position==1) {
                                Utils.choicePic(EmployerAuthActivity.this, 1, CommonParams.RESULT_ALUMNI);
                            }
                        }, "取消", () -> {

                        });
                break;
            case R.id.layout_employerauth_pic3:
                ActionSheetUtils.showCamera(EmployerAuthActivity.this.getSupportFragmentManager(),
                        "添加3", new String[]{"拍照", "从相册获取"},
                        position -> {
                            if (position==0) {
                                Utils.takePicture(EmployerAuthActivity.this, CommonParams.RESULT_TAKEPHOTO);
                            }
                            else if (position==1) {
                                Utils.choicePic(EmployerAuthActivity.this, 1, CommonParams.RESULT_ALUMNI);
                            }
                        }, "取消", () -> {

                        });
                break;
            case R.id.layout_employerauth_name:
                Intent intent_employerauth_name=new Intent(EmployerAuthActivity.this, UpdateTextInfoActivity.class);
                intent_employerauth_name.putExtra("title", "企业名称");
                intent_employerauth_name.putExtra("param", "companyName");
                intent_employerauth_name.putExtra("needcommit", false);
                intent_employerauth_name.putExtra("source", tv_employerauth_name.getText().toString());
                startActivityForResult(intent_employerauth_name, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerauth_phone:
                Intent intent_employerauth_phone=new Intent(EmployerAuthActivity.this, UpdateTextInfoActivity.class);
                intent_employerauth_phone.putExtra("title", "联系方式");
                intent_employerauth_phone.putExtra("param", "contactPhone");
                intent_employerauth_phone.putExtra("needcommit", false);
                intent_employerauth_phone.putExtra("source", tv_employerauth_phone.getText().toString());
                startActivityForResult(intent_employerauth_phone, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerauth_compcode:
                Intent intent_employerauth_compcode=new Intent(EmployerAuthActivity.this, UpdateTextInfoActivity.class);
                intent_employerauth_compcode.putExtra("title", "机构识别代码");
                intent_employerauth_compcode.putExtra("param", "companyCode");
                intent_employerauth_compcode.putExtra("needcommit", false);
                intent_employerauth_compcode.putExtra("source", tv_employerauth_compcode.getText().toString());
                startActivityForResult(intent_employerauth_compcode, CommonParams.RESULT_UPDATEUSERINFO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==CommonParams.RESULT_UPDATEUSERINFO) {
            if (data.getStringExtra("param").equals("companyName")) {
                tv_employerauth_name.setText(data.getStringExtra("value"));
            }
            if (data.getStringExtra("param").equals("contactPhone")) {
                tv_employerauth_phone.setText(data.getStringExtra("value"));
            }
            if (data.getStringExtra("param").equals("companyCode")) {
                tv_employerauth_compcode.setText(data.getStringExtra("value"));
            }
        }
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            Utils.cropImage(path, EmployerAuthActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {
            ArrayList<String> filePaths=data.getExtras().getStringArrayList("choiceImages");
            if (filePaths==null) {
                return;
            }
            if (filePaths.size()!=1) {
                return;
            }
            Utils.cropImage(filePaths.get(0), EmployerAuthActivity.this, CommonParams.RESULT_CROP);
        }
        if (requestCode==CommonParams.RESULT_CROP && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            uploadFile(path);
        }
    }

    private void uploadFile(String path) {
        HashMap<String, File> fileHashMap=new HashMap<>();
        fileHashMap.put("image", new File(path));
        OKHttpHelper helper=new OKHttpHelper();
        helper.asyncUpload(fileHashMap, "http://114.215.18.160:9333/submit", new HashMap<>(), () -> {

        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Gson gson=new Gson();
                UploadResponse response=gson.fromJson(string, UploadResponse.class);
                String imageUrl="http://114.215.18.160:8081/"+response.getFid();
                if (uploadPicPosition==1) {
                    iv_employerauth_pic1.setTag(imageUrl);
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(imageUrl)).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic1.setController(draweeController);
                    tv_employerauth_pic1.setVisibility(View.GONE);
                }
                else if (uploadPicPosition==2) {
                    iv_employerauth_pic2.setTag(imageUrl);
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(imageUrl)).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic2.setController(draweeController);
                    tv_employerauth_pic2.setVisibility(View.GONE);
                }
                else if (uploadPicPosition==3) {
                    iv_employerauth_pic3.setTag(imageUrl);
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(imageUrl)).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic3.setController(draweeController);
                    tv_employerauth_pic3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(EmployerAuthActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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

    private void commitAuth() {
        if (TextUtils.isEmpty(tv_employerauth_phone.getText().toString())) {
            Toast.makeText(this, "请填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_employerauth_name.getText().toString())) {
            Toast.makeText(this, "请填写企业名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_employerauth_compcode.getText().toString())) {
            Toast.makeText(this, "请填写机构识别代码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv_employerauth_pic1.getTag()==null) {
            Toast.makeText(this, "请添加照片1", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv_employerauth_pic2.getTag()==null) {
            Toast.makeText(this, "请添加照片2", Toast.LENGTH_SHORT).show();
            return;
        }
        if (iv_employerauth_pic3.getTag()==null) {
            Toast.makeText(this, "请添加照片3", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
