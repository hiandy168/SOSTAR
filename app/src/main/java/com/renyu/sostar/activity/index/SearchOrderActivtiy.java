package com.renyu.sostar.activity.index;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.views.FlowLayout;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.order.OrderDetailActivity;
import com.renyu.sostar.adapter.OrderListAdapter;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.bean.OrderResponse;
import com.renyu.sostar.bean.SearchOrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by renyu on 2017/3/14.
 */

public class SearchOrderActivtiy extends BaseActivity {

    @BindView(R.id.ed_searchorder_word)
    EditText ed_searchorder_word;
    @BindView(R.id.flow_searchorder_hot)
    FlowLayout flow_searchorder_hot;
    @BindView(R.id.rv_searchorder)
    RecyclerView rv_searchorder;
    OrderListAdapter adapter;

    ArrayList<MyOrderListResponse.DataBean> beans;

    String[] choiceArray={"品质检测", "机械操作", "普工", "技工"};

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        RxTextView.textChanges(ed_searchorder_word)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(CharSequence charSequence) throws Exception {
                        //请求网络数据
                        return observer -> {
                            if (!charSequence.toString().equals("")) {
                                searchOrder(charSequence.toString());
                            }
                            observer.onNext(charSequence.toString());
                        };
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.equals("")) {
                        beans.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
        for (String s : choiceArray) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTextColor(Color.parseColor("#999999"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.shape_rounded_corner_gray);
            textView.setPadding(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(8), 0);
            textView.setText(s);
            textView.setOnClickListener(v -> {
                ed_searchorder_word.setText(s);
                ed_searchorder_word.setSelection(s.length());
            });
            ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin= SizeUtils.dp2px(4);
            params.rightMargin= SizeUtils.dp2px(4);
            params.bottomMargin= SizeUtils.dp2px(4);
            params.height= SizeUtils.dp2px(30);
            textView.setLayoutParams(params);
            flow_searchorder_hot.addView(textView);
        }
        rv_searchorder.setHasFixedSize(true);
        rv_searchorder.setLayoutManager(new LinearLayoutManager(this));
        adapter=new OrderListAdapter(this, beans, true);
        adapter.setOnClickListener(position -> {
            Intent intent=new Intent(SearchOrderActivtiy.this, OrderDetailActivity.class);
            intent.putExtra("orderId", beans.get(position).getOrderId());
            // 雇员待接单采用雇主订单详情
            intent.putExtra("typeIsCommit", true);
            startActivity(intent);
        });
        rv_searchorder.setAdapter(adapter);
        rv_searchorder.setOnTouchListener((v, event) -> {
            if (event.getAction()==MotionEvent.ACTION_MOVE) {
                KeyboardUtils.hideSoftInput(SearchOrderActivtiy.this);
            }
            return false;
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_searchorder;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @OnClick({R.id.tv_searchorder_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_searchorder_cancel:
                finish();
                break;
        }
    }

    private void searchOrder(String keyWord) {
        SearchOrderRequest request=new SearchOrderRequest();
        SearchOrderRequest.ParamBean paramBean=new SearchOrderRequest.ParamBean();
        paramBean.setCondition(keyWord);
        paramBean.setUserId(ACache.get(this).getAsString(CommonParams.USER_ID));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .searchOrder(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.backgroundList()).subscribe(new Observer<List<MyOrderListResponse.DataBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<MyOrderListResponse.DataBean> value) {
                beans.clear();
                beans.addAll(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 员工状态变化以刷新
    // 雇员接单状态变化以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OrderResponse response) {
        searchOrder(ed_searchorder_word.getText().toString());
    }
}
