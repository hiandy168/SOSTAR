package com.renyu.sostar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.renyu.sostar.params.CommonParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by renyu on 2017/1/5.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        api= WXAPIFactory.createWXAPI(this, CommonParams.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub
        if (arg0.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(arg0.errCode==0) {

            }
            else {

            }
            finish();
        }
    }

}