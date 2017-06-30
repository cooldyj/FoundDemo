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
 * 未使用的优惠券Adapter
 * Created by Jerry on 2017/4/2.
 */

public class PayCouponUnusedListAdapter extends BaseListAdapter<CouponModel> {

    public PayCouponUnusedListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UnusedViewHolder(LayoutInflater.from(context).inflate(R.layout.coupon_unused_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CouponModel model = getDataList().get(position);
        UnusedViewHolder viewHolder = (UnusedViewHolder) holder;
        if (null != model) {
            viewHolder.unusedType.setText(model.getCategory());
            viewHolder.unusedName.setText(model.getCoupon_name());
            viewHolder.unusedDate.setText(model.getEnd_time());
            viewHolder.unusedInstruction.setText(model.getContent());

            int type = model.getCoupon_type();
            switch (type) {
                case 0:   //加息券
                    viewHolder.unusedType.setBackground(context.getResources().getDrawable(R.drawable.coupon_type_text_blue));
                    String percent = "+" + model.getCoupon_value() + "%";
                    viewHolder.unusedPercent.setText(percent);
                    break;
                case 1:   //直减券
                    viewHolder.unusedType.setBackground(context.getResources().getDrawable(R.drawable.coupon_type_text_red));
                    String coupon0 = "¥" + model.getCoupon_value();
                    viewHolder.unusedPercent.setText(coupon0);
                    break;
                case 2:   //满减券
                    viewHolder.unusedType.setBackground(context.getResources().getDrawable(R.drawable.coupon_type_text_light_red));
                    String coupon1 = "¥" + model.getCoupon_value();
                    viewHolder.unusedPercent.setText(coupon1);
                    break;
                default:
                    break;
            }

            if (model.isSelected()) {
                viewHolder.unusedstatus.setVisibility(View.VISIBLE);
            } else {
                viewHolder.unusedstatus.setVisibility(View.GONE);
            }
        }
    }

    private class UnusedViewHolder extends RecyclerView.ViewHolder {

        private TextView unusedPercent;
        private TextView unusedType;
        private TextView unusedName;
        private TextView unusedDate;
        private TextView unusedInstruction;
        private ImageView unusedstatus;

        UnusedViewHolder(View itemView) {
            super(itemView);
            unusedPercent = (TextView) itemView.findViewById(R.id.coupon_unused_icon);
            unusedType = (TextView) itemView.findViewById(R.id.coupon_unused_tyep);
            unusedName = (TextView) itemView.findViewById(R.id.coupon_unused_name);
            unusedDate = (TextView) itemView.findViewById(R.id.coupon_unused_date);
            unusedInstruction = (TextView) itemView.findViewById(R.id.coupon_unused_instruction);
            unusedstatus = (ImageView) itemView.findViewById(R.id.coupon_unused_status);
        }
    }
}
