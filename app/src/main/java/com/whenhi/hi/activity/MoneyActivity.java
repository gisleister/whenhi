package com.whenhi.hi.activity;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.whenhi.hi.R;
import com.whenhi.hi.view.web.ProgressWebView;


public class MoneyActivity extends BaseActivity {

    private ProgressWebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        textView.setText("如何提现");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.fanhui);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        setUrl("http://www.whenhi.cn/static/cash/instruction.html?random"+Math.random());
        initWebView();

    }

    public void setUrl(String url){
        mUrl = url;
    }

    public void initWebView() {
        mWebView = (ProgressWebView) findViewById(R.id.money_webview);
        if (!TextUtils.isEmpty(mUrl)) {
            WebSettings webSettings = mWebView.getSettings();
            //设置WebView属性，能够执行Javascript脚本
            webSettings.setJavaScriptEnabled(true);
            //设置可以访问文件
            //webSettings.setAllowFileAccess(true);
            //设置支持缩放
            webSettings.setBuiltInZoomControls(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setUseWideViewPort(true); //打开页面时， 自适应屏幕
            webSettings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.loadUrl(mUrl);

            //设置Web视图
            //mWebView.setWebViewClient(new MyWebViewClient());
            //mWebView.setWebChromeClient(new MyWebChromeClient());
            //new MyAsnycTask().execute();
        }
    }

    private class MyAsnycTask extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... urls) {
            String data="";
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            //mWebView.loadDataWithBaseURL (null, data, "text/html", "utf-8",null);
            //加载需要显示的网页
            mWebView.loadUrl(mUrl);
        }
    }


    // 监听
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
            //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (request.getUrl().toString().contains("sina.cn")){
                    view.loadUrl("http://ask.csdn.net/questions/178242");
                    return true;
                }
            }*/

            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress != 100){

            }
            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.clearFormData();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();

    }
}
