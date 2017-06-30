package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.base.BaseWebView;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * H5页面Activity
 * Created by Jerry on 2017/4/1.
 */

public class CommonH5Activity extends BaseActivity implements View.OnClickListener {

    private String url;
    private String title = "";
    private boolean isShowIcon;
    private String nextTitle;
    private String nextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        if(intent != null){
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            isShowIcon = intent.getBooleanExtra("isShowIcon", false);
            if(isShowIcon){
                nextTitle = intent.getStringExtra("nextTitle");
                nextUrl = intent.getStringExtra("nextUrl");
            }
        }
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.about_title);
        titleView.setTitle(title);
        titleView.getBackIV().setOnClickListener(this);

        if(isShowIcon){
            titleView.getIconTV().setVisibility(View.VISIBLE);
            titleView.getIconTV().setOnClickListener(this);
        }

        BaseWebView webView = (BaseWebView) findViewById(R.id.about_webview);
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.app_title_icon:
                if(isShowIcon && !TextUtils.isEmpty(nextTitle) && !TextUtils.isEmpty(nextUrl)){
                    Bundle b2 = new Bundle();
                    b2.putString("url", nextUrl);
                    b2.putString("title", nextTitle);
                    IntentUtils.startActivity(CommonH5Activity.this, CommonH5Activity.class, b2);
                }
                break;
            default:
                break;
        }
    }
}
