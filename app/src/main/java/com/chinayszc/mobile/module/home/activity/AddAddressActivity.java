package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 新增地址Activity
 * Created by Jerry on 2017/4/1.
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.add_address_title);
        titleView.setTitle(getResources().getString(R.string.add_address));
        titleView.getBackIV().setOnClickListener(this);

        TextView saveTV = (TextView) findViewById(R.id.add_address_save);

        saveTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.add_address_save:
                attemptAdd();
                break;
            default:
                break;
        }
    }

    private void attemptAdd(){

    }
}
