package com.whenhi.hi.adapter;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whenhi.hi.view.web.ProgressWebView;

/**
 * Created by 王雷 on 2017/2/21.
 */

public class WebviewAdapter {

    private ProgressWebView mWebView;
    private String mUrl;

    public WebviewAdapter(ProgressWebView webView) {
        mWebView = webView;
    }

    public void setUrl(String url){
        mUrl = url;
    }



    public void initWebView() {
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

            return true;
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


    public void destroy(){
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.clearFormData();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }

        //mWebView.destroy();在执行destroy之前 fragment已经处理
    }
}
