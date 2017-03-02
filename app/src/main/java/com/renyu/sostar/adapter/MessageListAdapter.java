package com.renyu.sostar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renyu.sostar.R;
import com.renyu.sostar.bean.MsgListResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/2/28.
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Object> beans;
    OnMessageCtrolListener onMessageCtrolListener;

    SimpleDateFormat dateFormatTime;

    public MessageListAdapter(Context context, ArrayList<Object> beans, OnMessageCtrolListener onMessageCtrolListener) {
        this.context = context;
        this.beans = beans;
        this.onMessageCtrolListener = onMessageCtrolListener;
        dateFormatTime=new SimpleDateFormat("HH:mm");
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==2) {
            return new MessageTitleViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_messagelist_parent, parent, false));
        }
        else {
            return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_messagelist_child, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(holder.getLayoutPosition())==2) {
            ((MessageTitleViewHolder) holder).tv_adapter_messagelist_day.setText(beans.get(holder.getLayoutPosition()).toString());
        }
        else if (getItemViewType(holder.getLayoutPosition())==1) {
            ((MessageViewHolder) holder).tv_adapter_messagelist_content.setText(((MsgListResponse.DataBean) beans.get(holder.getLayoutPosition())).getMessage());
            Date date=new Date();
            date.setTime(((MsgListResponse.DataBean) beans.get(holder.getLayoutPosition())).getCrtTime());
            ((MessageViewHolder) holder).tv_adapter_messagelist_time.setText(dateFormatTime.format(date));
            ((MessageViewHolder) holder).tv_adapter_messagelist_layout.setOnClickListener(v -> {
                if (onMessageCtrolListener!=null) {
                    onMessageCtrolListener.read(holder.getLayoutPosition());
                }
            });
            ((MessageViewHolder) holder).tv_adapter_messagelist_delete.setOnClickListener(v -> {
                if (onMessageCtrolListener!=null) {
                    onMessageCtrolListener.delete(holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (beans.get(position) instanceof String) {
            return 2;
        }
        return 1;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_adapter_messagelist_type)
        TextView tv_adapter_messagelist_type;
        @BindView(R.id.tv_adapter_messagelist_time)
        TextView tv_adapter_messagelist_time;
        @BindView(R.id.tv_adapter_messagelist_content)
        TextView tv_adapter_messagelist_content;
        @BindView(R.id.tv_adapter_messagelist_layout)
        LinearLayout tv_adapter_messagelist_layout;
        @BindView(R.id.tv_adapter_messagelist_delete)
        Button tv_adapter_messagelist_delete;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MessageTitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_adapter_messagelist_day)
        TextView tv_adapter_messagelist_day;

        public MessageTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMessageCtrolListener {
        void delete(int position);
        void read(int position);
    }
}
