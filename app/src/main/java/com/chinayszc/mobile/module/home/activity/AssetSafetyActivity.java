package com.chinayszc.mobile.module.home.activity;

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
 * 资产安全Activity
 * Created by Jerry on 2017/4/1.
 */

public class AssetSafetyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_safety_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.asset_safety_title);
        titleView.setTitle(getResources().getString(R.string.asset_safety));
        titleView.getBackIV().setOnClickListener(this);

        BaseWebView webView = (BaseWebView) findViewById(R.id.asset_safety_webview);
        webView.loadUrl("http://m.azhongzhi.com/client/info/zichananquan?pr=/webapp/product?pr=%2Fwebapp");
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
