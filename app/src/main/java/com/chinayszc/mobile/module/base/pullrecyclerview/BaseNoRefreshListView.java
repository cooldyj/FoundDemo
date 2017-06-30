package com.chinayszc.mobile.module.base.pullrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;

import java.util.Arrays;
import java.util.List;

/**
 * 无下拉刷新ListView
 *
 * @param <T> Model类
 * @author CJK
 */
public abstract class BaseNoRefreshListView<T> {

    /**
     * RecyclerView
     */
    private RecyclerView recyclerView = null;

    /**
     * Adapter 负责数据管理及UI描绘
     */
    protected BaseNoRefreshListAdapter adapter = null;

    /**
     * 上下文
     */
    protected Context context;

    public BaseNoRefreshListView(RecyclerView recyclerView, Context context) {
        this.context = context;
        this.recyclerView = recyclerView;
        setAdapter();
        init();
    }

    /**
     * 初期化
     */
    public void init() {
        recyclerView.setAdapter(adapter);
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        // 回调点击
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onListItemClick(view, position);
            }

        });

        // 回调长按
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                onListItemLongClick(view, position);
            }
        });
    }

    /**
     * 数据更新
     */
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新数据
     *
     * @param resultList 数据列表
     */
    @SuppressWarnings("unchecked")
    public void onUpdate(T[] resultList) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        // 无数据处理
        if (resultList == null || resultList.length == 0) {
            return;
        }

        // 更新数据
        List<T> list = Arrays.asList(resultList);
        adapter.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     *
     * @param resultList 数据列表
     */
    @SuppressWarnings("unchecked")
    public void onUpdate(List<T> resultList) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if(adapter.getDataList() != null){
            adapter.getDataList().clear();
        }
        if(resultList != null && resultList.size() > 0){
            adapter.addAll(resultList);
        }
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return (Activity) context;
    }


    /**
     * 设置Adapter
     */
    public abstract void setAdapter();

    /**
     * 下拉刷新处理
     */
    public abstract void onRefreshing();

//    /**
//     * 加载更多处理
//     */
//    public abstract void onLoadingMore();

    /**
     * 点击事件
     *
     * @param view     视图
     * @param position 位置
     */
    public abstract void onListItemClick(View view, int position);

    /**
     * 长按事件
     *
     * @param view     视图
     * @param position 位置
     */
    public abstract void onListItemLongClick(View view, int position);

    /**
     * 强制刷新
     */
    public void forceRefresh() {
        recyclerView.invalidate();
        onRefreshing();
    }

    /**
     * 取得数据适配器
     *
     * @return 数据适配器
     */
    public BaseNoRefreshListAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置数据适配器
     *
     * @param adapter 数据适配器
     */
    public void setAdapter(BaseNoRefreshListAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 取得上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return context;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
