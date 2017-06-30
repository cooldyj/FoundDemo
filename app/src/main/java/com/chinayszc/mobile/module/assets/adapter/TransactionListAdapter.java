package com.chinayszc.mobile.module.assets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.pullrecyclerview.BaseListAdapter;
import com.chinayszc.mobile.module.assets.model.TransactionModel;

/**
 * 交易记录Adapter
 * Created by Jerry on 2017/4/2.
 */

public class TransactionListAdapter extends BaseListAdapter<TransactionModel> {

    public TransactionListAdapter(Context context) {
        super.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TransactionModel model = getDataList().get(position);
        TransactionViewHolder viewHolder = (TransactionViewHolder) holder;
        if (null != model) {
            viewHolder.transactionName.setText(model.getProduct_name());
            viewHolder.transactionAmount.setText(String.valueOf(model.getMoney()));
            viewHolder.transactionDate.setText(model.getCreate_time());
            viewHolder.transactionStatus.setText(model.getState());
        }
    }

    private class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView transactionName;
        private TextView transactionAmount;
        private TextView transactionDate;
        private TextView transactionStatus;

        TransactionViewHolder(View itemView) {
            super(itemView);
            transactionName = (TextView) itemView.findViewById(R.id.transaction_name);
            transactionAmount = (TextView) itemView.findViewById(R.id.transaction_amount);
            transactionDate = (TextView) itemView.findViewById(R.id.transaction_date);
            transactionStatus = (TextView) itemView.findViewById(R.id.transaction_status);
        }
    }
}
