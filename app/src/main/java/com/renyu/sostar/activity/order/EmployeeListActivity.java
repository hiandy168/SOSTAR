package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.EmployeeListAdapter;
import com.renyu.sostar.bean.ConfirmStaffRequest;
import com.renyu.sostar.bean.EmployeeListResponse;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    private void confirmStaff(int status, String userId) {
        ConfirmStaffRequest request=new ConfirmStaffRequest();
        ConfirmStaffRequest.ParamBean paramBean=new ConfirmStaffRequest.ParamBean();
        paramBean.setOrderId(Integer.parseInt(getIntent().getStringExtra("orderId")));
        paramBean.setStatus(status);
        paramBean.setUserId(userId);
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .confirmStaff(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
