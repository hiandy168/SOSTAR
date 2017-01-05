package com.renyu.sostar.wxapi;

import com.renyu.sostar.params.CommonParams;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final IWXAPI api=WXAPIFactory.createWXAPI(context, CommonParams.WX_APP_ID);

		api.registerApp(CommonParams.WX_APP_ID);
	}

}
