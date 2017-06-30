package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.home.view.ExchangeableListView;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 可兑换Activity
 * Created by Jerry on 2017/3/31.
 */

public class ExchangeableActivity extends BaseActivity implements View.OnClickListener {

    private LRecyclerView productRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchangeable_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.exchangeable_title_bar);
        titleView.setTitle(getResources().getString(R.string.i_can_exchange));
        titleView.getBackIV().setOnClickListener(this);
        productRV = (LRecyclerView) findViewById(R.id.exchangeable_recyclerview);

        ExchangeableListView exchangeableListView = new ExchangeableListView(productRV, ExchangeableActivity.this);
        exchangeableListView.forceRefresh();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

}
