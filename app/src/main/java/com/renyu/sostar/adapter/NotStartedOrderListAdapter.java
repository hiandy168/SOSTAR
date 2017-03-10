package com.renyu.sostar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.sostar.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/10.
 */

public class NotStartedOrderListAdapter extends RecyclerView.Adapter<NotStartedOrderListAdapter.NotStartedOrderListHolder> {

    Context context;
    ArrayList<String> beans;

    public NotStartedOrderListAdapter(Context context, ArrayList<String> beans) {
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

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class NotStartedOrderListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notstartedorderlist_type)
        TextView notstartedorderlist_type;
        @BindView(R.id.notstartedorderlist_person)
        TextView notstartedorderlist_person;
        @BindView(R.id.notstartedorderlist_time)
        TextView notstartedorderlist_time;
        @BindView(R.id.notstartedorderlist_price)
        TextView notstartedorderlist_price;
        @BindView(R.id.notstartedorderlist_comp)
        TextView notstartedorderlist_comp;
        @BindView(R.id.notstartedorderlist_distance)
        TextView notstartedorderlist_distance;

        public NotStartedOrderListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
