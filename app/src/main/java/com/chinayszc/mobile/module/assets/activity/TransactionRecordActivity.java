package com.chinayszc.mobile.module.assets.activity;

import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.view.TransactionListView;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 交易记录Acti
 * Created by Jerry on 2017/4/3.
 */

public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.transaction_title);
        titleView.setTitle(getResources().getString(R.string.transaction_record));
        titleView.getBackIV().setOnClickListener(this);
        LRecyclerView transactionRV = (LRecyclerView) findViewById(R.id.transaction_recycler_view);

        TransactionListView transactionListView = new TransactionListView(transactionRV, TransactionRecordActivity.this);
        transactionRV.setLoadMoreEnabled(true);
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
