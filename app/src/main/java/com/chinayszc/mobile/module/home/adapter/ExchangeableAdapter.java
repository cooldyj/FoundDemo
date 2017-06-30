package com.chinayszc.mobile.module.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.home.activity.ProductDetailActivity;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;

/**
 * 我可兑换页面Adapter
 * Created by Jerry on 2017/3/31.
 */

public class ExchangeableAdapter extends BaseListAdapter<PointProductModel> {

    public ExchangeableAdapter(Context context) {
        super.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExchangeableViewHolder(LayoutInflater.from(context).inflate(R.layout.point_mall_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PointProductModel item = getDataList().get(position);
        ExchangeableViewHolder viewHolder = (ExchangeableViewHolder) holder;

        if (null != item) {
            Glide.with(context).load(item.getProduct_thumb_img()).placeholder(R.mipmap.home_place_holder).into(viewHolder.productImg);
            viewHolder.productName.setText(item.getProduct_name());
            viewHolder.productPoint.setText(String.valueOf(item.getCredit()));
            viewHolder.productLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logs.i("id---" + item.getId());
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", item.getId());
                    IntentUtils.startActivity(context, ProductDetailActivity.class, bundle);
                }
            });
        }
    }

    private class ExchangeableViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productLayout;
        ImageView productImg;
        TextView productName;
        TextView productPoint;

        ExchangeableViewHolder(View itemView) {
            super(itemView);
            productLayout = (LinearLayout) itemView.findViewById(R.id.point_item_layout);
            productImg = (ImageView) itemView.findViewById(R.id.point_item_img);
            productName = (TextView) itemView.findViewById(R.id.point_item_name);
            productPoint = (TextView) itemView.findViewById(R.id.point_item_point);
        }

    }
}
