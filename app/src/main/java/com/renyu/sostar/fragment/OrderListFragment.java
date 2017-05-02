package com.renyu.sostar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.Response;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.order.OrderDetailActivity;
import com.renyu.sostar.adapter.OrderListAdapter;
import com.renyu.sostar.bean.MyOrderListRequest;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;
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
    // 订单列表类型
    public enum OrderListType {
        myOrderList("雇员待接单", 1),
        myEmployeeList("雇员我的订单列表", 2),
        myEmployerList("雇主订单列表", 3);

        OrderListType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        private String name ;
        private int index;

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }
    }

    ArrayList<MyOrderListResponse.DataBean> beans;

    int page=1;

    public static OrderListFragment newInstance(int type, OrderListType orderListType) {
        OrderListFragment fragment=new OrderListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("orderListType", orderListType.getIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        rv_orderlist.setHasFixedSize(true);
        rv_orderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        boolean isShowDistance=false;
        if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
            // 雇员待接单
            if (getArguments().getInt("orderListType")==1) {
                isShowDistance=true;
            }
        }
        adapter=new OrderListAdapter(getActivity(), beans, isShowDistance);
        adapter.setOnClickListener(position -> {
            Intent intent=new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("orderId", beans.get(position).getOrderId());
            if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
                // 雇员待接单采用雇主订单详情
                if (getArguments().getInt("orderListType")==1) {
                    intent.putExtra("typeIsCommit", true);
                }
            }
            startActivity(intent);
        });
        rv_orderlist.setAdapter(adapter);
        swipy_orderlist.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipy_orderlist.setOnRefreshListener(direction -> {
            if (direction==SwipyRefreshLayoutDirection.TOP) {
                page=1;
            }
            getMyOrderList();
        });

        EventBus.getDefault().register(this);
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
        Observable<Response<MyOrderListResponse>> observable=null;
        if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
            // 雇员待接单
            if (getArguments().getInt("orderListType")==1) {
                observable= retrofit.create(RetrofitImpl.class)
                        .myStaffOrderList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)));
            }
            // 雇员我的订单列表
            else if (getArguments().getInt("orderListType")==2) {
                observable= retrofit.create(RetrofitImpl.class)
                        .employeeOrderCenter(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)));
            }
        }
        else if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("1")) {
            // 雇主我的订单列表
            observable= retrofit.create(RetrofitImpl.class)
                    .myEmployerOrderList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)));
        }
        observable.compose(Retrofit2Utils.background()).subscribe(new Observer<MyOrderListResponse>() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    // 支付状态发生变化以刷新
    // 员工状态变化以刷新
    // 雇员接单状态变化以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderResponse response) {
        page=1;
        getMyOrderList();
    }
}
