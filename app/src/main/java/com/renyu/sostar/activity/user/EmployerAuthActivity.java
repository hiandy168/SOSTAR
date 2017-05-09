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
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.other.UpdateTextInfoActivity;
import com.renyu.sostar.bean.EmployerAuthRequest;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
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
    @BindView(R.id.btn_employerauth_commit)
    Button btn_employerauth_commit;

    // 当前选中图片位置
    private int choicePicPosition=-1;

    OKHttpHelper helper;

    MyCenterEmployerResponse myCenterResponse;

    @Override
    public void initParams() {
        helper=new OKHttpHelper();

        myCenterResponse= (MyCenterEmployerResponse) getIntent().getSerializableExtra("response");
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("企业认证");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        if (!TextUtils.isEmpty(myCenterResponse.getCompanyName())) {
            tv_employerauth_name.setText(myCenterResponse.getCompanyName());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getContactPhone())) {
            tv_employerauth_phone.setText(myCenterResponse.getContactPhone());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getCompanyCode())) {
            tv_employerauth_compcode.setText(myCenterResponse.getCompanyCode());
        }
        if (!TextUtils.isEmpty(myCenterResponse.getCerPath())) {
            iv_employerauth_pic1.setTag(myCenterResponse.getCerPath());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myCenterResponse.getCerPath()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController1 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic1.setController(draweeController1);
            tv_employerauth_pic1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getLicPath())) {
            iv_employerauth_pic2.setTag(myCenterResponse.getLicPath());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myCenterResponse.getLicPath()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController2 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic2.setController(draweeController2);
            tv_employerauth_pic2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getRifPath())) {
            iv_employerauth_pic3.setTag(myCenterResponse.getRifPath());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(myCenterResponse.getRifPath()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController3 = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic3.setController(draweeController3);
            tv_employerauth_pic3.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
            btn_employerauth_commit.setVisibility(View.GONE);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_employerauth_commit, R.id.layout_employerauth_pic1,
            R.id.layout_employerauth_pic2, R.id.layout_employerauth_pic3,
            R.id.layout_employerauth_name, R.id.layout_employerauth_phone,
            R.id.layout_employerauth_compcode, R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.btn_employerauth_commit:
                commitAuth();
                break;
            case R.id.layout_employerauth_pic1:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
                choicePicPosition=1;
                choicePic();
                break;
            case R.id.layout_employerauth_pic2:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
                choicePicPosition=2;
                choicePic();
                break;
            case R.id.layout_employerauth_pic3:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
                choicePicPosition=3;
                choicePic();
                break;
            case R.id.layout_employerauth_name:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
                Intent intent_employerauth_name=new Intent(EmployerAuthActivity.this, UpdateTextInfoActivity.class);
                intent_employerauth_name.putExtra("title", "企业名称");
                intent_employerauth_name.putExtra("param", "companyName");
                intent_employerauth_name.putExtra("needcommit", false);
                intent_employerauth_name.putExtra("source", tv_employerauth_name.getText().toString());
                startActivityForResult(intent_employerauth_name, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerauth_phone:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
                Intent intent_employerauth_phone=new Intent(EmployerAuthActivity.this, UpdateTextInfoActivity.class);
                intent_employerauth_phone.putExtra("title", "联系方式");
                intent_employerauth_phone.putExtra("param", "contactPhone");
                intent_employerauth_phone.putExtra("needcommit", false);
                intent_employerauth_phone.putExtra("source", tv_employerauth_phone.getText().toString());
                startActivityForResult(intent_employerauth_phone, CommonParams.RESULT_UPDATEUSERINFO);
                break;
            case R.id.layout_employerauth_compcode:
                if (!TextUtils.isEmpty(myCenterResponse.getAuthentication()) && myCenterResponse.getAuthentication().equals("1")) {
                    return;
                }
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
            uploadFile(path, choicePicPosition);
        }
    }

    private void uploadFile(String path, int uploadPicPosition) {
        if (uploadPicPosition==1) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://"+path))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic1.setController(draweeController);
            tv_employerauth_pic1.setVisibility(View.GONE);
        }
        else if (uploadPicPosition==2) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://"+path))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic2.setController(draweeController);
            tv_employerauth_pic2.setVisibility(View.GONE);
        }
        else if (uploadPicPosition==3) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://"+path))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request).setAutoPlayAnimations(true).build();
            iv_employerauth_pic3.setController(draweeController);
            tv_employerauth_pic3.setVisibility(View.GONE);
        }
        String url="http://106.15.46.105:9333/submit"+"?pos="+uploadPicPosition;
        HashMap<String, File> fileHashMap=new HashMap<>();
        fileHashMap.put("image", new File(path));
        helper.asyncUpload(fileHashMap, url, new HashMap<>(), () -> {
            helper.cancel(url);
        }, new OKHttpHelper.RequestListener() {
            @Override
            public void onSuccess(String string) {
                Toast.makeText(EmployerAuthActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                Gson gson=new Gson();
                UploadResponse response=gson.fromJson(string, UploadResponse.class);
                String imageUrl="http://106.15.46.105:8081/"+response.getFid();
                if (uploadPicPosition==1) {
                    iv_employerauth_pic1.setTag(imageUrl);
                }
                else if (uploadPicPosition==2) {
                    iv_employerauth_pic2.setTag(imageUrl);
                }
                else if (uploadPicPosition==3) {
                    iv_employerauth_pic3.setTag(imageUrl);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(EmployerAuthActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                if (uploadPicPosition==1) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+R.drawable.shape_rounded_solid_stroke_white))
                            .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic1.setController(draweeController);
                    tv_employerauth_pic1.setVisibility(View.VISIBLE);
                    tv_employerauth_pic1.setTag("");
                }
                else if (uploadPicPosition==2) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+R.drawable.shape_rounded_solid_stroke_white))
                            .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic2.setController(draweeController);
                    tv_employerauth_pic2.setVisibility(View.VISIBLE);
                    tv_employerauth_pic2.setTag("");
                }
                else if (uploadPicPosition==3) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+R.drawable.shape_rounded_solid_stroke_white))
                            .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(55), SizeUtils.dp2px(55))).build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request).setAutoPlayAnimations(true).build();
                    iv_employerauth_pic3.setController(draweeController);
                    tv_employerauth_pic3.setVisibility(View.VISIBLE);
                    tv_employerauth_pic3.setTag("");
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
        EmployerAuthRequest request=new EmployerAuthRequest();
        EmployerAuthRequest.ParamBean paramBean=new EmployerAuthRequest.ParamBean();
        paramBean.setContactPhone(tv_employerauth_phone.getText().toString());
        paramBean.setCompanyName(tv_employerauth_name.getText().toString());
        paramBean.setCompanyCode(tv_employerauth_compcode.getText().toString());
        paramBean.setCerPath(iv_employerauth_pic1.getTag().toString());
        paramBean.setLicPath(iv_employerauth_pic2.getTag().toString());
        paramBean.setRifPath(iv_employerauth_pic3.getTag().toString());
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .setEmployerAuthentica(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                showNetworkDialog("正在操作，请稍后");
            }

            @Override
            public void onNext(EmptyResponse value) {
                dismissNetworkDialog();

                Toast.makeText(EmployerAuthActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                myCenterResponse.setAuthentication("2");
                myCenterResponse.setCompanyName(tv_employerauth_name.getText().toString());
                myCenterResponse.setCompanyCode(tv_employerauth_compcode.getText().toString());
                myCenterResponse.setContactPhone(tv_employerauth_phone.getText().toString());
                myCenterResponse.setCerPath(iv_employerauth_pic1.getTag().toString());
                myCenterResponse.setLicPath(iv_employerauth_pic2.getTag().toString());
                myCenterResponse.setRifPath(iv_employerauth_pic3.getTag().toString());
                onBackPressed();
            }

            @Override
            public void onError(Throwable e) {
                dismissNetworkDialog();

                Toast.makeText(EmployerAuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void choicePic() {
        View view_clearmessage= LayoutInflater.from(EmployerAuthActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置头像")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("拍照");
        pop_three_choice1.setOnClickListener(v -> {
            Utils.takePicture(EmployerAuthActivity.this, CommonParams.RESULT_TAKEPHOTO);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("从相册获取");
        pop_three_choice2.setOnClickListener(v -> {
            Utils.choicePic(EmployerAuthActivity.this, 1, CommonParams.RESULT_ALUMNI);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("取消");
        pop_three_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
    }
}
