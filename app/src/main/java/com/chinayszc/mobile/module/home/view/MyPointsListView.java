package com.chinayszc.mobile.module.home.view;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.MyPointsActivity;
import com.chinayszc.mobile.module.home.adapter.MyPointsAdapter;
import com.chinayszc.mobile.module.home.model.MyPointsModel;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * 我的积分RecyclerView封装类
 * Created by Jerry on 2017/4/1.
 */

public class MyPointsListView extends BaseListView<MyPointsModel> {

    private int pageSize = 1;         //当前页

    public MyPointsListView(LRecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
    public void setAdapter() {
        adapter = new MyPointsAdapter(context);
    }

    /**
     * 获取接口数据
     */
    private void loadData(boolean isLoadMore) {

        if(isLoadMore){
            pageSize++;
            Logs.i("pageSize---" + pageSize);
        }else {
            pageSize = 1;
            Logs.i("pageSize---" + pageSize);
        }

        OkHttpUtils.post().url(Urls.SHOP_CREDIT_LIST)
                .addCommonHeaderAndBody()
                .addParams("page", String.valueOf(pageSize))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("onResponse---" + response);
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                JSONArray list = response.optJSONArray("list");
                                if(list != null){
                                    List<MyPointsModel> modelList = ParseJason.convertToList(list.toString(), MyPointsModel.class);
                                    if(null != modelList){
                                        onUpdate(modelList);
                                    }
                                }
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(context, LoginActivity.class);
                                getActivity().finish();
                            }
                        }
                    }
                });

    }

    @Override
    public void addHeaderView() {
        MyPointsActivity myPointsActivity = (MyPointsActivity)getActivity();
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        View header = LayoutInflater.from(context).inflate(R.layout.my_points_header, ll, true);
        TextView recordNum = (TextView) header.findViewById(R.id.my_point_record_num);
        recordNum.setText(myPointsActivity.getPoints());
        super.headerView = header;
    }

    @Override
    public void onRefreshing() {
        loadData(false);
    }

    @Override
    public void onLoadingMore() {
        loadData(true);
    }

    @Override
    public void onListItemClick(View view, int position) {

    }

    @Override
    public void onListItemLongClick(View view, int position) {

    }

}
