package com.renyu.sostar.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.order.NotStartedOrderListActivity;
import com.renyu.sostar.activity.order.OrderDetailActivity;
import com.renyu.sostar.activity.order.ReleaseOrderActivity;
import com.renyu.sostar.activity.user.InfoActivity;
import com.renyu.sostar.bean.EmployeeIndexRequest;
import com.renyu.sostar.bean.EmployeeIndexResponse;
import com.renyu.sostar.bean.EmployerIndexResponse;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;
import com.renyu.sostar.bean.MyCenterEmployerResponse;
import com.renyu.sostar.bean.ReleaseOrderRequest;
import com.renyu.sostar.impl.RetrofitImpl;
import com.renyu.sostar.params.CommonParams;
import com.renyu.sostar.service.LocationService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by renyu on 2017/2/26.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.mv_main)
    TextureMapView mv_main;
    BaiduMap mBaiduMap;
    @BindView(R.id.tv_main_notstartorder_num)
    TextView tv_main_notstartorder_num;
    @BindView(R.id.tv_main_open_space)
    View tv_main_open_space;
    @BindView(R.id.tv_main_oper)
    TextView tv_main_oper;

    // 是否地图第一次定位加载成功
    boolean isFirstLoc=true;
    // 用户头像
    Bitmap avatarBmp;
    // 用户坐标
    BDLocation bdLocation;
    // 用户marker
    Marker avatarMarker;
    // 所有marker
    ArrayList<Marker> allMarkers;
    // 地图加载成功
    boolean isMapLoaded=false;
    // 雇员或者雇主图标
    BitmapDescriptor bd;

    // 地图请求Disposable
    Disposable infoDisposable;
    // 定时刷新Disposable
    Disposable refreshDisposable;

    @Override
    public void initParams() {
        allMarkers=new ArrayList<>();

        mBaiduMap=mv_main.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        mBaiduMap.setOnMapLoadedCallback(() ->  {
            isMapLoaded=true;

            // 开启定位上报
            getActivity().startService(new Intent(getActivity(), LocationService.class));

        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
        mBaiduMap.setOnMarkerClickListener(marker -> {
            // 点击不是自己的头像
            if (marker.getZIndex()!=0) {
                if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    Intent intent=new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("orderId", ""+marker.getZIndex());
                    intent.putExtra("typeIsCommit", true);
                    startActivity(intent);
                }
                else if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("1")) {
                    Intent intent=new Intent(getActivity(), InfoActivity.class);
                    intent.putExtra("userId", ""+marker.getZIndex());
                    startActivity(intent);
                }
            }
            return false;
        });
        if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
            tv_main_oper.setText("立即接单");
        }
        else if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("1")) {
            tv_main_oper.setText("立即发单");
        }
    }

    @Override
    public int initViews() {
        return R.layout.fragment_main;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mv_main.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mv_main.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().stopService(new Intent(getActivity(), LocationService.class));

        EventBus.getDefault().unregister(this);

        mv_main.onDestroy();
        mv_main = null;

        if (refreshDisposable!=null) {
            refreshDisposable.dispose();
        }
        if (infoDisposable!=null) {
            infoDisposable.dispose();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv_main.onSaveInstanceState(outState);
    }

    @OnClick({R.id.layout_main_oper, R.id.iv_main_mylocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_main_oper:
                if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
                    startActivity(new Intent(getActivity(), NotStartedOrderListActivity.class));
                }
                else {
                    if (bdLocation!=null) {
                        startActivity(new Intent(getActivity(), ReleaseOrderActivity.class));
                    }
                }
                break;
            case R.id.iv_main_mylocation:
                if (bdLocation!=null) {
                    LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
                break;
        }
    }

    // 刷新地理位置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getDirection())
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        MainFragment.this.bdLocation=bdLocation;
        addUserOverLay(avatarBmp, bdLocation);
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            // 开启定时分别加载地图相关信息
            Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
                @Override
                public void onSubscribe(Disposable d) {
                    refreshDisposable=d;
                }

                @Override
                public void onNext(Long value) {
                    if (isMapLoaded) {
                        if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("0")) {
                            loadEmployeeIndex(bdLocation);
                        }
                        else if (ACache.get(getActivity()).getAsString(CommonParams.USER_TYPE).equals("1")) {
                            loadEmployerIndex(bdLocation);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    // 获取头像以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyCenterEmployeeResponse response) {
        loadAvatarBitmap(response.getPicPath());
    }

    // 获取头像以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyCenterEmployerResponse response) {
        loadAvatarBitmap(response.getLogoPath());
    }

    // 发单成功以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ReleaseOrderRequest request) {
        if (TextUtils.isEmpty(tv_main_notstartorder_num.getText().toString())) {
            tv_main_notstartorder_num.setText("1");
        }
        else {
            tv_main_notstartorder_num.setText(""+(Integer.parseInt(tv_main_notstartorder_num.getText().toString())+1));
        }
        tv_main_notstartorder_num.setVisibility(View.VISIBLE);
        tv_main_open_space.setVisibility(View.VISIBLE);
    }

    /**
     * 头像改变之后刷新头像
     * @param avatarUrl
     */
    private void loadAvatarBitmap(String avatarUrl) {
        if (TextUtils.isEmpty(avatarUrl)) {
            avatarBmp= BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_avatar_large);
            addUserOverLay(avatarBmp, bdLocation);
            return;
        }
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(avatarUrl)).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(final Bitmap bitmap) {
                Observable.just(bitmap).observeOn(AndroidSchedulers.mainThread()).subscribe(bitmap1 -> {
                    avatarBmp=bitmap1;
                    addUserOverLay(bitmap1, bdLocation);
                });
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                avatarBmp= BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_avatar_large);
                addUserOverLay(avatarBmp, bdLocation);
            }
        }, CallerThreadExecutor.getInstance());
    }

    /**
     * 雇员加载订单信息
     * @param bdLocation
     */
    private void loadEmployeeIndex(BDLocation bdLocation) {
        EmployeeIndexRequest request=new EmployeeIndexRequest();
        EmployeeIndexRequest.ParamBean paramBean=new EmployeeIndexRequest.ParamBean();
        paramBean.setUserId(ACache.get(getActivity()).getAsString(CommonParams.USER_ID));
        paramBean.setLatitude(""+bdLocation.getLatitude());
        paramBean.setLongitude(""+bdLocation.getLongitude());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getEmployeeIndex(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployeeIndexResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                infoDisposable=d;
            }

            @Override
            public void onNext(EmployeeIndexResponse value) {
                if (value.getCount()>0) {
                    tv_main_notstartorder_num.setText(""+value.getCount());
                    tv_main_notstartorder_num.setVisibility(View.VISIBLE);
                    tv_main_open_space.setVisibility(View.VISIBLE);
                }
                else {
                    tv_main_notstartorder_num.setVisibility(View.GONE);
                    tv_main_open_space.setVisibility(View.GONE);
                }
                addEmployeeOverLay(value);
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

    /**
     * 雇主加载雇员信息
     * @param bdLocation
     */
    private void loadEmployerIndex(BDLocation bdLocation) {
        EmployeeIndexRequest request=new EmployeeIndexRequest();
        EmployeeIndexRequest.ParamBean paramBean=new EmployeeIndexRequest.ParamBean();
        paramBean.setUserId(ACache.get(getActivity()).getAsString(CommonParams.USER_ID));
        paramBean.setLatitude(""+bdLocation.getLatitude());
        paramBean.setLongitude(""+bdLocation.getLongitude());
        request.setParam(paramBean);
        retrofit.create(RetrofitImpl.class)
                .getEmployerIndex(Retrofit2Utils.postJsonPrepare(new Gson().toJson(request)))
                .compose(Retrofit2Utils.background()).subscribe(new Observer<EmployerIndexResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                infoDisposable=d;
            }

            @Override
            public void onNext(EmployerIndexResponse value) {
                if (value.getCount()>0) {
                    tv_main_notstartorder_num.setText(""+value.getCount());
                    tv_main_notstartorder_num.setVisibility(View.VISIBLE);
                    tv_main_open_space.setVisibility(View.VISIBLE);
                }
                else {
                    tv_main_notstartorder_num.setVisibility(View.GONE);
                    tv_main_open_space.setVisibility(View.GONE);
                }
                addEmployerOverLay(value);
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

    /**
     * 刷新使用者的位置
     * @param bitmap
     * @param bdLocation
     */
    private void addUserOverLay(Bitmap bitmap, BDLocation bdLocation) {
        if (bitmap==null) {
            return;
        }
        if (bdLocation==null) {
            return;
        }
        if (avatarMarker!=null) {
            avatarMarker.remove();
        }

        View view=LayoutInflater.from(getActivity()).inflate(R.layout.view_mapitem, null, false);
        CircleImageView iv_mapitem_avatar= (CircleImageView) view.findViewById(R.id.iv_mapitem_avatar);
        iv_mapitem_avatar.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        view.destroyDrawingCache();
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        BitmapDescriptor bd= BitmapDescriptorFactory.fromBitmap(view.getDrawingCache());
        MarkerOptions oo = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()))
                .icon(bd)
                .zIndex(0);
        oo.animateType(MarkerOptions.MarkerAnimateType.grow);
        avatarMarker = (Marker) (mBaiduMap.addOverlay(oo));
    }

    private void addEmployeeOverLay(EmployeeIndexResponse value) {
        for (Marker allOrdersMarker : allMarkers) {
            allOrdersMarker.remove();
        }
        allMarkers.clear();
        EmployeeIndexResponse.OrdersBean[] beans=new EmployeeIndexResponse.OrdersBean[value.getOrders().size()];
        for (int i = 0; i < value.getOrders().size(); i++) {
            beans[i]=value.getOrders().get(i);
        }
        bd= BitmapDescriptorFactory.fromResource(R.mipmap.ic_main_comp);
        Observable.fromArray(beans).map(ordersBean -> {
            if (TextUtils.isEmpty(ordersBean.getLatitude()) || TextUtils.isEmpty(ordersBean.getLongitude())) {
                return false;
            }
            MarkerOptions oo = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(ordersBean.getLatitude()), Double.parseDouble(ordersBean.getLongitude())))
                    .icon(bd)
                    .zIndex(Integer.parseInt(ordersBean.getOrderId()));
            oo.animateType(MarkerOptions.MarkerAnimateType.grow);
            allMarkers.add((Marker) (mBaiduMap.addOverlay(oo)));
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

        });
    }

    private void addEmployerOverLay(EmployerIndexResponse value) {
        for (Marker allOrdersMarker : allMarkers) {
            allOrdersMarker.remove();
        }
        allMarkers.clear();
        EmployerIndexResponse.StaffsBean[] beans=new EmployerIndexResponse.StaffsBean[value.getStaffs().size()];
        for (int i = 0; i < value.getStaffs().size(); i++) {
            beans[i]=value.getStaffs().get(i);
        }
        bd= BitmapDescriptorFactory.fromResource(R.mipmap.ic_main_employee);
        Observable.fromArray(beans).map(staffsBean -> {
            if (TextUtils.isEmpty(staffsBean.getLatitude()) || TextUtils.isEmpty(staffsBean.getLongitude())) {
                return false;
            }
            MarkerOptions oo = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(staffsBean.getLatitude()), Double.parseDouble(staffsBean.getLongitude())))
                    .icon(bd)
                    .zIndex(Integer.parseInt(staffsBean.getUserId()));
            oo.animateType(MarkerOptions.MarkerAnimateType.grow);
            allMarkers.add((Marker) (mBaiduMap.addOverlay(oo)));
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

        });
    }
}
