package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.EmployeeListAdapter;
import com.renyu.sostar.bean.EmployeeListResponse;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by renyu on 2017/3/15.
 */

public class EmployeeListActivity extends BaseActivity {

    @BindView(R.id.rv_employee)
    RecyclerView rv_employee;
    EmployeeListAdapter adapter;

    ArrayList<EmployeeListResponse> beans;

    @Override
    public void initParams() {
        beans=new ArrayList<>();
        rv_employee.setHasFixedSize(true);
        rv_employee.setLayoutManager(new LinearLayoutManager(this));
        adapter=new EmployeeListAdapter(this, beans);
        rv_employee.setAdapter(adapter);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rv_employee.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_employeelist;
    }

    @Override
    public void loadData() {
        for (int i=0;i<20;i++) {
            EmployeeListResponse response=new EmployeeListResponse();
            response.setGroupName("k"+i);
            response.setId(i/5);
            response.setName("a"+i);
            beans.add(response);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public int setStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
