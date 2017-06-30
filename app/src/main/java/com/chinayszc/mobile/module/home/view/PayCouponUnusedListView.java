package com.chinayszc.mobile.module.home.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListView;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.PayCouponActivity;
import com.chinayszc.mobile.module.home.adapter.PayCouponUnusedListAdapter;
import com.chinayszc.mobile.module.home.adapter.UnusedListAdapter;
import com.chinayszc.mobile.module.home.model.CouponModel;
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
 * 未使用的优惠券List封装类
 * Created by Jerry on 2017/4/2.
 */

public class PayCouponUnusedListView extends BaseListView<CouponModel> {

    private String productId;
    private String amount;
    private int selectedPosition = -1;

    public PayCouponUnusedListView(LRecyclerView recyclerView, Context context, String productId, String amount, int selectedPosition) {
        super(recyclerView, context);
        this.productId = productId;
        this.amount = amount;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public void setAdapter() {
        adapter = new PayCouponUnusedListAdapter(context);
    }

    /**
     * 获取接口数据
     */
    private void loadData() {
        OkHttpUtils.post().url(Urls.GET_COUPON)
                .addCommonHeaderAndBody()
                .addParams("product", productId)
                .addParams("money", amount)
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
                                JSONArray list = response.optJSONArray("valid");
                                if (list != null) {
                                    List<CouponModel> modelList = ParseJason.convertToList(list.toString(), CouponModel.class);
                                    if (null != modelList) {
                                        if (selectedPosition != -1) {
                                            modelList.get(selectedPosition).setSelected(true);
                                        }
                                        onUpdate(modelList);
                                        PayCouponActivity activity = (PayCouponActivity) getActivity();
                                        activity.setTab1Name(modelList.size());
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
    public void onRefreshing() {
        loadData();
    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public void onListItemClick(View view, int position) {
        Logs.i("UnusedListView---" + position);
        List<CouponModel> modelList = adapter.getDataList();
        if (modelList != null && modelList.size() > position) {
            if (selectedPosition >= 0){
                if(selectedPosition != position){ //已有选择了，并且点击的和选择的不同
                    modelList.get(selectedPosition).setSelected(false);
                    modelList.get(position).setSelected(true);
                    selectedPosition = position;
                } else {  //已有选择了，并且点击的和选择的相同
                    modelList.get(selectedPosition).setSelected(false);
                    selectedPosition = -1;
                }
            } else {    //没有选择过
                modelList.get(position).setSelected(true);
                selectedPosition = position;
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemLongClick(View view, int position) {
    }

    public void confirm() {
        Intent intent = new Intent();
        if (selectedPosition == -1) {
            intent.putExtra("couponId", 0);
            intent.putExtra("couponName", "");
            intent.putExtra("couponAmount", 0L);
            intent.putExtra("selectedPosition", -1);
        } else {
            List<CouponModel> modelList = adapter.getDataList();
            if (modelList != null && modelList.size() > selectedPosition) {
                long amount = (long) modelList.get(selectedPosition).getCoupon_value();
                intent.putExtra("couponId", modelList.get(selectedPosition).getId());
                intent.putExtra("couponName", modelList.get(selectedPosition).getCoupon_name());
                if(modelList.get(selectedPosition).getCoupon_type() == 0){ //加息券
                    intent.putExtra("couponAmount", 0L);
                } else { //直减券, 满减券
                    intent.putExtra("couponAmount", amount);
                }
                intent.putExtra("selectedPosition", selectedPosition);
            } else {
                intent.putExtra("couponId", 0);
                intent.putExtra("couponName", "");
                intent.putExtra("couponAmount", 0L);
                intent.putExtra("selectedPosition", -1);
            }
        }
        PayCouponActivity payCouponActivity = (PayCouponActivity) context;
        payCouponActivity.setResult(13, intent);
        payCouponActivity.finish();
    }

}
