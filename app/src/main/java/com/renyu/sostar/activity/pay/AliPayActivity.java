package com.renyu.sostar.activity.pay;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.sostar.bean.PayResult;

import java.util.Map;

public class AliPayActivity extends BaseActivity {

	private static final int SDK_PAY_FLAG = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				if (TextUtils.equals(resultStatus, "9000")) {

				} else {

				}
				break;
			}
			default:
				break;
			}
		}
	};

	public void payV2(String orderInfo) {
		Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(AliPayActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@Override
	public void initParams() {

	}

	@Override
	public int initViews() {
		return 0;
	}

	@Override
	public void loadData() {
		payV2("");
	}

	@Override
	public int setStatusBarColor() {
		return 0;
	}

	@Override
	public int setStatusBarTranslucent() {
		return 1;
	}
}
