package com.chinayszc.mobile.module.home.adapter;

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

/**
 * 已过期的优惠券Adapter
 * Created by Jerry on 2017/4/2.
 */

public class ExpiredListAdapter extends BaseListAdapter<CouponModel> {

    public ExpiredListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpiredViewHolder(LayoutInflater.from(context).inflate(R.layout.coupon_used_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CouponModel model = getDataList().get(position);
        ExpiredViewHolder viewHolder = (ExpiredViewHolder) holder;
        if (null != model) {
            viewHolder.expiredType.setText(model.getCategory());
            viewHolder.expiredName.setText(model.getCoupon_name());
            viewHolder.expiredDate.setText(model.getEnd_time());
            viewHolder.expiredInstruction.setText(model.getContent());
            viewHolder.expiredIcon.setImageResource(R.mipmap.expired_right_icon);

            int type = model.getCoupon_type();
            switch (type) {
                case 0:   //加息券
                    String percent = "+" + model.getCoupon_value() + "%";
                    viewHolder.expiredPercent.setText(percent);
                    break;
                case 1:   //直减券
                    String coupon0 = "¥" + model.getCoupon_value();
                    viewHolder.expiredPercent.setText(coupon0);
                    break;
                case 2:   //满减券
                    String coupon1 = "¥" + model.getCoupon_value();
                    viewHolder.expiredPercent.setText(coupon1);
                    break;
                default:
                    break;
            }
        }
    }

    private class ExpiredViewHolder extends RecyclerView.ViewHolder {

        private TextView expiredPercent;
        private TextView expiredType;
        private TextView expiredName;
        private TextView expiredDate;
        private TextView expiredInstruction;
        private ImageView expiredIcon;

        ExpiredViewHolder(View itemView) {
            super(itemView);
            expiredPercent = (TextView) itemView.findViewById(R.id.coupon_used_icon);
            expiredType = (TextView) itemView.findViewById(R.id.coupon_used_tyep);
            expiredName = (TextView) itemView.findViewById(R.id.coupon_used_name);
            expiredDate = (TextView) itemView.findViewById(R.id.coupon_used_date);
            expiredInstruction = (TextView) itemView.findViewById(R.id.coupon_used_instruction);
            expiredIcon = (ImageView) itemView.findViewById(R.id.coupon_used_right_icon);
        }
    }
}
