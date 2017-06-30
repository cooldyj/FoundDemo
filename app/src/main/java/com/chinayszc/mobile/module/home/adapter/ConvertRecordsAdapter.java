package com.chinayszc.mobile.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.home.model.ConvertRecordModel;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;

/**
 * 兑换记录页面Adapter
 * Created by Jerry on 2017/3/31.
 */

public class ConvertRecordsAdapter extends BaseListAdapter<ConvertRecordModel> {

    public ConvertRecordsAdapter(Context context) {
        super.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConvertRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.convert_records_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConvertRecordModel item = getDataList().get(position);
        ConvertRecordViewHolder viewHolder = (ConvertRecordViewHolder) holder;

        if (null != item) {
            Glide.with(context).load(item.getProduct_thumb_img()).placeholder(R.mipmap.home_place_holder).into(viewHolder.productImg);
            viewHolder.productName.setText(item.getProduct_name());
            viewHolder.productPoint.setText(String.valueOf(item.getCredit()));
            viewHolder.productDate.setText(item.getCreate_time());
            viewHolder.productOrderId.setText("订单号：" + item.getOrder_code());
        }
    }

    private class ConvertRecordViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        TextView productName;
        TextView productPoint;
        TextView productDate;
        TextView productOrderId;

        ConvertRecordViewHolder(View itemView) {
            super(itemView);
            productImg = (ImageView) itemView.findViewById(R.id.convert_record_img);
            productName = (TextView) itemView.findViewById(R.id.convert_record_name);
            productPoint = (TextView) itemView.findViewById(R.id.convert_record_point);
            productDate = (TextView) itemView.findViewById(R.id.convert_record_date);
            productOrderId = (TextView) itemView.findViewById(R.id.convert_record_num);
        }

    }
}
