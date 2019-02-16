package cn.denua.v2ex.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.widget.CustomWebView;

/**
 * cn.denua.v2ex.ui
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/16
 */
public class WebViewActivity extends BaseActivity {

    private CustomWebView mWebView;

    public static void start(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_web_view);

        mWebView = find(R.id.webView);

        mWebView.setNetworkAvailable(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.destroy();
    }
}
