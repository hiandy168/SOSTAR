package com.renyu.sostar.activity.message;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.utils.SizeUtils;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.ListViewDecoration;
import com.renyu.sostar.R;
import com.renyu.sostar.adapter.MessageListAdapter;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by renyu on 2017/2/28.
 */

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.rv_messagelist)
    SwipeMenuRecyclerView rv_messagelist;
    MessageListAdapter adapter;

    ArrayList<String> beans;

    @Override
    public void initParams() {
        beans=new ArrayList<>();
        for (int i=0;i<20;i++) {
            beans.add(""+i);
        }

        rv_messagelist.setHasFixedSize(true);
        rv_messagelist.setLayoutManager(new LinearLayoutManager(this));
        rv_messagelist.setItemAnimator(new DefaultItemAnimator());
        rv_messagelist.addItemDecoration(new ListViewDecoration(getApplicationContext()));
        // 设置菜单创建器。
        rv_messagelist.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        rv_messagelist.setSwipeMenuItemClickListener(menuItemClickListener);
        adapter=new MessageListAdapter(this, beans);
        rv_messagelist.setAdapter(adapter);
    }

    @Override
    public int initViews() {
        return R.layout.activity_messagelist;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        int width = SizeUtils.dp2px(60);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 添加左侧的，如果不添加，则左侧不会出现菜单。
        {

        }

        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(MessageListActivity.this)
                    .setBackgroundColor(Color.WHITE)
                    .setText("删除")
                    .setTextColor(Color.RED)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            // 关闭被点击的菜单。
            closeable.smoothCloseMenu();
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(MessageListActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(MessageListActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
            // 删除按钮被点击。
            if (menuPosition == 0) {
                beans.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);
            }
        }
    };
}
