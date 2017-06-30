package com.chinayszc.mobile.module.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.home.activity.AboutActivity;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.PhoneUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 二级关于我们
 * Created by Jerry Yang on 2017/6/20.
 */

public class SecondAboutActivity extends BaseActivity implements View.OnClickListener{

    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_about_activity);
        Intent data = getIntent();
        if(data != null){
            phoneNum = data.getStringExtra("phoneNum");
        }
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.second_about_title);
        titleView.setTitle(getResources().getString(R.string.about_ys));
        titleView.getBackIV().setOnClickListener(this);

        FrameLayout aboutFL = (FrameLayout) findViewById(R.id.me_about_yisheng);
        FrameLayout feedbackFL = (FrameLayout) findViewById(R.id.me_feed_back);
        TextView phoneTV = (TextView) findViewById(R.id.me_phone);
        TextView versionTV = (TextView) findViewById(R.id.me_version);

        aboutFL.setOnClickListener(this);
        feedbackFL.setOnClickListener(this);
        phoneTV.setOnClickListener(this);
        versionTV.setOnClickListener(this);

        if (!TextUtils.isEmpty(phoneNum)) {
            String phoneText = getResources().getString(R.string.client_phone);
            phoneTV.setText(phoneText + phoneNum);
        }

        String versionName = getResources().getString(R.string.version) + Env.versionName;
		versionTV.setText(versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.me_about_yisheng:
                IntentUtils.startActivity(SecondAboutActivity.this, AboutActivity.class);
                break;
            case R.id.me_feed_back:
                IntentUtils.startActivity(SecondAboutActivity.this, FeedbackActivity.class);
                break;
            case R.id.me_version:
                break;
            case R.id.me_phone:
                if (!TextUtils.isEmpty(phoneNum)) {
                    PhoneUtils.dial(SecondAboutActivity.this, phoneNum);
                }
                break;
            default:
                break;
        }
    }

}
