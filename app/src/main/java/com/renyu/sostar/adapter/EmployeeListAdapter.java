package com.renyu.sostar.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.renyu.sostar.R;
import com.renyu.sostar.activity.order.EmployeeListActivity;
import com.renyu.sostar.activity.user.InfoActivity;
import com.renyu.sostar.bean.EmployerStaffListResponse;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/3/17.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyEmployeeListViewHolder> implements StickyRecyclerHeadersAdapter<EmployeeListAdapter.MyEmployeeListHeadViewHolder> {

    Context context;
    ArrayList<EmployerStaffListResponse> beans;

    public EmployeeListAdapter(Context context, ArrayList<EmployerStaffListResponse> beans) {
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
        holder.tv_adapter_employeelist_name.setText(beans.get(position).getNickName());
        holder.tv_adapter_employeelist_desp.setText("评价等级 "+beans.get(position).getEvaluateLevel());
        ImageRequest request;
        if (!TextUtils.isEmpty(beans.get(position).getPicPath())) {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(beans.get(position).getPicPath()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(40), SizeUtils.dp2px(40))).build();
        }
        else {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("res:///"+R.mipmap.ic_avatar))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(40), SizeUtils.dp2px(40))).build();
        }
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request).setAutoPlayAnimations(true).build();
        holder.iv_adapter_employeelist_avatar.setController(draweeController);
        // 未确认
        if (beans.get(position).getStaffStatus().equals("0")) {
            holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper1.setText("接受");
            holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).confirmStaff(beans.get(position).getUserId(), 1));
            holder.tv_adapter_employeelist_oper2.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper2.setText("拒绝");
            holder.tv_adapter_employeelist_oper2.setOnClickListener(v -> ((EmployeeListActivity) context).confirmStaff(beans.get(position).getUserId(), 2));
            holder.v_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
        }
        // 已确认
        else if (beans.get(position).getStaffStatus().equals("1")) {
            holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper1.setText("拒绝");
            holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).confirmStaff(beans.get(position).getUserId(), 2));
            holder.tv_adapter_employeelist_oper2.setVisibility(View.GONE);
            holder.v_adapter_employeelist_oper1.setVisibility(View.GONE);
        }
        // 已完成
        else if (beans.get(position).getStaffStatus().equals("4") ||
                beans.get(position).getStaffStatus().equals("5")) {
            if (beans.get(position).getEvaluateFlg().equals("1") && beans.get(position).getFavFlg().equals("1")) {
                holder.tv_adapter_employeelist_oper1.setVisibility(View.GONE);
                holder.tv_adapter_employeelist_oper2.setVisibility(View.GONE);
                holder.v_adapter_employeelist_oper1.setVisibility(View.GONE);
            }
            else if (beans.get(position).getEvaluateFlg().equals("0") && beans.get(position).getFavFlg().equals("1")) {
                holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
                holder.tv_adapter_employeelist_oper1.setText("评价");
                holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).evaluate(beans.get(position).getUserId(), beans.get(position).getName()));
                holder.tv_adapter_employeelist_oper2.setVisibility(View.GONE);
                holder.v_adapter_employeelist_oper1.setVisibility(View.GONE);
            }
            else if (beans.get(position).getEvaluateFlg().equals("1") && beans.get(position).getFavFlg().equals("0")) {
                holder.tv_adapter_employeelist_oper1.setVisibility(View.GONE);
                holder.tv_adapter_employeelist_oper2.setVisibility(View.VISIBLE);
                holder.tv_adapter_employeelist_oper2.setText("收藏");
                holder.tv_adapter_employeelist_oper2.setOnClickListener(v -> ((EmployeeListActivity) context).collection(beans.get(position).getUserId()));
                holder.v_adapter_employeelist_oper1.setVisibility(View.GONE);
            }
            else {
                holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
                holder.tv_adapter_employeelist_oper1.setText("评价");
                holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).evaluate(beans.get(position).getUserId(), beans.get(position).getNickName()));
                holder.tv_adapter_employeelist_oper2.setVisibility(View.VISIBLE);
                holder.tv_adapter_employeelist_oper2.setText("收藏");
                holder.tv_adapter_employeelist_oper2.setOnClickListener(v -> ((EmployeeListActivity) context).collection(beans.get(position).getUserId()));
                holder.v_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
            }
        }
        // 进行中
        else if (beans.get(position).getStaffStatus().equals("8")) {
            holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper1.setText("解雇");
            holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).fireStaff(beans.get(position).getUserId()));
            holder.tv_adapter_employeelist_oper2.setVisibility(View.GONE);
            holder.v_adapter_employeelist_oper1.setVisibility(View.GONE);
        }
        // 申请离职
        else if (beans.get(position).getStaffStatus().equals("11")) {
            holder.tv_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper1.setText("确认");
            holder.tv_adapter_employeelist_oper1.setOnClickListener(v -> ((EmployeeListActivity) context).comfirmResignation(beans.get(position).getUserId()));
            holder.tv_adapter_employeelist_oper2.setVisibility(View.VISIBLE);
            holder.tv_adapter_employeelist_oper2.setText("拒绝");
            holder.tv_adapter_employeelist_oper2.setOnClickListener(v -> ((EmployeeListActivity) context).refuseResignation(beans.get(position).getUserId()));
            holder.v_adapter_employeelist_oper1.setVisibility(View.VISIBLE);
        }
        holder.layout_adapter_employeelist.setOnClickListener(v -> {
            Intent intent=new Intent(context, InfoActivity.class);
            intent.putExtra("userId", beans.get(position).getUserId());
            // 进行中的订单可以查看到敏感信息
            if (beans.get(position).getStaffStatus().equals("1") ||
                    beans.get(position).getStaffStatus().equals("8") ||
                    beans.get(position).getStaffStatus().equals("11")) {
                intent.putExtra("canphone", true);
            }
            else {
                intent.putExtra("canphone", false);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public long getHeaderId(int position) {
        if (beans.get(position).getStaffStatus().equals("4") ||
                beans.get(position).getStaffStatus().equals("5")) {
            return Integer.parseInt(beans.get(position).getEvaluateFlg());
        }
        return Integer.parseInt(beans.get(position).getStaffStatus());
    }

    @Override
    public MyEmployeeListHeadViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.header_employeelist, parent, false);
        return new MyEmployeeListHeadViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(MyEmployeeListHeadViewHolder holder, int position) {
        if (beans.get(position).getStaffStatus().equals("0")) {
            holder.tv_adapter_employeelist_head.setText("未确认");
            holder.tv_adapter_employeelist_choice.setVisibility(View.VISIBLE);
        }
        else if (beans.get(position).getStaffStatus().equals("1")) {
            holder.tv_adapter_employeelist_head.setText("已确认");
            holder.tv_adapter_employeelist_choice.setVisibility(View.VISIBLE);
        }
        else if (beans.get(position).getStaffStatus().equals("4") ||
                beans.get(position).getStaffStatus().equals("5")) {
            if (beans.get(position).getEvaluateFlg().equals("1")) {
                holder.tv_adapter_employeelist_head.setText("已评价");
                holder.tv_adapter_employeelist_choice.setVisibility(View.GONE);
            }
            else if (beans.get(position).getEvaluateFlg().equals("0")) {
                holder.tv_adapter_employeelist_head.setText("未评价");
                holder.tv_adapter_employeelist_choice.setVisibility(View.VISIBLE);
            }
        }
        else if (beans.get(position).getStaffStatus().equals("8")) {
            holder.tv_adapter_employeelist_head.setText("已确认");
            holder.tv_adapter_employeelist_choice.setVisibility(View.VISIBLE);
        }
        else if (beans.get(position).getStaffStatus().equals("11")) {
            holder.tv_adapter_employeelist_head.setText("申请离职");
            holder.tv_adapter_employeelist_choice.setVisibility(View.VISIBLE);
        }
        holder.tv_adapter_employeelist_choice.setOnClickListener(v -> {


        });
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
        @BindView(R.id.tv_adapter_employeelist_oper1)
        TextView tv_adapter_employeelist_oper1;
        @BindView(R.id.tv_adapter_employeelist_oper2)
        TextView tv_adapter_employeelist_oper2;
        @BindView(R.id.v_adapter_employeelist_oper1)
        View v_adapter_employeelist_oper1;

        public MyEmployeeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyEmployeeListHeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_adapter_employeelist_head)
        TextView tv_adapter_employeelist_head;
        @BindView(R.id.tv_adapter_employeelist_choice)
        TextView tv_adapter_employeelist_choice;

        public MyEmployeeListHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
