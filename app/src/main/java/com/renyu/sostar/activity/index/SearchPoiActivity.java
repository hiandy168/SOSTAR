package com.renyu.sostar.activity.index;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.SearchPoiAdapter;
import com.renyu.sostar.params.CommonParams;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by renyu on 2017/3/29.
 */

public class SearchPoiActivity extends BaseActivity implements OnGetPoiSearchResultListener {

    @BindView(R.id.ed_searchpoi_word)
    EditText ed_searchpoi_word;
    @BindView(R.id.rv_searchpoi)
    RecyclerView rv_searchpoi;
    SearchPoiAdapter adapter;

    PoiSearch mPoiSearch = null;

    ArrayList<PoiInfo> beans;

    @Override
    public void initParams() {
        beans=new ArrayList<>();

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        RxTextView.textChanges(ed_searchpoi_word)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.toString().trim().length() > 0)
                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(CharSequence charSequence) throws Exception {
                        //请求网络数据
                        return observer -> {
                            mPoiSearch.searchInCity((new PoiCitySearchOption())
                                    .city(CommonParams.CITY).keyword(charSequence.toString()).pageCapacity(30).pageNum(0));
                            observer.onNext("");
                        };
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResultModels -> {

                });
        ed_searchpoi_word.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH) {
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(CommonParams.CITY).keyword(ed_searchpoi_word.getText().toString()).pageCapacity(30).pageNum(0));
            }
            return false;
        });
        rv_searchpoi.setHasFixedSize(true);
        rv_searchpoi.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SearchPoiAdapter(this, beans);
        rv_searchpoi.setAdapter(adapter);
    }

    @Override
    public int initViews() {
        return R.layout.activity_searchpoi;
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

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        beans.clear();
        if (poiResult.getAllPoi()!=null) {
            beans.addAll(poiResult.getAllPoi());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }

    @OnClick({R.id.tv_searchpoi_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_searchpoi_cancel:
                finish();
                break;
        }
    }
}
