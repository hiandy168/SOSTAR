package com.renyu.sostar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.order.ReleaseOrderActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/29.
 */

public class SearchPoiAdapter extends RecyclerView.Adapter<SearchPoiAdapter.SearchPoiViewHolder> {

    Context context;
    ArrayList<PoiInfo> beans;

    public SearchPoiAdapter(Context context, ArrayList<PoiInfo> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public SearchPoiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_searchpoi, parent, false);
        return new SearchPoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchPoiViewHolder holder, int position) {
        holder.tv_search_poi_title.setText(beans.get(position).name);
        holder.tv_search_poi_desp.setText(beans.get(position).address);
        holder.layout_search_poi.setOnClickListener(v -> {
            Intent intent=new Intent(context, ReleaseOrderActivity.class);
            intent.putExtra("address", beans.get(position).name);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class SearchPoiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_search_poi_title)
        TextView tv_search_poi_title;
        @BindView(R.id.tv_search_poi_desp)
        TextView tv_search_poi_desp;
        @BindView(R.id.layout_search_poi)
        LinearLayout layout_search_poi;

        public SearchPoiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
