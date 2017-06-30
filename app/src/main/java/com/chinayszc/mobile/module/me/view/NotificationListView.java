package com.chinayszc.mobile.module.me.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.CommonH5Activity;
import com.chinayszc.mobile.module.home.activity.FinacialProductDetailActivity;
import com.chinayszc.mobile.module.home.activity.ProductDetailActivity;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.module.me.activity.RegisterActivity;
import com.chinayszc.mobile.module.me.adapter.NotificationListAdapter;
import com.chinayszc.mobile.module.me.model.NotificationModel;
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
 * 通知List封装类
 * Created by Jerry on 2017/4/2.
 */

public class NotificationListView extends BaseListView<NotificationModel> {

    private String noticeClass;
    private int pageSize = 1;         //当前页

    public NotificationListView(LRecyclerView recyclerView, Context context, String noticeClass) {
        super(recyclerView, context);
        this.noticeClass = noticeClass;
    }

    @Override
    public void setAdapter() {
        adapter = new NotificationListAdapter(context);
    }

    /**
     * 获取接口数据
     */
    private void loadData(boolean isLoadMore) {

        if (isLoadMore) {
            pageSize++;
            Logs.i("pageSize---" + pageSize);
        } else {
            pageSize = 1;
            Logs.i("pageSize---" + pageSize);
        }

        OkHttpUtils.post().url(Urls.NOTICE_LIST)
                .addCommonHeaderAndBody()
                .addParams("page", String.valueOf(pageSize))
                .addParams("noticeClass", noticeClass)
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
                                    List<NotificationModel> modelList = ParseJason.convertToList(list.toString(), NotificationModel.class);
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
    }

    @Override
    public void onListItemClick(View view, int position) {
        Logs.i("UnusedListView---" + position);
        List<NotificationModel> list = adapter.getDataList();
        if (list != null && list.size() > position) {
            String type = list.get(position).getMsgType();
            int id = list.get(position).getId();
            switch (type) {
                case "shop":  //积分商城产品
                    Bundle bundle0 = new Bundle();
                    bundle0.putInt("id", id);
                    IntentUtils.startActivity(getActivity(), ProductDetailActivity.class, bundle0);
                    break;
                case "product":  //理财产品
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("proId", id);
                    IntentUtils.startActivity(getActivity(), FinacialProductDetailActivity.class, bundle1);
                    break;
                case "register":  //注册页
                    IntentUtils.startActivity(getActivity(), RegisterActivity.class);
                    break;
                case "notice":  //web活动页
                    Bundle bundle2 = new Bundle();
                    IntentUtils.startActivity(getActivity(), CommonH5Activity.class, bundle2);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onListItemLongClick(View view, int position) {

    }
}
