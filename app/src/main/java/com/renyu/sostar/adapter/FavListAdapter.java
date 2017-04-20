package com.renyu.sostar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.settings.FavListActivity;
import com.renyu.sostar.bean.FavListResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/29.
 */

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavListHolder> {

    Context context;
    ArrayList<FavListResponse> beans;

    public FavListAdapter(Context context, ArrayList<FavListResponse> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public FavListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_favlist, parent, false);
        return new FavListHolder(view);
    }

    @Override
    public void onBindViewHolder(FavListHolder holder, int position) {
        holder.tv_adapter_favlist_name.setText(beans.get(position).getNickName());
        holder.tv_adapter_favlist_desp.setText("评价等级 "+beans.get(position).getStar());
        DraweeController draweeController;
        if (!TextUtils.isEmpty(beans.get(position).getPicPath())) {
            draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(beans.get(position).getPicPath())).setAutoPlayAnimations(true).build();
        }
        else {
            draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse("res:///"+R.mipmap.ic_avatar_small)).setAutoPlayAnimations(true).build();
        }
        holder.iv_adapter_favlist_avatar.setController(draweeController);
        holder.tv_adapter_favlist_delete.setOnClickListener(v -> ((FavListActivity) context).deleteFav(beans.get(position).getUserId()));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class FavListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_adapter_favlist_avatar)
        SimpleDraweeView iv_adapter_favlist_avatar;
        @BindView(R.id.tv_adapter_favlist_name)
        TextView tv_adapter_favlist_name;
        @BindView(R.id.tv_adapter_favlist_desp)
        TextView tv_adapter_favlist_desp;
        @BindView(R.id.tv_adapter_favlist_delete)
        Button tv_adapter_favlist_delete;

        public FavListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
