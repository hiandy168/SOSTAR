package com.renyu.sostar.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
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
import com.renyu.commonlibrary.basefrag.BaseFragment;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.MyCenterEmployeeResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by renyu on 2017/2/26.
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.mv_main)
    MapView mv_main;
    BaiduMap mBaiduMap;

    // 是否地图第一次定位加载成功
    boolean isFirstLoc=true;
    // 用户头像
    Bitmap avatarBmp;
    // 用户坐标
    BDLocation bdLocation;
    // 用户marker
    Marker avatarMarker;

    @Override
    public void initParams() {
        mBaiduMap=mv_main.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        mBaiduMap.setOnMapLoadedCallback(() ->  addUserOverLay(avatarBmp, bdLocation));
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

        EventBus.getDefault().unregister(this);

        mv_main.onDestroy();
        mv_main = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv_main.onSaveInstanceState(outState);
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
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        MainFragment.this.bdLocation=bdLocation;
        addUserOverLay(avatarBmp, bdLocation);
    }

    // 获取头像以刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyCenterEmployeeResponse response) {
        loadAvatarBitmap(response.getPicPath());
    }

    private void loadAvatarBitmap(String avatarUrl) {
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

            }
        }, CallerThreadExecutor.getInstance());
    }

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
        TextView tv_mapitem_text= (TextView) view.findViewById(R.id.tv_mapitem_text);
        ImageView iv_mapitem_avatar= (ImageView) view.findViewById(R.id.iv_mapitem_avatar);
        iv_mapitem_avatar.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        view.destroyDrawingCache();
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        BitmapDescriptor bd2= BitmapDescriptorFactory.fromBitmap(view.getDrawingCache());
        MarkerOptions oo = new MarkerOptions().position(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).icon(bd2);
        oo.animateType(MarkerOptions.MarkerAnimateType.grow);
        avatarMarker = (Marker) (mBaiduMap.addOverlay(oo));
    }
}
