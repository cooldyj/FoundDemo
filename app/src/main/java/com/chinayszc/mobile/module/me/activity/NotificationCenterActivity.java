package com.chinayszc.mobile.module.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 消息中心Activity
 * Created by Jerry on 2017/4/4.
 */

public class NotificationCenterActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_center_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.notification_center_title);
        titleView.setTitle(getResources().getString(R.string.notification_center));
        titleView.getBackIV().setOnClickListener(this);

        FrameLayout couponBtn = (FrameLayout) findViewById(R.id.notification_center_coupon);
        FrameLayout pointBtn = (FrameLayout) findViewById(R.id.notification_center_points);
        FrameLayout othersBtn = (FrameLayout) findViewById(R.id.notification_center_others);

        couponBtn.setOnClickListener(this);
        pointBtn.setOnClickListener(this);
        othersBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.notification_center_coupon:
                Bundle bundle0 = new Bundle();
                bundle0.putInt("type", 1);
                IntentUtils.startActivity(NotificationCenterActivity.this,
                        NotificationListActivity.class, bundle0);
                break;
            case R.id.notification_center_points:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 2);
                IntentUtils.startActivity(NotificationCenterActivity.this,
                        NotificationListActivity.class, bundle1);
                break;
            case R.id.notification_center_others:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type", 3);
                IntentUtils.startActivity(NotificationCenterActivity.this,
                        NotificationListActivity.class, bundle2);
                break;
            default:
                break;
        }
    }
}
