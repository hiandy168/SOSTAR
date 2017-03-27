package com.renyu.sostar.activity.other;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.imagelibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/8.
 */

public class UpdateTextInfoWithPicActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.grid_updatetextinfowithpic)
    GridLayout grid_updatetextinfowithpic;
    @BindView(R.id.et_updatetextinfowithpic)
    EditText et_updatetextinfowithpic;

    ArrayList<String> picPath;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText(getIntent().getStringExtra("title"));
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_right.setText("确认");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        et_updatetextinfowithpic.setText(getIntent().getStringExtra("ed"));
        picPath=getIntent().getStringArrayListExtra("picPath");
        if (picPath==null) {
            picPath=new ArrayList<>();
        }
        for (int i=0;i<picPath.size();i++) {
            addImage(picPath.get(i));
        }
        if (picPath.size()<3) {
            addImage("");
        }
    }

    private void addImage(String path) {
        grid_updatetextinfowithpic.post(() -> {
            View view= LayoutInflater.from(UpdateTextInfoWithPicActivity.this)
                    .inflate(R.layout.view_updatetextinfowithpic, null, false);
            ImageView iv_updatetextinfowithpic_delete= (ImageView) view.findViewById(R.id.iv_updatetextinfowithpic_delete);
            iv_updatetextinfowithpic_delete.setOnClickListener(v -> {
                picPath.remove(path);
                grid_updatetextinfowithpic.removeView(view);
                if (picPath.size()<3) {
                    addImage("");
                }
            });
            SimpleDraweeView draweeView= (SimpleDraweeView) view.findViewById(R.id.iv_updatetextinfowithpic);
            TextView textview= (TextView) view.findViewById(R.id.tv_updatetextinfowithpic);
            if (!TextUtils.isEmpty(path)) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path.indexOf("http")!=-1?path:"file://"+path))
                        .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(200), SizeUtils.dp2px(200))).build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request).setAutoPlayAnimations(true).build();
                draweeView.setController(draweeController);
                textview.setVisibility(View.GONE);
                iv_updatetextinfowithpic_delete.setVisibility(View.VISIBLE);
            }
            view.setOnClickListener(v -> {
                if (TextUtils.isEmpty(path)) {
                    choicePic();
                }
            });
            GridLayout.LayoutParams params=new GridLayout.LayoutParams();
            params.width=grid_updatetextinfowithpic.getMeasuredWidth()/3;
            params.height=grid_updatetextinfowithpic.getMeasuredWidth()/3;
            grid_updatetextinfowithpic.addView(view, params);
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_updatetextinfowithpic;
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
                if (TextUtils.isEmpty(et_updatetextinfowithpic.getText().toString())) {
                    Toast.makeText(this, "请填写详细描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_updatetextinfowithpic.getText().toString().length()>120) {
                    Toast.makeText(this, "详细描述不能超过120字", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent();
                intent.putExtra("picPath", picPath);
                intent.putExtra("ed", et_updatetextinfowithpic.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ib_nav_left:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_TAKEPHOTO && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            Utils.cropImage(path, UpdateTextInfoWithPicActivity.this, CommonParams.RESULT_CROP);
            return;
        }
        if (requestCode==CommonParams.RESULT_ALUMNI && resultCode==RESULT_OK) {
            ArrayList<String> filePaths=data.getExtras().getStringArrayList("choiceImages");
            if (filePaths==null) {
                return;
            }
            grid_updatetextinfowithpic.removeView(
                    grid_updatetextinfowithpic.getChildAt(grid_updatetextinfowithpic.getChildCount()-1));
            picPath.addAll(filePaths);
            for (int i=0;i<filePaths.size();i++) {
                addImage(filePaths.get(i));
            }
            if (picPath.size()<3) {
                addImage("");
            }
        }
        if (requestCode==CommonParams.RESULT_CROP && resultCode==RESULT_OK) {
            String path=data.getExtras().getString("path");
            grid_updatetextinfowithpic.removeView(
                    grid_updatetextinfowithpic.getChildAt(grid_updatetextinfowithpic.getChildCount()-1));
            picPath.add(path);
            addImage(path);
            if (picPath.size()<3) {
                addImage("");
            }
        }

    }

    private void choicePic() {
        View view_clearmessage= LayoutInflater.from(UpdateTextInfoWithPicActivity.this)
                .inflate(R.layout.view_actionsheet_button_3, null, false);
        ActionSheetFragment actionSheetFragment=ActionSheetFragment.build(getSupportFragmentManager())
                .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                .setTitle("设置图片")
                .setCustomerView(view_clearmessage)
                .show();
        TextView pop_three_choice1= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice1);
        pop_three_choice1.setText("拍照");
        pop_three_choice1.setOnClickListener(v -> {
            Utils.takePicture(UpdateTextInfoWithPicActivity.this, CommonParams.RESULT_TAKEPHOTO);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_choice2= (TextView) view_clearmessage.findViewById(R.id.pop_three_choice2);
        pop_three_choice2.setText("从相册获取");
        pop_three_choice2.setOnClickListener(v -> {
            Utils.choicePic(UpdateTextInfoWithPicActivity.this, 3-picPath.size(), CommonParams.RESULT_ALUMNI);
            actionSheetFragment.dismiss();
        });
        TextView pop_three_cancel= (TextView) view_clearmessage.findViewById(R.id.pop_three_cancel);
        pop_three_cancel.setText("取消");
        pop_three_cancel.setOnClickListener(v -> actionSheetFragment.dismiss());
    }
}
