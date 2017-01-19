package com.renyu.imagelibrary.preview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.imagelibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.relex.circleindicator.CircleIndicator;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by renyu on 16/1/31.
 */
public class ImagePreviewActivity extends BaseActivity {

    MultiTouchViewPager imagepreview_viewpager;
    CircleIndicator imagepreview_indicator;

    int position=0;
    ArrayList<String> urls;
    ArrayList<PhotoDraweeView> photoDraweeViews;
    //是否可以下载
    boolean canDownload;

    HashMap<String, ImageInfo> point;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_imagepreview;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        point=new HashMap<>();

        imagepreview_viewpager= (MultiTouchViewPager) findViewById(R.id.imagepreview_viewpager);
        imagepreview_indicator= (CircleIndicator) findViewById(R.id.imagepreview_indicator);

        position=getIntent().getExtras().getInt("position");
        urls=getIntent().getExtras().getStringArrayList("urls");
        canDownload=getIntent().getExtras().getBoolean("canDownload");
        photoDraweeViews=new ArrayList<>();
        for (int i=0;i<urls.size();i++) {
            PhotoDraweeView photoDraweeView=new PhotoDraweeView(this);
            photoDraweeViews.add(photoDraweeView);
        }
        imagepreview_viewpager.setAdapter(new MyPagerAdapter());
        imagepreview_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                Observable.timer(300, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (position-1>=0&&point.containsKey(""+(position-1))) {
                            photoDraweeViews.get((position-1)).update(point.get(""+(position-1)).getWidth(), point.get(""+(position-1)).getHeight());
                        }
                        if (position+1<=urls.size()&&point.containsKey(""+(position+1))) {
                            photoDraweeViews.get((position+1)).update(point.get(""+(position+1)).getWidth(), point.get(""+(position+1)).getHeight());
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagepreview_indicator.setViewPager(imagepreview_viewpager);
        imagepreview_viewpager.setCurrentItem(position);
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoDraweeViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(photoDraweeViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PhotoDraweeView photoDraweeView=photoDraweeViews.get(position);
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse(urls.get(position).indexOf("http")!=-1?urls.get(position):"file://"+urls.get(position)));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    point.put(""+position, imageInfo);
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());
            photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ImagePreviewActivity.this.finish();
                }
            });
            photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(ImagePreviewActivity.this).setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which==0 && canDownload) {
                                Intent intent=new Intent("ImagePreviewDownload");
                                intent.putExtra("image", urls.get(position));
                                sendBroadcast(intent);
                            }
                        }
                    }).show();
                    return true;
                }
            });
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoDraweeView;
        }
    }
}
