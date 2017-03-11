package com.renyu.sostar.activity.order;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.R;

import java.util.ArrayList;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by renyu on 2017/3/10.
 */

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.vp_orderdetail)
    ViewPager vp_orderdetail;
    OrderDetailAdapter adapter;
    @BindView(R.id.indicator_orderdetail)
    CircleIndicator indicator_orderdetail;

    ArrayList<String> images;
    ArrayList<View> views;

    @Override
    public void initParams() {
        images=new ArrayList<>();
        images.add("http://img13.360buyimg.com/cms/jfs/t3103/222/8040064852/71576/3797843b/58bcb258N6a39eee7.jpg");
        images.add("http://img10.360buyimg.com/n1/s900x900_g12/M00/0A/1A/rBEQYFGjKkkIAAAAAATpHoK00ycAACHPQGXBhIABOk2022.jpg");
        images.add("http://img10.360buyimg.com/n1/s900x900_g12/M00/0A/1A/rBEQYVGjKlQIAAAAAASO8hZgA_UAACHPQK0ZpgABI8K724.jpg");
        images.add("http://img10.360buyimg.com/n1/s900x900_g12/M00/0A/1A/rBEQYFGjKlsIAAAAAAO-T6_hsrQAACHPQO2YaMAA75n987.jpg");
        views=new ArrayList<>();
        for (String image : images) {
            View view= LayoutInflater.from(this).inflate(R.layout.adapter_orderdetail, null, false);
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(image)).setAutoPlayAnimations(true).build();
            ((SimpleDraweeView) view).setController(draweeController);
            views.add(view);
        }
        adapter=new OrderDetailAdapter();
        vp_orderdetail.setAdapter(adapter);
        indicator_orderdetail.setViewPager(vp_orderdetail);
    }

    @Override
    public int initViews() {
        return R.layout.activity_orderdetail;
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

    public class OrderDetailAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
