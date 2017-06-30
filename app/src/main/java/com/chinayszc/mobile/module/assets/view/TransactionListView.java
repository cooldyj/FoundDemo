package com.chinayszc.mobile.module.assets.view;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.adapter.TransactionListAdapter;
import com.chinayszc.mobile.module.assets.model.TransactionModel;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.common.Urls;
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
 * 交易记录List封装类
 * Created by Jerry on 2017/4/2.
 */

public class TransactionListView extends BaseListView<TransactionModel> {
    private int pageSize = 1;         //当前页

    public TransactionListView(LRecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
    public void setAdapter() {
        adapter = new TransactionListAdapter(context);
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

        OkHttpUtils.post().url(Urls.ORDER_LIST)
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
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                JSONArray list = response.optJSONArray("list");
                                if (list != null) {
                                    List<TransactionModel> modelList = ParseJason.convertToList(list.toString(), TransactionModel.class);
                                    onUpdate(modelList);
                                }
                            } else {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(getActivity(), LoginActivity.class);
                                getActivity().finish();
                            }
                        }
                    }
                });
    }

    @Override
    public void onRefreshing() {
        loadData(false);
    }

    @Override
    public void onLoadingMore() {
        loadData(true);
        Logs.i("onLoadingMore---");
    }

    @Override
    public void onListItemClick(View view, int position) {
        Logs.i("TransactionView---" + position);
    }

    @Override
    public void onListItemLongClick(View view, int position) {

    }
}
