package com.renyu.sostar.alipay;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.renyu.commonlibrary.baseact.BaseActivity;

public class AliH5PayActivity extends BaseActivity {

	private WebView mWebView;
	String url;

	@Override
	public void initParams() {
		url=getIntent().getExtras().getString("url");
	}

	@Override
	public int initViews() {
		return 0;
	}

	@Override
	public void loadData() {

	}

	@Override
	public int setStatusBarColor() {
		return 0;
	}

	@Override
	public int setStatusBarTranslucent() {
		return 1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(getApplicationContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout, params);

		mWebView = new WebView(getApplicationContext());
		params.weight = 1;
		mWebView.setVisibility(View.VISIBLE);
		layout.addView(mWebView, params);

		WebSettings settings = mWebView.getSettings();
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setSupportMultipleWindows(true);
		settings.setJavaScriptEnabled(true);
		settings.setSavePassword(false);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
		settings.setAllowFileAccess(false);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		mWebView.setVerticalScrollbarOverlay(true);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {
			if (!(url.startsWith("http") || url.startsWith("https"))) {
				return true;
			}

			final PayTask task = new PayTask(AliH5PayActivity.this);
			final String ex = task.fetchOrderInfoFromH5PayUrl(url);
			if (!TextUtils.isEmpty(ex)) {
				new Thread(() -> {
                    System.out.println("payTask:::" + ex);
                    final H5PayResultModel result = task.h5Pay(ex, true);
                    if (!TextUtils.isEmpty(result.getReturnUrl())) {
                        AliH5PayActivity.this.runOnUiThread(() -> view.loadUrl(result.getReturnUrl()));
                    }
                }).start();
			} else {
				view.loadUrl(url);
			}
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.removeAllViews();
			try {
				mWebView.destroy();
			} catch (Throwable t) {
			}
			mWebView = null;
		}
	}
}
