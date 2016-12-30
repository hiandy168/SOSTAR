package com.baidu.mapapi.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renyu on 2016/12/29.
 */

public class TrackShowDemoActivity extends AppCompatActivity {

    MapView trackshow_mapView;
    BaiduMap baiduMap;
    LocationClient mLocClient;
    Polyline polyline;
    Marker mMoveMarker;

    Handler handler;

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0002;

    private static final LatLng[] latlngs = new LatLng[] {
            new LatLng(32.089089, 118.790315),
            new LatLng(32.083827, 118.790458),
            new LatLng(32.076485, 118.790746),
            new LatLng(32.071222, 118.79053),
            new LatLng(32.065347, 118.790243),
            new LatLng(32.064429, 118.797142),
            new LatLng(32.063695, 118.804328),
            new LatLng(32.063878, 118.812377),
            new LatLng(32.064062, 118.819204),
            new LatLng(32.071895, 118.822726),
            new LatLng(32.07832, 118.818629),
            new LatLng(32.083338, 118.816689),
            new LatLng(32.089212, 118.813527),
            new LatLng(32.091414, 118.807922),
            new LatLng(32.093311, 118.80246),
            new LatLng(32.091842, 118.798004)
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackshowdemo);

        handler=new Handler(Looper.getMainLooper());

        initViews();
    }

    private void initViews() {
        trackshow_mapView= (MapView) findViewById(R.id.trackshow_mapView);
        baiduMap=trackshow_mapView.getMap();
        // 设置普通模式
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        // 开启定位图层
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || bdLocation == null) {
                    return;
                }
                // 关闭定位
                mLocClient.stop();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                baiduMap.setMyLocationData(locData);
                // 设置地图中心点
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(ll).zoom(16).build()));

                drawPolyLine();
                moveLooper();
            }
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

    private void drawPolyLine() {
        List<LatLng> drawPolyLine=new ArrayList<>();
        for (LatLng latlng : latlngs) {
            drawPolyLine.add(latlng);
        }
        drawPolyLine.add(latlngs[0]);
        PolylineOptions options=new PolylineOptions().points(drawPolyLine).width(10).color(Color.RED);
        polyline= (Polyline) baiduMap.addOverlay(options);
        OverlayOptions overlayOptions=new MarkerOptions()
                .position(latlngs[0])
                .anchor(0.5f, 0.5f)
                .flat(true)
                .rotate((float) getAngle(0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.arrow));
        mMoveMarker= (Marker) baiduMap.addOverlay(overlayOptions);
    }

    private void moveLooper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < latlngs.length-1; i++) {
                    final LatLng startPoint = latlngs[i];
                    final LatLng endPoint = latlngs[i + 1];
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (trackshow_mapView == null) {
                                return;
                            }
                            mMoveMarker.setPosition(startPoint);
                            mMoveMarker.setRotate((float) getAngle(startPoint, endPoint));
                        }
                    });
                    double slope = getSlope(startPoint, endPoint);
                    // 是不是正向的标示
                    boolean isReverse = (startPoint.latitude > endPoint.latitude);
                    double intercept = getInterception(slope, startPoint);
                    double xMoveDistance = isReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);
                    for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j - xMoveDistance) {
                        LatLng latLng;
                        if (slope == Double.MAX_VALUE) {
                            latLng = new LatLng(j, startPoint.longitude);
                        }
                        else {
                            latLng = new LatLng(j, (j - intercept) / slope);
                        }
                        final LatLng finalLatLng = latLng;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (trackshow_mapView == null) {
                                    return;
                                }
                                mMoveMarker.setPosition(finalLatLng);
                            }
                        });
                        try {
                            Thread.sleep(TIME_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= polyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = polyline.getPoints().get(startIndex);
        LatLng endPoint = polyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {
        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }


    @Override
    protected void onResume() {
        super.onResume();
        trackshow_mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        trackshow_mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        baiduMap.setMyLocationEnabled(false);
        trackshow_mapView.onDestroy();
        trackshow_mapView = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        trackshow_mapView.onSaveInstanceState(outState);
    }
}
