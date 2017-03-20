package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.EmployeeListAdapter;
import com.renyu.sostar.bean.ComfirmEmployeeRequest;
import com.renyu.sostar.bean.EmployerStaffListResponse;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/15.
 */

public class EmployeeListActivity extends BaseActivity {

    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_left)
    ImageButton ib_nav_left;
    @BindView(R.id.rv_employee)
    RecyclerView rv_employee;
    EmployeeListAdapter adapter;

    ArrayList<EmployerStaffListResponse> beans;

    @Override
    public void initParams() {
        tv_nav_title.setTextColor(Color.WHITE);
        tv_nav_title.setText("雇员列表");

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
        getEmployerStaffList();
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

    private void getEmployerStaffList() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setOrderId(getIntent().getStringExtra("orderId"));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getEmployerStaffList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.backgroundList()).subscribe(new Observer<List<EmployerStaffListResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<EmployerStaffListResponse> value) {
                beans.addAll(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void confirmStaff(String userId, int status) {
        ComfirmEmployeeRequest request=new ComfirmEmployeeRequest();
        ComfirmEmployeeRequest.ParamBean paramBean=new ComfirmEmployeeRequest.ParamBean();
        paramBean.setOrderId(Integer.parseInt(getIntent().getStringExtra("orderId")));
        paramBean.setUserId(userId);
        paramBean.setStatus(status);
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .confirmStaff(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                EmployerStaffListResponse beanTemp=null;
                for (EmployerStaffListResponse bean : beans) {
                    if (bean.getUserId().equals(userId)) {
                        beanTemp=bean;
                        beans.remove(bean);
                    }
                }
                if (beanTemp!=null) {
                    // 确认
                    if (status==1) {
                        beanTemp.setStaffStatus("1");
                        beans.add(0, beanTemp);
                    }
                    // 拒绝
                    else if (status==2) {
                        beanTemp.setStaffStatus("0");
                        beans.add(0, beanTemp);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
