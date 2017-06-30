package com.chinayszc.mobile.module.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.module.home.model.CouponModel;
import com.chinayszc.mobile.module.me.model.NotificationModel;

/**
 * 通知Adapter
 * Created by Jerry on 2017/4/2.
 */

public class NotificationListAdapter extends BaseListAdapter<NotificationModel> {


    public NotificationListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NotificationModel model = getDataList().get(position);
        NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
        if (null != model) {
            viewHolder.notificationContent.setText(model.getMsgContent());
            viewHolder.notificationDate.setText(model.getCreateTime());
        }
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationContent;
        private TextView notificationDate;

        NotificationViewHolder(View itemView) {
            super(itemView);
            notificationContent = (TextView) itemView.findViewById(R.id.notification_content);
            notificationDate = (TextView) itemView.findViewById(R.id.notification_date);
        }
    }
}
