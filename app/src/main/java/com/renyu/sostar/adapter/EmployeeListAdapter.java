package com.renyu.sostar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.renyu.sostar.R;
import com.renyu.sostar.bean.EmployeeListResponse;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/17.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyEmployeeListViewHolder> implements StickyRecyclerHeadersAdapter<EmployeeListAdapter.MyEmployeeListHeadViewHolder> {

    Context context;
    ArrayList<EmployeeListResponse> beans;

    public EmployeeListAdapter(Context context, ArrayList<EmployeeListResponse> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public MyEmployeeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_employeelist, parent, false);
        return new MyEmployeeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyEmployeeListViewHolder holder, int position) {
        holder.tv_adapter_employeelist_name.setText(beans.get(position).getName());
        if (position%2==0) {
            holder.swipelayout_adapter_employeelist.setSwipeEnable(true);
        }
        else {
            holder.swipelayout_adapter_employeelist.setSwipeEnable(false);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return beans.get(position).getId();
    }

    @Override
    public MyEmployeeListHeadViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.header_employeelist, parent, false);
        return new MyEmployeeListHeadViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(MyEmployeeListHeadViewHolder holder, int position) {
        holder.tv_adapter_employeelist_head.setText(beans.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class MyEmployeeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_adapter_employeelist)
        LinearLayout layout_adapter_employeelist;
        @BindView(R.id.iv_adapter_employeelist_avatar)
        SimpleDraweeView iv_adapter_employeelist_avatar;
        @BindView(R.id.tv_adapter_employeelist_name)
        TextView tv_adapter_employeelist_name;
        @BindView(R.id.tv_adapter_employeelist_desp)
        TextView tv_adapter_employeelist_desp;
        @BindView(R.id.tv_adapter_employeelist_delete)
        Button tv_adapter_employeelist_delete;
        @BindView(R.id.swipelayout_adapter_employeelist)
        SwipeMenuLayout swipelayout_adapter_employeelist;

        public MyEmployeeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyEmployeeListHeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_adapter_employeelist_head)
        TextView tv_adapter_employeelist_head;

        public MyEmployeeListHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
