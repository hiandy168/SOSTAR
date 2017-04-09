package com.renyu.sostar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.NotStartedOrderListHolder> {

    Context context;
    ArrayList<MyOrderListResponse.DataBean> beans;
    OnClickListener onClickListener;

    public interface OnClickListener {
        void click(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OrderListAdapter(Context context, ArrayList<MyOrderListResponse.DataBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public NotStartedOrderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_orderlist, parent, false);
        return new NotStartedOrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(NotStartedOrderListHolder holder, int position) {
        holder.tv_orderlist_type.setText(beans.get(position).getJobType());
        holder.tv_orderlist_person.setText(""+beans.get(position).getStaffAccount()+"人");
        String times="";
        String[] timeTemps=beans.get(position).getPeriodTime().split(",");
        for (String timeTemp : timeTemps) {
            times+=timeTemp+"  "+beans.get(position).getStartTime()+"-"+beans.get(position).getEndTime()+"\n";
        }
        times=times.substring(0, times.length()-1);
        holder.tv_orderlist_time.setText(times);
        holder.tv_orderlist_price.setText(""+beans.get(position).getUnitPrice());
        if (beans.get(position).getPaymentType().equals("1")) {
            holder.tv_orderlist_price_type.setText("/天");
        }
        else if (beans.get(position).getPaymentType().equals("2")) {
            holder.tv_orderlist_price_type.setText("/小时");
        }
        holder.tv_orderlist_comp.setText(beans.get(position).getCompanyName());
        if (LocationService.lastBdLocation==null || LocationService.lastBdLocation==null) {
            holder.tv_orderlist_distance.setVisibility(View.GONE);
        }
        else {
            LatLng userLatlng=new LatLng(LocationService.lastBdLocation.getLatitude(), LocationService.lastBdLocation.getLongitude());
            LatLng orderLatlng=new LatLng(Double.parseDouble(beans.get(position).getLatitude()), Double.parseDouble(beans.get(position).getLongitude()));
            if (Double.parseDouble(beans.get(position).getLatitude())<1 || Double.parseDouble(beans.get(position).getLongitude())<1) {
                holder.tv_orderlist_distance.setVisibility(View.GONE);
            }
            else {
                holder.tv_orderlist_distance.setVisibility(View.VISIBLE);
                holder.tv_orderlist_distance.setText(((int) DistanceUtil.getDistance(userLatlng, orderLatlng))+"m");
            }
        }
        if (!TextUtils.isEmpty(beans.get(position).getLogoPath())) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(beans.get(position).getLogoPath())).setAutoPlayAnimations(true).build();
            holder.iv_orderlist_logo.setController(draweeController);
        }
        holder.layout_orderlist_root.setOnClickListener(v -> {
            if (onClickListener!=null) {
                onClickListener.click(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class NotStartedOrderListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_orderlist_root)
        LinearLayout layout_orderlist_root;
        @BindView(R.id.tv_orderlist_type)
        TextView tv_orderlist_type;
        @BindView(R.id.tv_orderlist_person)
        TextView tv_orderlist_person;
        @BindView(R.id.tv_orderlist_time)
        TextView tv_orderlist_time;
        @BindView(R.id.tv_orderlist_price)
        TextView tv_orderlist_price;
        @BindView(R.id.tv_orderlist_price_type)
        TextView tv_orderlist_price_type;
        @BindView(R.id.tv_orderlist_comp)
        TextView tv_orderlist_comp;
        @BindView(R.id.tv_orderlist_distance)
        TextView tv_orderlist_distance;
        @BindView(R.id.iv_orderlist_logo)
        SimpleDraweeView iv_orderlist_logo;

        public NotStartedOrderListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
