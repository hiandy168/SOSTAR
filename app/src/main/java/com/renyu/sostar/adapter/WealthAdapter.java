package com.renyu.sostar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.sostar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/4/10.
 */

public class WealthAdapter extends RecyclerView.Adapter {

    private static final int TIME=0;
    private static final int INFO=1;

    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TIME) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_wealth_time, parent, false);
            return new WealthTimeViewHolder(view);
        }
        if (viewType==INFO) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_wealth_info, parent, false);
            return new WealthInfoViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class WealthTimeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_wealth_time)
        TextView tv_wealth_time;
        @BindView(R.id.tv_wealth_empty)
        TextView tv_wealth_empty;

        public WealthTimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class WealthInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_wealth_info_time)
        TextView tv_wealth_info_time;
        @BindView(R.id.tv_wealth_info_text)
        TextView tv_wealth_info_text;
        @BindView(R.id.tv_wealth_info_addmoney)
        TextView tv_wealth_info_addmoney;
        @BindView(R.id.tv_wealth_info_lastmoney)
        TextView tv_wealth_info_lastmoney;
        @BindView(R.id.view_wealth_info)
        TextView view_wealth_info;

        public WealthInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
