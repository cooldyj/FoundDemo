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
import com.chinayszc.mobile.module.main.MainNavigationActivity;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * H5页面Activity
 * Created by Jerry on 2017/4/1.
 */

public class BoughtSuccessActivity extends BaseActivity implements View.OnClickListener {

    private String url;
    private String title = "购买成功";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.about_title);
        titleView.setTitle("购买成功");
        titleView.getBackIV().setOnClickListener(this);

        BaseWebView webView = (BaseWebView) findViewById(R.id.about_webview);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Logs.i("url--" + url);
                    try {
                        String str[] = url.split("//");
                        String type = str[1];
                        if (!TextUtils.isEmpty(type)) {
                            if (type.equals("assetHome")) {
                                Bundle b0 = new Bundle();
                                b0.putInt("tab", 2);
                                IntentUtils.startActivity(BoughtSuccessActivity.this, MainNavigationActivity.class, b0);
                            } else if (type.equals("productHome")) {
                                Bundle b1 = new Bundle();
                                b1.putInt("tab", 1);
                                IntentUtils.startActivity(BoughtSuccessActivity.this, MainNavigationActivity.class, b1);
                            } else if (type.equals("shopHome")) {
                                IntentUtils.startActivity(BoughtSuccessActivity.this, PointMallActivity.class);
                            } else if (type.equals("exchange")) {
                                IntentUtils.startActivity(BoughtSuccessActivity.this, ConvertRecordsActivity.class);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
