package com.renyu.sostar.activity.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.FavListAdapter;
import com.renyu.sostar.bean.FavDelRequest;
import com.renyu.sostar.bean.FavListResponse;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/29.
 */

public class FavListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.rv_favlist)
    RecyclerView rv_favlist;
    FavListAdapter adapter;

    ArrayList<FavListResponse> beans;

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("收藏");
        tv_nav_title.setTextColor(Color.parseColor("#333333"));

        rv_favlist.setHasFixedSize(true);
        rv_favlist.setLayoutManager(new LinearLayoutManager(this));
        rv_favlist.setItemAnimator(new DefaultItemAnimator());
        adapter=new FavListAdapter(this, beans);
        rv_favlist.setAdapter(adapter);
    }

    @Override
    public int initViews() {
        return R.layout.activity_favlist;
    }

    @Override
    public void loadData() {
        getFavList();
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

    private void getFavList() {
        OrderRequest request=new OrderRequest();
        OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .searchFav(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.backgroundList()).subscribe(new Observer<List<FavListResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<FavListResponse> value) {
                beans.clear();
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

    public void deleteFav(String staffs) {
        FavDelRequest request=new FavDelRequest();
        FavDelRequest.ParamBean paramBean=new FavDelRequest.ParamBean();
        paramBean.setEmployer(ACache.get(this).getAsString(CommonParams.USER_ID));
        ArrayList<String> strings=new ArrayList<>();
        strings.add(staffs);
        paramBean.setStaffs(strings);
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .outFav(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(FavListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                for (FavListResponse bean : beans) {
                    if (bean.getUserId().equals(staffs)) {
                        beans.remove(bean);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FavListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
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
