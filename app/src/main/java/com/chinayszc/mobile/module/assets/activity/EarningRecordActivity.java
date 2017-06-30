package com.chinayszc.mobile.module.assets.activity;

import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.view.EarningListView;
import com.chinayszc.mobile.module.assets.view.TransactionListView;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 收益记录Acti
 * Created by Jerry on 2017/4/3.
 */

public class EarningRecordActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earning_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.earning_title);
        titleView.setTitle(getResources().getString(R.string.earning_record));
        titleView.getBackIV().setOnClickListener(this);
        LRecyclerView transactionRV = (LRecyclerView) findViewById(R.id.earning_recycler_view);

        EarningListView transactionListView = new EarningListView(transactionRV, EarningRecordActivity.this);
        transactionRV.setLoadMoreEnabled(true);
        transactionListView.setHasMoreData(true);
        transactionListView.forceRefresh();
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
