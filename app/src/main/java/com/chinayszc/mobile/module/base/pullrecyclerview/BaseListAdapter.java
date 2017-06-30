package com.chinayszc.mobile.module.base.pullrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author cjk
 * @param <T> 模型类
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter {
    /**
     * 上下文
     */
    protected Context context;

    /**
     * 数据区
     */
    private ArrayList<T> dataList = new ArrayList<>();

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 取得数据数量
     * @return 数据数量
     */
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    /**
     * 取得数据列表
     * @return 数据列表
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置数据
     * @param list 待添加列表
     */
    public void setDataList(Collection<T> list) {
        this.dataList.clear();
        this.dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加
     * @param list 待添加列表
     */
    public void addAll(Collection<T> list) {
        int lastIndex = this.dataList.size();
        if (this.dataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    /**
     * 删除
     * @param position 条目编号
     */
    public void remove(int position) {
        if(this.dataList.size() > 0) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }

    }

    /**
     * 清除
     */
    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }
}
