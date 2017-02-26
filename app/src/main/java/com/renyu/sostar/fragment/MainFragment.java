package com.renyu.sostar.fragment;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.sostar.R;

import butterknife.BindView;

/**
 * Created by renyu on 2017/2/26.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.main_mapView)
    MapView main_mapView;
    BaiduMap mBaiduMap;

    @Override
    public void initParams() {
        mBaiduMap=main_mapView.getMap();
        // 设置普通模式
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        // 开启定位图层
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
    }

    @Override
    public int initViews() {
        return R.layout.fragment_main;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        main_mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        main_mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        main_mapView.onDestroy();
        main_mapView = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        main_mapView.onSaveInstanceState(outState);
    }
}
