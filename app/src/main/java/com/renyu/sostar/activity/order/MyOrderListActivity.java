package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.fragment.OrderListFragment;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 2017/3/13.
 */

public class MyOrderListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tab_orderlist)
    TabLayout tab_orderlist;
    @BindView(R.id.vp_orderlist)
    ViewPager vp_orderlist;

    ArrayList<Fragment> fragments;
    ArrayList<String> titles;

    @Override
    public void initParams() {
        fragments=new ArrayList<>();
        titles=new ArrayList<>();
        if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("1")) {
            titles.add("已发单");
            titles.add("已开工");
            titles.add("已完成");
            titles.add("已取消");
            titles.add("草稿");
            fragments.add(OrderListFragment.newInstance(1, OrderListFragment.OrderListType.myEmployerList));
            fragments.add(OrderListFragment.newInstance(3, OrderListFragment.OrderListType.myEmployerList));
            fragments.add(OrderListFragment.newInstance(5, OrderListFragment.OrderListType.myEmployerList));
            fragments.add(OrderListFragment.newInstance(9, OrderListFragment.OrderListType.myEmployerList));
            fragments.add(OrderListFragment.newInstance(0, OrderListFragment.OrderListType.myEmployerList));
        }
        else if (ACache.get(this).getAsString(CommonParams.USER_TYPE).equals("0")) {
            titles.add("已接单");
            titles.add("已成单");
            titles.add("已完成");
            titles.add("已取消");
            titles.add("已拒绝");
            fragments.add(OrderListFragment.newInstance(1, OrderListFragment.OrderListType.myEmployeeList));
            fragments.add(OrderListFragment.newInstance(2, OrderListFragment.OrderListType.myEmployeeList));
            fragments.add(OrderListFragment.newInstance(3, OrderListFragment.OrderListType.myEmployeeList));
            fragments.add(OrderListFragment.newInstance(4, OrderListFragment.OrderListType.myEmployeeList));
            fragments.add(OrderListFragment.newInstance(4, OrderListFragment.OrderListType.myEmployeeList));
        }

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("订单");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        tab_orderlist.setTabGravity(TabLayout.GRAVITY_FILL);
        if (titles.size()>3) {
            tab_orderlist.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        else {
            tab_orderlist.setTabMode(TabLayout.MODE_FIXED);
            Utils.setIndicator(this, tab_orderlist, 30, 30);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) tab_orderlist.getLayoutParams();
            params.setMargins(SizeUtils.dp2px(40), 0, SizeUtils.dp2px(40), 0);
            tab_orderlist.setLayoutParams(params);
        }

        vp_orderlist.setAdapter(new OrderAdapter(getSupportFragmentManager()));
        tab_orderlist.setupWithViewPager(vp_orderlist);
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderlist;
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

    private class OrderAdapter extends FragmentPagerAdapter {
        public OrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
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
