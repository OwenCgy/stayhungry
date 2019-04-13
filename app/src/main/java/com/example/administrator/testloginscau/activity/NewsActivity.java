package com.example.administrator.testloginscau.activity;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.example.administrator.testloginscau.R;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String url = "http://www.jlmu.edu.cn/";

    private WebView webview;
    private ProgressBar pg;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        webview = (WebView) findViewById(R.id.webView);
        pg=(ProgressBar) findViewById(R.id.progressBar);

        //获取WebSettings对象
        WebSettings settings = webview.getSettings();

        webview.loadUrl(url);

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                if(newProgress==100){
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
            }}

        );

        //实现在webview内加载网页
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //设置支持js
        settings.setJavaScriptEnabled(true);
        // 支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);/*
        //设置缓存使用方式为优先加载本地缓存
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        //开启数据库存储API功能
        settings.setDatabaseEnabled(true);
        //开启DOM存储API功能
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //构造缓存路径
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache/";

        //设置数据库缓存路径
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(cacheDirPath);
*/

    }
    //设置手机物理返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {//当webview不是处于第一个页面时，返回上一个页面
                webview.goBack();
                return true;
            }


        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:

                actionKey(KeyEvent.KEYCODE_BACK);//调用actionKey方法并传人对应参数
                break;
        }
    }
    //设置自定义返回键动作
    public void actionKey(final int keyCode) {
        new Thread () {
            public void run () {
                try {
                    Instrumentation inst=new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
