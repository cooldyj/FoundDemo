package com.chinayszc.mobile.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.module.home.model.MyPointsModel;

/**
 * 我的积分Adapter
 * Created by Jerry on 2017/4/1.
 */

public class MyPointsAdapter extends BaseListAdapter<MyPointsModel> {

    public MyPointsAdapter(Context context) {
        super.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPointsViewHolder(LayoutInflater.from(context).inflate(R.layout.my_points_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyPointsModel item = getDataList().get(position);
        MyPointsViewHolder viewHolder = (MyPointsViewHolder) holder;

        if (null != item) {
            viewHolder.myPointDetail.setText(item.getType_name());
            viewHolder.myPointDate.setText(item.getCreate_time());
            viewHolder.myPointNum.setText(String.valueOf(item.getCredit()));
        }
    }

    private class MyPointsViewHolder extends RecyclerView.ViewHolder {

        TextView myPointDetail;
        TextView myPointDate;
        TextView myPointNum;

        MyPointsViewHolder(View itemView) {
            super(itemView);
            myPointDetail = (TextView) itemView.findViewById(R.id.my_point_detail);
            myPointDate = (TextView) itemView.findViewById(R.id.my_point_date);
            myPointNum = (TextView) itemView.findViewById(R.id.my_point_num);
        }
    }
}
