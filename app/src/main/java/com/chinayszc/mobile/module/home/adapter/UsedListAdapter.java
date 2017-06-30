package com.chinayszc.mobile.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.module.home.model.CouponModel;

/**
 * 已使用的优惠券Adapter
 * Created by Jerry on 2017/4/2.
 */

public class UsedListAdapter extends BaseListAdapter<CouponModel> {

    public UsedListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsedViewHolder(LayoutInflater.from(context).inflate(R.layout.coupon_used_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CouponModel model = getDataList().get(position);
        UsedViewHolder viewHolder = (UsedViewHolder) holder;
        if (null != model) {
            viewHolder.usedType.setText(model.getCategory());
            viewHolder.usedName.setText(model.getCoupon_name());
            viewHolder.usedDate.setText(model.getEnd_time());
            viewHolder.usedInstruction.setText(model.getContent());
            viewHolder.usedIcon.setImageResource(R.mipmap.uses_right_icon);

            int type = model.getCoupon_type();
            switch (type) {
                case 0:   //加息券
                    String percent = "+" + model.getCoupon_value() + "%";
                    viewHolder.usedPercent.setText(percent);
                    break;
                case 1:   //直减券
                    String coupon0 = "¥" + model.getCoupon_value();
                    viewHolder.usedPercent.setText(coupon0);
                    break;
                case 2:   //满减券
                    String coupon1 = "¥" + model.getCoupon_value();
                    viewHolder.usedPercent.setText(coupon1);
                    break;
                default:
                    break;
            }
        }
    }

    private class UsedViewHolder extends RecyclerView.ViewHolder {

        private TextView usedPercent;
        private TextView usedType;
        private TextView usedName;
        private TextView usedDate;
        private TextView usedInstruction;
        private ImageView usedIcon;

        UsedViewHolder(View itemView) {
            super(itemView);
            usedPercent = (TextView) itemView.findViewById(R.id.coupon_used_icon);
            usedType = (TextView) itemView.findViewById(R.id.coupon_used_tyep);
            usedName = (TextView) itemView.findViewById(R.id.coupon_used_name);
            usedDate = (TextView) itemView.findViewById(R.id.coupon_used_date);
            usedInstruction = (TextView) itemView.findViewById(R.id.coupon_used_instruction);
            usedIcon = (ImageView) itemView.findViewById(R.id.coupon_used_right_icon);
        }
    }
}
