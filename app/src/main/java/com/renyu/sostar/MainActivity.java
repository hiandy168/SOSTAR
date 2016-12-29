package com.renyu.sostar;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.renyu.sostar.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_mapview)
    MapView main_mapview;
    BaiduMap mBaiduMap;
    LocationClient mLocClient;

    ClusterManager<MyMarkerOptions> mClusterManager;

    @Override
    public void initParams() {
        mBaiduMap=main_mapview.getMap();
        // 设置普通模式
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        // 开启定位图层
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
//        mBaiduMap.setOnMarkerClickListener(marker -> {
//            Toast.makeText(MainActivity.this, "点击了" + marker.getTitle(), Toast.LENGTH_SHORT).show();
//            return true;
//        });
        mBaiduMap.setMyLocationEnabled(true);
        mClusterManager=new ClusterManager<>(this, mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(cluster -> {
            Log.d("MainActivity", "有" + cluster.getSize() + "个点");
            return false;
        });
        mClusterManager.setOnClusterItemClickListener(item -> {
            Log.d("MainActivity", "点击单个Item" + item.getTitle());
            return false;
        });
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(bdLocation -> {
            if (bdLocation == null || bdLocation == null) {
                return;
            }
            // 关闭定位
            mLocClient.stop();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            // 设置地图中心点
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(ll).zoom(16).build()));

//            LatLng llA = new LatLng(32.065102, 118.790243);
//            MarkerOptions optionsA=new MarkerOptions()
//                    .animateType(MarkerOptions.MarkerAnimateType.grow)
//                    .title("A")
//                    .position(llA)
//                    .zIndex(0)
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
//            mBaiduMap.addOverlay(optionsA);

            addMarkers();

//            changeInfoWindow(ll, Color.BLUE);
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000);
        option.setAddrType("all");
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void addMarkers() {
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);
        LatLng llE = new LatLng(39.956965, 116.331394);
        LatLng llF = new LatLng(39.886965, 116.441394);
        LatLng llG = new LatLng(39.996965, 116.411394);

        List<MyMarkerOptions> items = new ArrayList<MyMarkerOptions>();
        items.add(new MyMarkerOptions(llA, "llA"));
        items.add(new MyMarkerOptions(llB, "llA"));
        items.add(new MyMarkerOptions(llC, "llA"));
        items.add(new MyMarkerOptions(llD, "llA"));
        items.add(new MyMarkerOptions(llE, "llA"));
        items.add(new MyMarkerOptions(llF, "llA"));
        items.add(new MyMarkerOptions(llG, "llA"));

        mClusterManager.addItems(items);
    }

    public class MyMarkerOptions implements ClusterItem {
        private final LatLng mPosition;
        private final String mTitle;

        public MyMarkerOptions(LatLng latLng, String title) {
            mPosition = latLng;
            mTitle = title;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

    }

    /**
     * 改变InfoWindow
     * @param ll
     * @param color
     */
    private void changeInfoWindow(LatLng ll, int color) {
        Button button = new Button(getApplicationContext());
        button.setGravity(Gravity.CENTER);
        button.setText("更改位置");
        button.setBackgroundColor(color);
        button.setWidth(new Random().nextInt(300)+300);
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, () -> {
            changeInfoWindow(ll, Color.YELLOW);
        });
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @Override
    public int initViews() {
        return R.layout.activity_main;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        main_mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        main_mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        main_mapview.onDestroy();
        main_mapview = null;
    }
}
