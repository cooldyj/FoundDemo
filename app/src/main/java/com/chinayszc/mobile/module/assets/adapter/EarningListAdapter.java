package com.chinayszc.mobile.module.assets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.model.EarningModel;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;

/**
 * 收益记录Adapter
 * Created by Jerry on 2017/4/2.
 */

public class EarningListAdapter extends BaseListAdapter<EarningModel> {

    public EarningListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.earning_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EarningModel model = getDataList().get(position);
        TransactionViewHolder viewHolder = (TransactionViewHolder) holder;
        if (null != model) {
            viewHolder.earningDate.setText(model.getProfit_day());
            viewHolder.earningAmount.setText(String.valueOf(model.getProfit_money()));
        }
    }

    private class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView earningDate;
        private TextView earningAmount;

        TransactionViewHolder(View itemView) {
            super(itemView);
            earningDate = (TextView) itemView.findViewById(R.id.earning_date);
            earningAmount = (TextView) itemView.findViewById(R.id.earning_amount);
        }
    }
}
