package com.renyu.sostar.activity.order;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.jpushlibrary.bean.NotificationBean;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.EmployeeListAdapter;
import com.renyu.sostar.bean.ComfirmEmployeeRequest;
import com.renyu.sostar.bean.EmployerStaffListResponse;
import com.renyu.sostar.bean.FavRequest;
import com.renyu.sostar.bean.OrderRequest;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.ReleaseOrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/3/15.
 */

public class EmployeeListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.ib_nav_right)
    ImageButton ib_nav_right;
    @BindView(R.id.rv_employee)
    RecyclerView rv_employee;
    EmployeeListAdapter adapter;

    ArrayList<EmployerStaffListResponse> beans;

    @Override
    public void initParams() {
        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setTextColor(Color.parseColor("#333333"));
        tv_nav_title.setText("雇员列表");
        ib_nav_right.setImageResource(R.mipmap.ic_order_notification);

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
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ib_nav_left, R.id.ib_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.ib_nav_right:
                Intent intent=new Intent(EmployeeListActivity.this, OrderBroadcastActivity.class);
                intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                startActivity(intent);
                break;
        }
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

    // 确认雇员
    public void confirmStaff(String userId, int status) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage(status==1?"确认雇佣该员工？":"拒绝雇佣该员工？")
                .setPositiveButton("确定", (dialog, which) -> {
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
                            showNetworkDialog("正在操作，请稍后");
                        }

                        @Override
                        public void onNext(EmptyResponse value) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                            EmployerStaffListResponse beanTemp=null;
                            for (EmployerStaffListResponse bean : beans) {
                                if (bean.getUserId().equals(userId)) {
                                    beanTemp=bean;
                                    beans.remove(bean);
                                    break;
                                }
                            }
                            if (beanTemp!=null) {
                                // 确认
                                if (status==1) {
                                    beanTemp.setStaffStatus("1");
                                    beans.add(0, beanTemp);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            // 员工身份变化需要刷新
                            EventBus.getDefault().post(new OrderResponse());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
    }

    // 解雇
    public void fireStaff(String userId) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确认解雇该员工？")
                .setPositiveButton("确定", (dialog, which) -> {
                    OrderRequest request=new OrderRequest();
                    OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
                    paramBean.setOrderId(getIntent().getStringExtra("orderId"));
                    paramBean.setUserId(userId);
                    request.setParam(paramBean);
                    retrofit.create(RetrofitImpl.class)
                            .fireStaff(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                            .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            showNetworkDialog("正在操作，请稍后");
                        }

                        @Override
                        public void onNext(EmptyResponse value) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                            EmployerStaffListResponse beanTemp=null;
                            for (EmployerStaffListResponse bean : beans) {
                                if (bean.getUserId().equals(userId)) {
                                    beanTemp=bean;
                                    beans.remove(bean);
                                    break;
                                }
                            }
                            if (beanTemp!=null) {
                                adapter.notifyDataSetChanged();
                            }

                            // 员工身份变化需要刷新
                            EventBus.getDefault().post(new OrderResponse());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
    }

    // 确认离职
    public void comfirmResignation(String userId) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确认该员工离职？")
                .setPositiveButton("确定", (dialog, which) -> {
                    OrderRequest request=new OrderRequest();
                    OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
                    paramBean.setOrderId(getIntent().getStringExtra("orderId"));
                    paramBean.setUserId(userId);
                    request.setParam(paramBean);
                    retrofit.create(RetrofitImpl.class)
                            .comfirmResignation(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                            .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            showNetworkDialog("正在操作，请稍后");
                        }

                        @Override
                        public void onNext(EmptyResponse value) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                            EmployerStaffListResponse beanTemp=null;
                            for (EmployerStaffListResponse bean : beans) {
                                if (bean.getUserId().equals(userId)) {
                                    beanTemp=bean;
                                    beans.remove(bean);
                                    break;
                                }
                            }
                            if (beanTemp!=null) {
                                adapter.notifyDataSetChanged();
                            }

                            // 员工身份变化需要刷新
                            EventBus.getDefault().post(new OrderResponse());
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
    }

    // 拒绝同意离职
    public void refuseResignation(String userId) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("拒绝该员工离职？")
                .setPositiveButton("确定", (dialog, which) -> {
                    OrderRequest request=new OrderRequest();
                    OrderRequest.ParamBean paramBean=new OrderRequest.ParamBean();
                    paramBean.setOrderId(getIntent().getStringExtra("orderId"));
                    paramBean.setUserId(userId);
                    request.setParam(paramBean);
                    retrofit.create(RetrofitImpl.class)
                            .refuseResignation(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                            .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            showNetworkDialog("正在操作，请稍后");
                        }

                        @Override
                        public void onNext(EmptyResponse value) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                            EmployerStaffListResponse beanTemp=null;
                            for (EmployerStaffListResponse bean : beans) {
                                if (bean.getUserId().equals(userId)) {
                                    beanTemp=bean;
                                    beans.remove(bean);
                                    break;
                                }
                            }
                            if (beanTemp!=null) {
                                beanTemp.setStaffStatus("8");
                                beans.add(0, beanTemp);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissNetworkDialog();

                            Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
    }

    // 评价雇员
    public void evaluate(String userId, String userName) {
        Intent intent=new Intent(EmployeeListActivity.this, EvaluateActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
        intent.putExtra("userId", userId);
        startActivityForResult(intent, CommonParams.RESULT_EVALUATE);
    }

    // 收藏
    public void collection(String userId) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确认收藏？")
                .setPositiveButton("确定", (dialog, which) -> {
            FavRequest request=new FavRequest();
            FavRequest.ParamBean paramBean=new FavRequest.ParamBean();
            paramBean.setEmployer(ACache.get(this).getAsString(CommonParams.USER_ID));
            paramBean.setStaff(userId);
            request.setParam(paramBean);
            retrofit.create(RetrofitImpl.class)
                    .doFav(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                    .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(EmptyResponse value) {
                    Toast.makeText(EmployeeListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();
                    for (EmployerStaffListResponse bean : beans) {
                        if (bean.getUserId().equals(userId)) {
                            bean.setFavFlg("1");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(EmployeeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
        }).setNegativeButton("取消", (dialog, which) -> {

        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==CommonParams.RESULT_EVALUATE) {
                EmployerStaffListResponse tempBean=null;
                for (EmployerStaffListResponse bean : beans) {
                    if (bean.getUserId().equals(data.getStringExtra("userId"))) {
                        tempBean=bean;
                        break;
                    }
                }
                if (tempBean!=null) {
                    beans.remove(tempBean);
                    tempBean.setEvaluateFlg("1");
                    beans.add(tempBean);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    // 收到推送以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NotificationBean response) {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(response.getExtra());
            // 相同订单进行刷新
            if (jsonObject.getString("orderId").equals(getIntent().getStringExtra("orderId"))) {
                getEmployerStaffList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
