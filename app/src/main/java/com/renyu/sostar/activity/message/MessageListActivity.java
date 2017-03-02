package com.renyu.sostar.activity.message;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.networkutils.Retrofit2Utils;
import com.renyu.commonlibrary.networkutils.params.EmptyResponse;
import com.renyu.commonlibrary.views.ActionSheetFragment;
import com.renyu.commonlibrary.views.ActionSheetUtils;
import com.renyu.jpushlibrary.bean.NotificationBean;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.MessageListAdapter;
import com.renyu.sostar.bean.DeleteAllMessageRequest;
import com.renyu.sostar.bean.DeleteOneMessageRequest;
import com.renyu.sostar.bean.MsgListRequest;
import com.renyu.sostar.bean.MsgListResponse;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by renyu on 2017/2/28.
 */

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.nav_layout)
    RelativeLayout nav_layout;
    @BindView(R.id.tv_nav_title)
    TextView tv_nav_title;
    @BindView(R.id.tv_nav_right)
    TextView tv_nav_right;
    @BindView(R.id.swipy_messagelist)
    SwipyRefreshLayout swipy_messagelist;
    @BindView(R.id.rv_messagelist)
    RecyclerView rv_messagelist;
    MessageListAdapter adapter;

    ArrayList<Object> beans;

    int page=1;

    Disposable disposable;

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        nav_layout.setBackgroundColor(Color.WHITE);
        tv_nav_title.setText("消息中心");
        tv_nav_right.setText("清空");
        tv_nav_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        swipy_messagelist.setOnRefreshListener(direction -> {
            if (direction==SwipyRefreshLayoutDirection.BOTTOM) {

            }
            else if (direction==SwipyRefreshLayoutDirection.TOP) {
                page=1;
            }
            getMsgList();
        });
        rv_messagelist.setHasFixedSize(true);
        rv_messagelist.setLayoutManager(new LinearLayoutManager(this));
        rv_messagelist.setItemAnimator(new DefaultItemAnimator());
        adapter=new MessageListAdapter(this, beans, new MessageListAdapter.OnMessageCtrolListener() {
            @Override
            public void delete(int position) {
                deleteOneMessage(position);
            }

            @Override
            public void read(int position) {
                readMessage(position);
            }
        });
        rv_messagelist.setAdapter(adapter);
    }

    @Override
    public int initViews() {
        return R.layout.activity_messagelist;
    }

    @Override
    public void loadData() {
        swipy_messagelist.post(() -> {
            swipy_messagelist.setRefreshing(true);
            getMsgList();
        });
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

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ib_nav_left, R.id.tv_nav_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
            case R.id.tv_nav_right:
                ActionSheetUtils.showDouble(getSupportFragmentManager(), "清空消息", "取消", "清空",
                        value -> deleteAllMessage(), () -> {

                });
                break;
        }
    }

    private void getMsgList() {
        MsgListRequest request=new MsgListRequest();
        MsgListRequest.ParamBean paramBean=new MsgListRequest.ParamBean();
        MsgListRequest.ParamBean.PaginationBean paginationBean=new MsgListRequest.ParamBean.PaginationBean();
        paginationBean.setPageSize(15);
        paginationBean.setStartPos(page);
        paramBean.setPagination(paginationBean);
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class).msgList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<MsgListResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(MsgListResponse value) {
                if (page==1) {
                    beans.clear();
                }
                SimpleDateFormat dateFormatTime=new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < value.getData().size(); i++) {
                    // 最开始的第一个
                    if ((beans.size()+i)==0) {
                        beans.add(checkTime(value.getData().get(i).getCrtTime()));
                    }
                    else {
                        Date date=new Date();
                        date.setTime(((MsgListResponse.DataBean) (beans.get(beans.size()-1))).getCrtTime());
                        String preview=dateFormatTime.format(date);
                        date.setTime(value.getData().get(i).getCrtTime());
                        String now=dateFormatTime.format(date);
                        if (!now.equals(preview)) {
                            beans.add(checkTime(value.getData().get(i).getCrtTime()));
                        }
                    }
                    beans.add(value.getData().get(i));
                }
                adapter.notifyDataSetChanged();
                page++;
                swipy_messagelist.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipy_messagelist.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void deleteOneMessage(int position) {
        DeleteOneMessageRequest request=new DeleteOneMessageRequest();
        DeleteOneMessageRequest.ParamBean paramBean=new DeleteOneMessageRequest.ParamBean();
        paramBean.setMessageId(Integer.parseInt(((MsgListResponse.DataBean) beans.get(position)).getMessageId()));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .deleteMsg(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(MessageListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                adapter.notifyItemRemoved(position);
                beans.remove(position);

                outer:
                while (beans.size()!=0) {
                    for (int i = 0; i < beans.size(); i++) {
                        if (beans.get(i) instanceof String) {
                            // 首先检查当前时间节点是否为最后一个节点，是则需要删除
                            if (i==beans.size()-1) {
                                adapter.notifyItemRemoved(i);
                                beans.remove(i);
                                continue outer;
                            }
                            // 其次检查当前时间节点下方item是否依然是时间节点，是则需要删除
                            else if (beans.get(i+1) instanceof String) {
                                adapter.notifyItemRemoved(i);
                                beans.remove(i);
                                continue outer;
                            }
                        }
                    }
                    break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MessageListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void readMessage(int position) {
        DeleteOneMessageRequest request=new DeleteOneMessageRequest();
        DeleteOneMessageRequest.ParamBean paramBean=new DeleteOneMessageRequest.ParamBean();
        paramBean.setMessageId(Integer.parseInt(((MsgListResponse.DataBean) beans.get(position)).getMessageId()));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .readMsg(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                ((MsgListResponse.DataBean) beans.get(position)).setReadFlg("1");
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

    private void deleteAllMessage() {
        DeleteAllMessageRequest request=new DeleteAllMessageRequest();
        DeleteAllMessageRequest.ParamBean paramBean=new DeleteAllMessageRequest.ParamBean();
        paramBean.setUserId(Integer.parseInt(ACache.get(this).getAsString(CommonParams.USER_ID)));
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .deleteMsgList(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmptyResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(EmptyResponse value) {
                Toast.makeText(MessageListActivity.this, value.getMessage(), Toast.LENGTH_SHORT).show();

                beans.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MessageListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String checkTime(long time) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        String todayText=dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String yesterdayText=dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String beforeYesterdayText=dateFormat.format(calendar.getTime());
        Date date=new Date();
        date.setTime(time);
        String today=dateFormat.format(date);
        if (todayText.equals(today)) {
            return "今天";
        }
        if (yesterdayText.equals(today)) {
            return "昨天";
        }
        if (beforeYesterdayText.equals(today)) {
            return "前天";
        }
        return today;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NotificationBean bean) {
        swipy_messagelist.setRefreshing(true);
        page=1;
        getMsgList();
    }
}
