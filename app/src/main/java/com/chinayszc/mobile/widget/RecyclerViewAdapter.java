package com.chinayszc.mobile.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView公用Adapter，暂只支持单一item布局；分隔线请添加在item布局中。
 * Created by xupan on 2016/12/6.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<T> mData;
    private int mItemLayoutId;
    protected Context mContext;

    public RecyclerViewAdapter(Context mContext, int mItemLayoutId, List<T> mData) {
        this.mContext = mContext;
        this.mItemLayoutId = mItemLayoutId;
        this.mData = mData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
        return new RecyclerViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        convertItemView(holder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
    }

    /**
     * 抽象方法，可以在具体实现中，设置ItemView的具体显示内容，如TextView内容，ImageView的URL，以及设置点击事件监听等。
     *
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void convertItemView(RecyclerViewHolder holder, T item, final int position);
}

