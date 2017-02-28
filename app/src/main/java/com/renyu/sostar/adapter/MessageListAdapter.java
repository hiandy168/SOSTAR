package com.renyu.sostar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renyu.sostar.R;
import com.renyu.sostar.activity.message.MessageListActivity;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/2/28.
 */

public class MessageListAdapter extends SwipeMenuAdapter<MessageListAdapter.MessageViewHolder> {

    Context context;
    ArrayList<String> beans;

    public MessageListAdapter(Context context, ArrayList<String> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_messagelist, parent, false);
        return view;
    }

    @Override
    public MessageViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MessageViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.tv_adapter_messagelist_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "我是第" + holder.getLayoutPosition() + "条。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
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

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
