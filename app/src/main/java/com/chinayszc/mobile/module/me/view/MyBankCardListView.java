package com.chinayszc.mobile.module.me.view;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.me.adapter.MyBankCardAdapter;
import com.chinayszc.mobile.module.me.adapter.NotificationListAdapter;
import com.chinayszc.mobile.module.me.model.MyBankCardModel;
import com.chinayszc.mobile.module.me.model.NotificationModel;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

import java.util.List;

/**
 * 我的银行卡List封装类
 * Created by Jerry on 2017/4/2.
 */

public class MyBankCardListView extends BaseListView<MyBankCardModel> {

    public MyBankCardListView(LRecyclerView recyclerView, Context context) {
        super(recyclerView, context);
        loadData();
    }

    @Override
    public void setAdapter() {
        adapter = new MyBankCardAdapter(context);
    }

    /**
     * 获取接口数据
     */
    private void loadData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String dummydata = "[ { \"bankCardImg\": \"http://www.ujushou.com/images/tmp/b_zhaoshang.jpg\", \"bankCardName\": \"招商银行\", \"bankCardNum\": \"6214 **** **** 2211\" }, { \"bankCardImg\": \"http://www.ujushou.com/images/tmp/b_gongshang.jpg\", \"bankCardName\": \"工商银行\", \"bankCardNum\": \"6228 **** **** 3456\" }, { \"bankCardImg\": \"http://www.ujushou.com/images/tmp/b_nongye.jpg\", \"bankCardName\": \"农业银行\", \"bankCardNum\": \"5214 **** **** 2341\" } ]";
                List<MyBankCardModel> modelList = ParseJason.convertToList(dummydata, MyBankCardModel.class);
                if(null != modelList){
                    onUpdate(modelList);
                }
            }
        }, 10);

    }

    @Override
    public void onRefreshing() {
        loadData();
    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public void onListItemClick(View view, int position) {
        Logs.i("MyBankCardListView---" + position);
    }

    @Override
    public void onListItemLongClick(View view, int position) {

    }
}
