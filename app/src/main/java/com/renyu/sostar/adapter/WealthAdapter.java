package com.renyu.sostar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.sostar.R;
import com.renyu.sostar.bean.FlowResponse;
import com.renyu.sostar.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/4/10.
 */

public class WealthAdapter extends RecyclerView.Adapter {

    public static final int TIME=0;
    public static final int INFO=1;
    public static final int EMPTY=2;

    Context context;
    List<Object> beans;

    public WealthAdapter(Context context, List<Object> beans) {
        this.context = context;
        this.beans = beans;
    }

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
        if (viewType==EMPTY) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_wealth_time, parent, false);
            return new WealthTimeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==EMPTY) {
            ((WealthTimeViewHolder) holder).tv_wealth_time.setVisibility(View.GONE);
            ((WealthTimeViewHolder) holder).tv_wealth_empty.setVisibility(View.VISIBLE);
        }
        else if (getItemViewType(position)==TIME) {
            ((WealthTimeViewHolder) holder).tv_wealth_empty.setVisibility(View.GONE);
            ((WealthTimeViewHolder) holder).tv_wealth_time.setVisibility(View.VISIBLE);
            ((WealthTimeViewHolder) holder).tv_wealth_time.setText(beans.get(position).toString());
        }
        else if (getItemViewType(position)==INFO) {
            ((WealthInfoViewHolder) holder).tv_wealth_info_time.setText(((FlowResponse) beans.get(position)).getDate());
            if (((FlowResponse) beans.get(position)).getType().equals("1") ||
                    ((FlowResponse) beans.get(position)).getType().equals("3") ||
                    ((FlowResponse) beans.get(position)).getType().equals("5")) {
                ((WealthInfoViewHolder) holder).tv_wealth_info_addmoney
                        .setText("+"+ Utils.removeZero(((FlowResponse) beans.get(position)).getCashTotal()));
                ((WealthInfoViewHolder) holder).tv_wealth_info_addmoney.setTextColor(Color.parseColor("#33acde"));
            }
            else {
                ((WealthInfoViewHolder) holder).tv_wealth_info_addmoney
                        .setText("-"+Utils.removeZero(((FlowResponse) beans.get(position)).getCashTotal()));
                ((WealthInfoViewHolder) holder).tv_wealth_info_addmoney.setTextColor(Color.parseColor("#999999"));
            }
            ((WealthInfoViewHolder) holder).tv_wealth_info_lastmoney
                    .setText(Utils.removeZero(((FlowResponse) beans.get(position)).getCashAmount()));
            ((WealthInfoViewHolder) holder).tv_wealth_info_text.setText(((FlowResponse) beans.get(position)).getDescri());
            // 最后一条
            if (position==beans.size()-1) {
                ((WealthInfoViewHolder) holder).view_wealth_info.setVisibility(View.GONE);
            }
            else {
                if (beans.get(position) instanceof String) {
                    ((WealthInfoViewHolder) holder).view_wealth_info.setVisibility(View.GONE);
                }
                else if (beans.get(position) instanceof FlowResponse) {
                    if (beans.get(position+1) instanceof FlowResponse) {
                        ((WealthInfoViewHolder) holder).view_wealth_info.setVisibility(View.VISIBLE);
                    }
                    else {
                        ((WealthInfoViewHolder) holder).view_wealth_info.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (beans.get(position) instanceof String) {
            if (beans.get(position).toString().equals("")) {
                return EMPTY;
            }
            else {
                return TIME;
            }
        }
        else if (beans.get(position) instanceof FlowResponse) {
            return INFO;
        }
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
        View view_wealth_info;

        public WealthInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
