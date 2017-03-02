package com.renyu.sostar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by renyu on 2017/3/2.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String extra=intent.getStringExtra("cn.jpush.android.EXTRA");

    }
}
