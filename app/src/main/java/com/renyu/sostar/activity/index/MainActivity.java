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
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetUtils;
import com.renyu.sostar.BuildConfig;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.message.MessageListActivity;
import com.renyu.sostar.activity.order.MyOrderListActivity;
import com.renyu.sostar.activity.settings.SettingsActivity;
import com.renyu.sostar.activity.sign.SignInSignUpActivity;
import com.renyu.sostar.activity.user.EmployeeAuthActivity;
import com.renyu.sostar.activity.user.EmployeeInfoActivity;
import com.renyu.sostar.activity.user.EmployerAuthActivity;
import com.renyu.sostar.activity.user.EmployerInfoActivity;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.MyCenterRequest;
import com.renyu.sostar.fragment.MainFragment;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.service.LocationService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
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
    @BindView(R.id.iv_nav_title)
    ImageView iv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.ib_nav_right)
    ImageButton ib_nav_right;
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
    @BindView(R.id.tv_main_menu_evaluatelevel)
    TextView tv_main_menu_evaluatelevel;
    @BindView(R.id.tv_main_menu_order)
    TextView tv_main_menu_order;
    @BindView(R.id.tv_main_menu_order_desp)
    TextView tv_main_menu_order_desp;
    @BindView(R.id.tv_main_menu_closerate)
    TextView tv_main_menu_closerate;
    @BindView(R.id.tv_main_menu_auth2)
    TextView tv_main_menu_auth2;
    @BindView(R.id.tv_main_menu_mycenter_info)
    TextView tv_main_menu_mycenter_info;
    @BindView(R.id.tv_main_menu_mycenter_area_desp)
    TextView tv_main_menu_mycenter_area_desp;
    @BindView(R.id.tv_main_menu_mycenter_area)
    TextView tv_main_menu_mycenter_area;
    @BindView(R.id.tv_main_menu_message)
    TextView tv_main_menu_message;
    @BindView(R.id.layout_main_menu_mycenter_fav_line)
    View layout_main_menu_mycenter_fav_line;
    @BindView(R.id.layout_main_menu_mycenter_fav)
    LinearLayout layout_main_menu_mycenter_fav;

    MyCenterEmployeeResponse myCenterEmployeeResponse;
    MyCenterEmployerResponse myCenterEmployerResponse;

    Disposable disposable;

    @Override
    public void initParams() {
        // 设置当前用户已经上线
        ACache.get(this).put(CommonParams.USER_SIGNIN, "1");

        // 开启定位上报
        startService(new Intent(this, LocationService.class));

        // 注册极光alias
        JPushInterface.setAliasAndTags(getApplicationContext(), ACache.get(this).getAsString(CommonParams.USER_ID), null, (i, s, set) -> {
            if (i==0) {
                Log.d("MainActivity", "注册极光alias成功");
            }
            else {
                Log.d("MainActivity", "注册极光alias失败");
            }
        });

        nav_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        iv_nav_title.setImageResource(R.mipmap.ic_main_logo);
        ib_nav_left.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_main_menu));
        ib_nav_right.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_main_started_order));
        ImageButton ib_menu_nav_left= (ImageButton) (main_menu_layout.findViewById(R.id.ib_nav_left));
        ib_menu_nav_left.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_arrow_write_left));
        ib_menu_nav_left.setOnClickListener(v -> main_dl.closeDrawer(main_menu_layout));
        main_menu_layout.findViewById(R.id.nav_layout).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView tv_menu_nav_title= (TextView) main_menu_layout.findViewById(R.id.tv_nav_title);
        tv_menu_nav_title.setText("我的");
        tv_menu_nav_title.setTextColor(Color.WHITE);
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            tv_main_menu_mycenter_info.setText("企业资料");
            tv_main_menu_mycenter_area_desp.setText("发单范围");
            tv_main_menu_order_desp.setText("已发单");
        }
        else {
            tv_main_menu_mycenter_info.setText("个人资料");
            tv_main_menu_mycenter_area_desp.setText("接单范围");
            tv_main_menu_order_desp.setText("已接单");
        }

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
                view.requestLayout();
            }
        });

        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            layout_main_menu_mycenter_fav.setVisibility(View.GONE);
            layout_main_menu_mycenter_fav_line.setVisibility(View.GONE);
        }

        // 主页添加
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content, new MainFragment(), "mainFragment").commitAllowingStateLoss();
    }

    @Override
    public int initViews() {
        return R.layout.activity_main;
    }

    @Override
    public void loadData() {
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            getMyEmployerCenter();
        }
        else {
            getMyEmployeeCenter();
        }
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, LocationService.class));
    }

    @OnClick({R.id.ib_nav_left, R.id.ib_nav_right, R.id.layout_main_menu_mycenter_info,
            R.id.layout_main_menu_mycenter_auth, R.id.layout_main_menu_mycenter_area,
            R.id.layout_main_menu_mycenter_settings, R.id.layout_main_menu_message,
            R.id.layout_main_menu_myorder})
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
            case R.id.ib_nav_right:
                break;
            case R.id.layout_main_menu_mycenter_info:
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    if (myCenterEmployerResponse==null) {
                        return;
                    }
                    Intent intent_info=new Intent(MainActivity.this, EmployerInfoActivity.class);
                    intent_info.putExtra("response", myCenterEmployerResponse);
                    startActivityForResult(intent_info, CommonParams.RESULT_UPDATEUSERINFO);
                }
                else {
                    if (myCenterEmployeeResponse==null) {
                        return;
                    }
                    Intent intent_info=new Intent(MainActivity.this, EmployeeInfoActivity.class);
                    intent_info.putExtra("response", myCenterEmployeeResponse);
                    startActivityForResult(intent_info, CommonParams.RESULT_UPDATEUSERINFO);
                }
                break;
            case R.id.layout_main_menu_mycenter_auth:
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    if (myCenterEmployerResponse==null) {
                        return;
                    }
                    Intent intent_auth=new Intent(MainActivity.this, EmployerAuthActivity.class);
                    intent_auth.putExtra("response", myCenterEmployerResponse);
                    startActivityForResult(intent_auth, CommonParams.RESULT_UPDATEUSERINFO);
                }
                else {
                    if (myCenterEmployeeResponse==null) {
                        return;
                    }
                    Intent intent_auth=new Intent(MainActivity.this, EmployeeAuthActivity.class);
                    intent_auth.putExtra("response", myCenterEmployeeResponse);
                    startActivityForResult(intent_auth, CommonParams.RESULT_UPDATEUSERINFO);
                }
                break;
            case R.id.layout_main_menu_mycenter_area:
                int[] realArea=new int[] {3, 5, 10, 10000};
                String[] despArea=new String[] {"3公里", "5公里", "10公里", "不限范围"};
                if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    ActionSheetUtils.showList(getSupportFragmentManager(), "发单范围",
                            new String[]{"3公里", "5公里", "10公里", "不限范围"}, position -> {
                                tv_main_menu_mycenter_area.setText(despArea[position]);
                                myCenterEmployerResponse.setRangeArea(""+realArea[position]);
                                updateTextInfo("rangeArea", ""+realArea[position]);
                            }, () -> {

                            });
                }
                else {
                    ActionSheetUtils.showList(getSupportFragmentManager(), "接单范围",
                            new String[]{"3公里", "5公里", "10公里", "不限范围"}, position -> {
                                tv_main_menu_mycenter_area.setText(despArea[position]);
                                myCenterEmployeeResponse.setRangeArea(realArea[position]);
                                updateTextInfo("rangeArea", ""+realArea[position]);
                            }, () -> {

                            });
                }
                break;
            case R.id.layout_main_menu_mycenter_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.layout_main_menu_message:
                if (myCenterEmployeeResponse==null && myCenterEmployerResponse==null) {
                    return;
                }
                startActivity(new Intent(MainActivity.this, MessageListActivity.class));
                break;
            case R.id.layout_main_menu_myorder:
                if (myCenterEmployeeResponse==null && myCenterEmployerResponse==null) {
                    return;
                }
                startActivity(new Intent(MainActivity.this, MyOrderListActivity.class));
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
            if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
                myCenterEmployerResponse= (MyCenterEmployerResponse) data.getSerializableExtra("value");
                updateMyEmployerCenter(myCenterEmployerResponse);
            }
            else {
                myCenterEmployeeResponse= (MyCenterEmployeeResponse) data.getSerializableExtra("value");
                updateMyEmployeeCenter(myCenterEmployeeResponse);

                EventBus.getDefault().post(myCenterEmployeeResponse);
            }
        }
    }

    private void getMyEmployeeCenter() {
        if (TextUtils.isEmpty(ACache.get(this).getAsString(CommonParams.USER_ID))) {
            return;
        }
        MyCenterRequest request=new MyCenterRequest();
        MyCenterRequest.ParamBean paramBean=new MyCenterRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getMyEmployeeCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyCenterEmployeeResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyCenterEmployeeResponse value) {
                myCenterEmployeeResponse=value;
                updateMyEmployeeCenter(myCenterEmployeeResponse);

                EventBus.getDefault().post(value);
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

    private void getMyEmployerCenter() {
        if (TextUtils.isEmpty(ACache.get(this).getAsString(CommonParams.USER_ID))) {
            return;
        }
        MyCenterRequest request=new MyCenterRequest();
        MyCenterRequest.ParamBean paramBean=new MyCenterRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getMyEmployerCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyCenterEmployerResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyCenterEmployerResponse value) {
                myCenterEmployerResponse=value;
                updateMyEmployerCenter(value);

                EventBus.getDefault().post(value);
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

    private void updateMyEmployeeCenter(MyCenterEmployeeResponse value) {
        if (!TextUtils.isEmpty(value.getNickName())) {
            tv_main_menu_name.setText(value.getNickName());
        }
        if (!TextUtils.isEmpty(value.getPicPath())) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(value.getPicPath())).setAutoPlayAnimations(true).build();
            iv_main_menu_avatar.setController(draweeController);
        }
        if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
            tv_main_menu_auth.setText("未认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_userinfonoauth);
            tv_main_menu_auth2.setText("未认证");
        }
        else if (value.getAuthentication().equals("1")) {
            tv_main_menu_auth.setText("已认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_myauth);
            tv_main_menu_auth2.setText("已认证");
        }
        else if (value.getAuthentication().equals("2")) {
            tv_main_menu_auth.setText("认证中");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_userinfoauthing);
            tv_main_menu_auth2.setText("认证中");
        }
        tv_main_menu_evaluatelevel.setText(TextUtils.isEmpty(value.getEvaluateLevel())?"0":value.getEvaluateLevel());
        tv_main_menu_order.setText(""+value.getFinishedOrders());
        tv_main_menu_closerate.setText(TextUtils.isEmpty(value.getCloseRate())?"0":value.getCloseRate());
        tv_main_menu_mycenter_area.setText(value.getRangeArea()+"公里");
        if (value.getRangeArea()==10000) {
            tv_main_menu_mycenter_area.setText("不限范围");
        }
        else {
            tv_main_menu_mycenter_area.setText(value.getRangeArea()+"公里");
        }
    }

    private void updateMyEmployerCenter(MyCenterEmployerResponse value) {
        if (!TextUtils.isEmpty(value.getCompanyName())) {
            tv_main_menu_name.setText(value.getCompanyName());
        }
        if (!TextUtils.isEmpty(value.getLogoPath())) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(value.getLogoPath())).setAutoPlayAnimations(true).build();
            iv_main_menu_avatar.setController(draweeController);
        }
        if (TextUtils.isEmpty(value.getAuthentication()) || value.getAuthentication().equals("0")) {
            tv_main_menu_auth.setText("未认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_userinfonoauth);
            tv_main_menu_auth2.setText("未认证");
        }
        else if (value.getAuthentication().equals("1")) {
            tv_main_menu_auth.setText("已认证");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_myauth);
            tv_main_menu_auth2.setText("已认证");
        }
        else if (value.getAuthentication().equals("2")) {
            tv_main_menu_auth.setText("认证中");
            iv_main_menu_auth.setImageResource(R.mipmap.ic_userinfoauthing);
            tv_main_menu_auth2.setText("认证中");
        }
        tv_main_menu_evaluatelevel.setText(TextUtils.isEmpty(value.getStar())?"0":value.getStar());
        tv_main_menu_order.setText(TextUtils.isEmpty(value.getOngoingOrder())?"0":value.getOngoingOrder());
        tv_main_menu_closerate.setText(TextUtils.isEmpty(value.getCloseRate())?"0":value.getCloseRate());
        if (TextUtils.isEmpty(value.getRangeArea())) {
            tv_main_menu_mycenter_area.setText("0公里");
        }
        else if (value.getRangeArea().equals("10000")) {
            tv_main_menu_mycenter_area.setText("不限范围");
        }
        else {
            tv_main_menu_mycenter_area.setText(value.getRangeArea()+"公里");
        }
    }

    private void updateTextInfo(String param, String value) {
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceId", Utils.getUniquePsuedoID());
            jsonObject.put("ver", BuildConfig.VERSION_NAME);
            JSONObject childJsonObject=new JSONObject();
            childJsonObject.put(param, value);
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
                        Toast.makeText(MainActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
