package com.chinayszc.mobile.module.home.view;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.adapter.ExchangeableAdapter;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseGridView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * 我可兑换页面RecyclerView封装类
 * Created by Jerry on 2017/4/1.
 */

public class ExchangeableListView extends BaseGridView<PointProductModel> {

    private int pageSize = 1;         //当前页

    public ExchangeableListView(LRecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    /**
     * 设置adapter
     */
    @Override
    public void setAdapter() {
        adapter = new ExchangeableAdapter(context);
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

        OkHttpUtils.post().url(Urls.MY_PRODUCT_LIST)
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
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                JSONArray list = response.optJSONArray("list");
                                if (list != null) {
                                    List<PointProductModel> modelList = ParseJason.convertToList(list.toString(), PointProductModel.class);
                                    onUpdate(modelList);
                                } else {
                                    onUpdate(null);
                                }
                            } else {
                                onUpdate(null);
                                Toast.makeText(context, context.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(context, LoginActivity.class);
                                getActivity().finish();
                            }
                        } else {
                            onUpdate(null);
                        }
                    }
                });


    }

    /**
     * 触发下拉刷新
     */
    @Override
    public void onRefreshing() {
        loadData(false);
    }

    /**
     * 触发加载更多
     */
    @Override
    public void onLoadingMore() {
        loadData(true);
    }

    /**
     * 单击条目事件
     */
    @Override
    public void onListItemClick(View view, int position) {

    }

    /**
     * 长按条目事件
     */
    @Override
    public void onListItemLongClick(View view, int position) {

    }
}
