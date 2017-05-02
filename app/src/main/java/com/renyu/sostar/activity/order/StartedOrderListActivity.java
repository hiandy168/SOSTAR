package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.sostar.R;
import com.renyu.sostar.fragment.OrderListFragment;
import com.renyu.sostar.params.CommonParams;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/28.
 */

public class StartedOrderListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("进行中的订单");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        Fragment fragment=null;
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            fragment= OrderListFragment.newInstance(3, OrderListFragment.OrderListType.myEmployerList);
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            fragment= OrderListFragment.newInstance(2, OrderListFragment.OrderListType.myEmployeeList);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_startedorderlist, fragment).commitAllowingStateLoss();
    }

    @Override
    public int initViews() {
        return R.layout.activity_startedorderlist;
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

    @OnClick({R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
        }
    }
}
