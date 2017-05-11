package com.renyu.sostar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.renyu.sostar.activity.order.OrderDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by renyu on 2017/3/2.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String extra=intent.getStringExtra("cn.jpush.android.EXTRA");
        try {
            JSONObject jsonObject=new JSONObject(extra);
            // 1:订单推送  2:系统推送
            if (jsonObject.getInt("type")==1) {
                String orderId=jsonObject.getString("orderId");
                Intent intent_=new Intent(context, OrderDetailActivity.class);
                intent_.putExtra("orderId", orderId);
                intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent_);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
