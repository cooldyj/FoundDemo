package com.chinayszc.mobile.module.assets.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.adapter.EarningListAdapter;
import com.chinayszc.mobile.module.assets.model.EarningModel;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * 收益记录List封装类
 * Created by Jerry on 2017/4/2.
 */

public class EarningListView extends BaseListView<EarningModel> {

    private int pageSize = 1;         //当前页

    public EarningListView(LRecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
    public void setAdapter() {
        adapter = new EarningListAdapter(context);
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

        OkHttpUtils.post().url(Urls.EARNING_LIST)
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
//                                if (list != null) {
                                    List<EarningModel> modelList = ParseJason.convertToList(list.toString(), EarningModel.class);
                                    onUpdate(modelList);
//                                }
                            } else {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
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
        Logs.i("onLoadingMore---");
        loadData(true);
    }

    @Override
    public void onListItemClick(View view, int position) {
        Logs.i("Earning---" + position);
    }

    @Override
    public void onListItemLongClick(View view, int position) {

    }
}
