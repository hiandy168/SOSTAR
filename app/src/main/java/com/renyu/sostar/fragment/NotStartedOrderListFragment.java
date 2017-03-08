package com.renyu.sostar.fragment;

import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.sostar.R;

/**
 * Created by renyu on 2017/3/8.
 */

public class NotStartedOrderListFragment extends BaseFragment {

    public static NotStartedOrderListFragment newInstance() {
        NotStartedOrderListFragment fragment=new NotStartedOrderListFragment();
        return fragment;
    }

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.fragment_notstartedorderlist;
    }

    @Override
    public void loadData() {

    }
}
