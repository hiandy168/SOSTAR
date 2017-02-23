package com.renyu.sostar.activity.index;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.sign.SignInSignUpActivity;
import com.renyu.sostar.activity.user.UserInfoActivity;
import com.renyu.sostar.bean.MyCenterRequest;
import com.renyu.sostar.bean.MyCenterResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.service.LocationService;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/17.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.main_dl)
    DrawerLayout main_dl;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.main_menu_layout)
    LinearLayout main_menu_layout;
    @BindView(R.id.main_menu_grid)
    GridLayout main_menu_grid;
    @BindView(R.id.tv_main_menu_name)
    TextView tv_main_menu_name;
    @BindView(R.id.iv_main_menu_avatar)
    SimpleDraweeView iv_main_menu_avatar;
    @BindView(R.id.tv_main_menu_auth)
    TextView tv_main_menu_auth;
    @BindView(R.id.iv_main_menu_auth)
    ImageView iv_main_menu_auth;
    @BindView(R.id.iv_main_menu_evaluatelevel)
    TextView iv_main_menu_evaluatelevel;
    @BindView(R.id.iv_main_menu_finishorder)
    TextView iv_main_menu_finishorder;
    @BindView(R.id.iv_main_menu_closerate)
    TextView iv_main_menu_closerate;
    @BindView(R.id.tv_main_menu_auth2)
    TextView tv_main_menu_auth2;

    MyCenterResponse myCenterResponse;

    @Override
    public void initParams() {
        // 开启定位上报
        startService(new Intent(this, LocationService.class));

        nav_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ib_nav_left.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        ImageButton ib_menu_nav_left= (ImageButton) (main_menu_layout.findViewById(R.id.ib_nav_left));
        ib_menu_nav_left.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_arrow_write_left));
        ib_menu_nav_left.setOnClickListener(v -> main_dl.closeDrawer(main_menu_layout));
        TextView tv_menu_nav_title= (TextView) main_menu_layout.findViewById(R.id.tv_nav_title);
        tv_menu_nav_title.setText("我的");
        tv_menu_nav_title.setTextColor(Color.WHITE);

        main_dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        main_dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                main_dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                main_dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d("EmployerMainActivity", "newState:" + newState);
            }
        });
        BarUtils.adjustStatusBar(this, (ViewGroup) main_dl.getChildAt(0), ContextCompat.getColor(this, R.color.colorPrimary));
        BarUtils.adjustStatusBar(this, (ViewGroup) main_dl.getChildAt(1), ContextCompat.getColor(this, R.color.colorPrimary));

        main_menu_grid.post(() -> {
            int height=main_menu_grid.getMeasuredHeight();
            int width=main_menu_grid.getMeasuredWidth();
            for (int i=0;i<main_menu_grid.getChildCount();i++) {
                View view=main_menu_grid.getChildAt(i);
                GridLayout.LayoutParams params= (GridLayout.LayoutParams) view.getLayoutParams();
                params.width=width/3;
                params.height=height;
            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_main;
    }

    @Override
    public void loadData() {
        getMyCenter();
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @OnClick({R.id.ib_nav_left, R.id.layout_main_menu_mycenter_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                if (main_dl.isDrawerOpen(main_menu_layout)) {
                    main_dl.closeDrawer(main_menu_layout);
                }
                else {
                    main_dl.openDrawer(main_menu_layout);
                }
                break;
            case R.id.layout_main_menu_mycenter_info:
                Intent intent_info=new Intent(MainActivity.this, UserInfoActivity.class);
                intent_info.putExtra("response", myCenterResponse);
                startActivityForResult(intent_info, CommonParams.RESULT_UPDATEUSERINFO);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, SignInSignUpActivity.class);
        intent.putExtra(CommonParams.FROM, CommonParams.FINISH);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CommonParams.RESULT_UPDATEUSERINFO && resultCode==RESULT_OK) {
            myCenterResponse= (MyCenterResponse) data.getSerializableExtra("value");
            updateMyCenter(myCenterResponse);
        }
    }

    private void getMyCenter() {
        if (TextUtils.isEmpty(ACache.get(this).getAsString(CommonParams.USER_ID))) {
            return;
        }
        MyCenterRequest request=new MyCenterRequest();
        MyCenterRequest.ParamBean paramBean=new MyCenterRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getMyCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyCenterResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyCenterResponse value) {
                myCenterResponse=value;
                updateMyCenter(myCenterResponse);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void updateMyCenter(MyCenterResponse value) {
        if (!TextUtils.isEmpty(value.getNickName())) {
            tv_main_menu_name.setText(value.getNickName());
        }
        else {
            tv_main_menu_name.setText("暂无");
        }
        if (!TextUtils.isEmpty(value.getPicPath())) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(value.getPicPath())).setAutoPlayAnimations(true).build();
            iv_main_menu_avatar.setController(draweeController);
        }
        if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
            tv_main_menu_auth.setText("未认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_mynoauth);
            tv_main_menu_auth2.setText("未认证");
        }
        else if (value.getAuthentication().equals("1")) {
            tv_main_menu_auth.setText("已认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_myauth);
            tv_main_menu_auth2.setText("已认证");
        }
        else if (value.getAuthentication().equals("2")) {
            tv_main_menu_auth.setText("认证中");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_myauth);
            tv_main_menu_auth2.setText("认证中");
        }
        iv_main_menu_evaluatelevel.setText(TextUtils.isEmpty(value.getEvaluateLevel())?"0":value.getEvaluateLevel());
        iv_main_menu_finishorder.setText(""+value.getFinishedOrders());
        iv_main_menu_closerate.setText(TextUtils.isEmpty(value.getCloseRate())?"0":value.getCloseRate());
    }
}
