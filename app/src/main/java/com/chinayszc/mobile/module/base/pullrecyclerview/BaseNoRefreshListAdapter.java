package com.chinayszc.mobile.module.base.pullrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 无下拉刷新Adapter
 * @param <T> Model类
 * @author dkzhang
 */
public class BaseNoRefreshListAdapter<T> extends RecyclerView.Adapter {
    /**
     * 上下文
     */
    protected Context context;

    /**
     * 数据区
     */
    private ArrayList<T> dataList = new ArrayList<>();

    /**
     * 点击监听器
     */
    private OnItemClickListener itemClickListener;

    /**
     * 长按监听器
     */
    private OnItemLongClickListener itemLongClickListener;


    public BaseNoRefreshListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        int adapterCount = getItemCount();
        if (position < adapterCount) {

            if (itemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(holder.itemView, position);
                    }
                });

            }

            if (itemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        itemLongClickListener.onItemLongClick(holder.itemView, position);
                        return true;
                    }
                });
            }

        }
    }

    /**
     * 取得数据数量
     *
     * @return 数据数量
     */
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 取得数据列表
     *
     * @return 数据列表
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置数据
     *
     * @param list 待添加列表
     */
    public void setDataList(Collection<T> list) {
        this.dataList.clear();
        this.dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加
     *
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
     *
     * @param position 条目编号
     */
    public void remove(int position) {
        if (this.dataList.size() > 0) {
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

    /**
     * 设置监听器
     * @param itemClickListener 监听器
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 设置监听器
     * @param itemLongClickListener 监听器
     */
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 取得上下文
     * @return 上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * 设置上下文
     * @param context 上下文
     */
    public void setContext(Context context) {
        this.context = context;
    }
}
