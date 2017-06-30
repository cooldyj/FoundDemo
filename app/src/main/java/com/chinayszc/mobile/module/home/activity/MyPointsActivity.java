package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.home.view.MyPointsListView;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 我的积分页面
 * Created by Jerry on 2017/4/1.
 */

public class MyPointsActivity extends BaseActivity implements View.OnClickListener {

    private String points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_points_activity);
        getData();
        initView();
    }

    private void getData(){
        Intent intent = getIntent();
        if(intent != null){
            points = intent.getStringExtra("points");
        }
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.my_point_title);
        titleView.setTitle(getResources().getString(R.string.my_points));
        titleView.getBackIV().setOnClickListener(this);
        LRecyclerView pointsRV = (LRecyclerView) findViewById(R.id.my_point_recycler_view);

        MyPointsListView myPointsListView = new MyPointsListView(pointsRV, MyPointsActivity.this);
        myPointsListView.forceRefresh();
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

    public String getPoints() {
        return points;
    }
}
