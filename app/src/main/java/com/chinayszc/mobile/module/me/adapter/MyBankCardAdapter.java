package com.chinayszc.mobile.module.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.module.me.model.MyBankCardModel;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 我的银行卡Adapter
 * Created by Jerry on 2017/4/2.
 */

public class MyBankCardAdapter extends BaseListAdapter<MyBankCardModel> {


    public MyBankCardAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyBankCardViewHolder(LayoutInflater.from(context).inflate(R.layout.my_bank_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyBankCardModel model = getDataList().get(position);
        MyBankCardViewHolder viewHolder = (MyBankCardViewHolder) holder;
        if (null != model) {
            viewHolder.myBankCardName.setText(model.getBankName());
            viewHolder.myBankCardNum.setText(model.getBankCardNum());
            Glide.with(context).load(model.getBankImg())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(viewHolder.myBankCardIcon);
        }
    }

    private class MyBankCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView myBankCardIcon;
        private TextView myBankCardName;
        private TextView myBankCardNum;

        MyBankCardViewHolder(View itemView) {
            super(itemView);
            myBankCardIcon = (ImageView) itemView.findViewById(R.id.my_bank_card_icon);
            myBankCardName = (TextView) itemView.findViewById(R.id.my_bank_card_name);
            myBankCardNum = (TextView) itemView.findViewById(R.id.my_bank_card_num);
        }
    }
}
