package com.renyu.sostar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.MyOrderListResponse;
import com.renyu.sostar.service.LocationService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/10.
 */

public class NotStartedOrderListAdapter extends RecyclerView.Adapter<NotStartedOrderListAdapter.NotStartedOrderListHolder> {

    Context context;
    ArrayList<MyOrderListResponse.DataBean> beans;

    public NotStartedOrderListAdapter(Context context, ArrayList<MyOrderListResponse.DataBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public NotStartedOrderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_notstartedorderlist, parent, false);
        return new NotStartedOrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(NotStartedOrderListHolder holder, int position) {
        holder.tv_notstartedorderlist_type.setText(beans.get(position).getJobType());
        holder.tv_notstartedorderlist_person.setText(""+beans.get(position).getStaffAccount()+"人");
        String times="";
        String[] timeTemps=beans.get(position).getPeriodTime().split(",");
        for (String timeTemp : timeTemps) {
            times+=timeTemp+"  "+beans.get(position).getStartTime()+"-"+beans.get(position).getEndTime()+"\n";
        }
        holder.tv_notstartedorderlist_time.setText(times);
        holder.tv_notstartedorderlist_price.setText(""+beans.get(position).getUnitPrice());
        if (beans.get(position).getPaymentType().equals("1")) {
            holder.tv_notstartedorderlist_price_type.setText("/天");
        }
        else if (beans.get(position).getPaymentType().equals("2")) {
            holder.tv_notstartedorderlist_price_type.setText("/小时");
        }
        holder.tv_notstartedorderlist_comp.setText(beans.get(position).getCompanyName());
        LatLng userLatlng=new LatLng(LocationService.lastBdLocation.getLatitude(), LocationService.lastBdLocation.getLongitude());
        LatLng orderLatlng=new LatLng(Double.parseDouble(beans.get(position).getLatitude()), Double.parseDouble(beans.get(position).getLongitude()));
        holder.tv_notstartedorderlist_distance.setText(((int) DistanceUtil.getDistance(userLatlng, orderLatlng))+"m");
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(beans.get(position).getLogoPath())).setAutoPlayAnimations(true).build();
        holder.notstartedorderlist_logo.setController(draweeController);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class NotStartedOrderListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_notstartedorderlist_type)
        TextView tv_notstartedorderlist_type;
        @BindView(R.id.tv_notstartedorderlist_person)
        TextView tv_notstartedorderlist_person;
        @BindView(R.id.tv_notstartedorderlist_time)
        TextView tv_notstartedorderlist_time;
        @BindView(R.id.tv_notstartedorderlist_price)
        TextView tv_notstartedorderlist_price;
        @BindView(R.id.tv_notstartedorderlist_price_type)
        TextView tv_notstartedorderlist_price_type;
        @BindView(R.id.tv_notstartedorderlist_comp)
        TextView tv_notstartedorderlist_comp;
        @BindView(R.id.tv_notstartedorderlist_distance)
        TextView tv_notstartedorderlist_distance;
        @BindView(R.id.notstartedorderlist_logo)
        SimpleDraweeView notstartedorderlist_logo;

        public NotStartedOrderListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
