package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.fragment.NotStartedOrderListFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by renyu on 2017/3/8.
 */

public class NotStartedOrderListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tab_notstartedorderlist)
    TabLayout tab_notstartedorderlist;
    @BindView(R.id.vp_notstartedorderlist)
    ViewPager vp_notstartedorderlist;

    ArrayList<Fragment> fragments;
    ArrayList<String> titles;

    @Override
    public void initParams() {
        fragments=new ArrayList<>();
        fragments.add(NotStartedOrderListFragment.newInstance());
        fragments.add(NotStartedOrderListFragment.newInstance());
        titles=new ArrayList<>();
        titles.add("推荐");
        titles.add("最新");

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("订单");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        tab_notstartedorderlist.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_notstartedorderlist.setTabMode(TabLayout.MODE_FIXED);
        vp_notstartedorderlist.setAdapter(new OrderAdapter(getSupportFragmentManager()));
        tab_notstartedorderlist.setupWithViewPager(vp_notstartedorderlist);
        Utils.setIndicator(this, tab_notstartedorderlist, 30, 30);
    }

    @Override
    public int initViews() {
        return R.layout.activity_notstartedorderlist;
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
}
