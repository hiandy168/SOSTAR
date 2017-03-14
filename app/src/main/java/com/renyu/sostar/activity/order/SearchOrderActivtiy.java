package com.renyu.sostar.activity.order;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.FlowLayout;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.SearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by renyu on 2017/3/14.
 */

public class SearchOrderActivtiy extends BaseActivity {

    @BindView(R.id.ed_search_word)
    EditText ed_search_word;
    @BindView(R.id.layout_search_hot)
    LinearLayout layout_search_hot;
    @BindView(R.id.flow_search_hot)
    FlowLayout flow_search_hot;

    String[] choiceArray={"品质检测", "机械操作", "普工", "技工"};

    @Override
    public void initParams() {
        RxTextView.textChanges(ed_search_word)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.toString().trim().length() > 0)
                .switchMap(new Function<CharSequence, ObservableSource<List<SearchResponse>>>() {
                    @Override
                    public ObservableSource<List<SearchResponse>> apply(CharSequence charSequence) throws Exception {
                        //请求网络数据
                        return observer -> {
                            List<SearchResponse> res = new ArrayList<>();
                            observer.onNext(res);
                        };
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResultModels -> {

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

            });
            ViewGroup.MarginLayoutParams params=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin= SizeUtils.dp2px(4);
            params.rightMargin= SizeUtils.dp2px(4);
            params.bottomMargin= SizeUtils.dp2px(4);
            params.height= SizeUtils.dp2px(30);
            textView.setLayoutParams(params);
            flow_search_hot.addView(textView);

        }
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

    @OnClick({R.id.tv_search_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancel:
                finish();
                break;
        }
    }
}
