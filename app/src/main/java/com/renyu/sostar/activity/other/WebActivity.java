package com.renyu.sostar.activity.other;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.sostar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by renyu on 16/2/16.
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.web_webview)
    WebView web_webview;

    @Override
    public int setStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParams() {
        web_webview.setSaveEnabled(true);
        web_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return false;
            }
        });
        web_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        web_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings=web_webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        web_webview.loadUrl(getIntent().getExtras().getString("url"));
    }

    @Override
    public int initViews() {
        return R.layout.activity_web;
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.ib_nav_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_nav_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        web_webview.removeAllViews();
        web_webview.destroy();
    }
}
