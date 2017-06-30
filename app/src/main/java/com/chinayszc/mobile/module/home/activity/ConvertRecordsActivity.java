package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.home.view.ConvertListView;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 兑换记录Activity
 * Created by Jerry on 2017/4/1.
 */

public class ConvertRecordsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_record_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.convert_record_title);
        titleView.setTitle(getResources().getString(R.string.exchange_records));
        titleView.getBackIV().setOnClickListener(this);
        LRecyclerView productRV = (LRecyclerView) findViewById(R.id.convert_record_recyclerview);

        ConvertListView convertListView = new ConvertListView(productRV, ConvertRecordsActivity.this);
        convertListView.forceRefresh();
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
