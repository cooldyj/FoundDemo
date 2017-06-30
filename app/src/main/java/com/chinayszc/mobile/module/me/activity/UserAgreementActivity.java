package com.chinayszc.mobile.module.me.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.base.BaseWebView;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 用户服务协议Activity
 * Created by Jerry on 2017/4/17.
 */

public class UserAgreementActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agreement_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.agreement_title);
        titleView.setTitle(getResources().getString(R.string.user_agreement));
        titleView.getBackIV().setOnClickListener(this);

        BaseWebView webView = (BaseWebView) findViewById(R.id.agreement_webview);
        webView.loadUrl("http://m.azhongzhi.com/client/info/yonghuxieyi.htm");
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
            default:
                break;
        }
    }

}
