package com.renyu.sostar.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.OrderListAdapter;
import com.renyu.sostar.bean.MyOrderListRequest;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/8.
 */

public class OrderListFragment extends BaseFragment {

    @BindView(R.id.swipy_orderlist)
    SwipyRefreshLayout swipy_orderlist;
    @BindView(R.id.rv_orderlist)
    RecyclerView rv_orderlist;
    OrderListAdapter adapter;

    ArrayList<MyOrderListResponse.DataBean> beans;

    int page=1;

    public static OrderListFragment newInstance(int type) {
        OrderListFragment fragment=new OrderListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        rv_orderlist.setHasFixedSize(true);
        rv_orderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new OrderListAdapter(getActivity(), beans);
        rv_orderlist.setAdapter(adapter);
        swipy_orderlist.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipy_orderlist.setOnRefreshListener(direction -> {
            if (direction==SwipyRefreshLayoutDirection.TOP) {
                page=1;
            }
            getMyOrderList();
        });
    }

    @Override
    public int initViews() {
        return R.layout.fragment_orderlist;
    }

    @Override
    public void loadData() {
        swipy_orderlist.post(() -> {
            swipy_orderlist.setRefreshing(true);
            getMyOrderList();
        });
    }

    private void getMyOrderList() {
        MyOrderListRequest request=new MyOrderListRequest();
        MyOrderListRequest.ParamBean paramBean=new MyOrderListRequest.ParamBean();
        paramBean.setUserId(ACache.get(getActivity()).getAsString(CommonParams.USER_ID));
        MyOrderListRequest.ParamBean.PaginationBean paginationBean=new MyOrderListRequest.ParamBean.PaginationBean();
        paginationBean.setPageSize(20);
        paginationBean.setStartPos(page);
        paramBean.setType(""+getArguments().getInt("type"));
        paramBean.setPagination(paginationBean);
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .myOrderList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MyOrderListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MyOrderListResponse value) {
                if (page==1) {
                    beans.clear();
                }
                beans.addAll(value.getData());
                adapter.notifyDataSetChanged();
                page++;
                swipy_orderlist.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipy_orderlist.setRefreshing(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
