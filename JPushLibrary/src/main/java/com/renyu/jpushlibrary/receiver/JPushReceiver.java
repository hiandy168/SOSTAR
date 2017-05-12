package com.renyu.jpushlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.renyu.commonlibrary.commonutils.NotificationUtils;
import com.renyu.jpushlibrary.R;
import com.renyu.jpushlibrary.bean.NotificationBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by renyu on 2016/12/30.
 */

public class JPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            printBundle(bundle);

            // 构造点击intent
            Intent intent_no=new Intent("com.renyu.sostarjob.NotificationReceiver");
            intent_no.putExtra("cn.jpush.android.EXTRA", bundle.getString(JPushInterface.EXTRA_EXTRA));
            intent_no.putExtra("cn.jpush.android.MESSAGE", bundle.getString(JPushInterface.EXTRA_MESSAGE));
            int type=-1;
            try {
                JSONObject jsonObject=new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                type=jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type==-1) {
                return;
            }
            else {
                String title="";
                if (type==1) {
                    title="订单提示";
                }
                else if (type==2) {
                    title="系统消息";
                }
                NotificationUtils.getNotificationCenter(context)
                        .createNormalNotification(context, title, title, bundle.getString(JPushInterface.EXTRA_MESSAGE),
                                ContextCompat.getColor(context, R.color.colorPrimary),
                                R.mipmap.ic_launcher, R.mipmap.ic_launcher, intent_no);
            }

            // 将消息传递出去
            NotificationBean notificationBean=new NotificationBean();
            notificationBean.setExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));
            EventBus.getDefault().post(notificationBean);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[JPushReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static void printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        Log.d(TAG, sb.toString());
    }
}
