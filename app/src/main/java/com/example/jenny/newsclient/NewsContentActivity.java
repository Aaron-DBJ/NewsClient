package com.example.jenny.newsclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NewsContentActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        String detailUrl = intent.getStringExtra("url");
        WebView webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setUseWideViewPort(true); //自适应屏幕 可以任意比例缩放
        webSettings.setLoadWithOverviewMode(true);//设置网页是否支持概览模式
        webSettings.setBuiltInZoomControls(true); //设置缩放按钮
        webSettings.setSupportZoom(true); //使页面支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不支持缓存
        webView.loadUrl(detailUrl);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override //返回false则使用当前的WebView加载链接
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//
//            @Override //页面打开时执行操作,显示一个进度条
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if(pd==null){
//                    pd = new ProgressDialog(NewsContentActivity.this); //创建进度条
//                }
//                pd.setMessage("正在载入，请稍后..."); //提示信息
//                pd.show(); //显示进度条
//
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override //页面关闭时执行操作
//            public void onPageFinished(WebView view, String url) {
//                if(pd!=null){
//                    pd.cancel();//进度条取消显示
//                }
//                super.onPageFinished(view, url);
//            }
//        });

    }
}
