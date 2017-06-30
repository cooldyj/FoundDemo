package com.chinayszc.mobile.module.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.me.view.NotificationListView;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 优惠券,积分，其他通知页
 * Created by Jerry on 2017/4/4.
 */

public class NotificationListActivity extends BaseActivity implements View.OnClickListener {

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
        getData();
        initView();
    }

    private void getData(){
        Intent data = getIntent();
        if(null != data){
            type = data.getIntExtra("type", 1);
        }
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.notification_title);
        if(type == 1){
            titleView.setTitle(getResources().getString(R.string.points_notify));
        }else if(type == 2){
            titleView.setTitle(getResources().getString(R.string.others_notify));
        }else {
            titleView.setTitle(getResources().getString(R.string.coupon_notify));
        }
        titleView.getBackIV().setOnClickListener(this);

        LRecyclerView notificationRV = (LRecyclerView) findViewById(R.id.notification_recycler_view);
        NotificationListView listView = new NotificationListView(notificationRV,
                NotificationListActivity.this, String.valueOf(type));
        listView.forceRefresh();
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
