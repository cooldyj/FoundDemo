package com.chinayszc.mobile.module.base.pullrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import java.util.List;

/**
 * @author CJK
 * @param <T> 类型
 */
public abstract class BaseGridView<T> {

    /**
     * 每一页展示多少条数据
     */

    public static final int REQUEST_COUNT = 10;

    /**
     *
     */
    private LRecyclerView recyclerView = null;

    /**
     * Adapter 负责数据管理及UI描绘
     */
    protected BaseListAdapter adapter = null;

    /**
     * Adapter 负责数据管理及UI描绘
     */
    private LRecyclerViewAdapter recyclerViewAdapter = null;

    /**
     * 是否有更多数据
     */
    private boolean hasMoreData = true;

    /**
     * 上下文
     */
    protected Context context;

    /**
     * 是否刷新
     */
    private boolean isRefresh = true;

    public BaseGridView(LRecyclerView recyclerView, Context context) {
        this.context = context;
        this.recyclerView = recyclerView;
        setAdapter();
        init();
    }

    /**
     * 初期化
     */
    private void init() {
        recyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                hasMoreData = true;
                onRefreshing();
            }
        });

        // 加载更多事件
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                // 没有更多了
                if (adapter.getItemCount() < REQUEST_COUNT) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.NoMore, null);
                    return;
                }
                // 没有更多了
                if (!hasMoreData) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.NoMore, null);
                    return;
                }
                // 加载更多
                RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                isRefresh = false;
                onLoadingMore();
            }
        });

        // 回调点击
        recyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onListItemClick(view, position);
            }

        });

        // 回调长按
        recyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            /**
             * 回调
             * @param view 视图
             * @param position 位置
             */
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
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 加载更多点击
     */
    private View.OnClickListener footerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), recyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            isRefresh = false;
            onLoadingMore();
        }
    };

    /**
     * 更新数据
     * @param resultList 数据列表
     */
    @SuppressWarnings("unchecked")
    public void onUpdate(List<T> resultList) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        // 无数据处理
        if((resultList == null || resultList.size() == 0 ) && !isRefresh){
            hasMoreData = false;
            // 动画完成
            recyclerView.refreshComplete(REQUEST_COUNT);
            RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
            return;
        }

        // 更新数据
        if (isRefresh) {
            adapter.getDataList().clear();
        }
        if(resultList != null && resultList.size() > 0){
            adapter.addAll(resultList);
        }
        notifyDataSetChanged();
        // 动画完成
        recyclerView.refreshComplete(REQUEST_COUNT);
        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
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

    /**
     * 加载更多处理
     */
    public abstract void onLoadingMore();

    /**
     * 点击事件
     * @param view 视图
     * @param position 位置
     */
    public abstract void onListItemClick(View view, int position);

    /**
     * 长按事件
     * @param view 视图
     * @param position 位置
     */
    public abstract void onListItemLongClick(View view, int position);

    /**
     * 强制刷新
     */
    public void forceRefresh() {
        recyclerView.forceToRefresh();
//        onRefreshing();
    }

    /**
     * 取得上下文
     * @return 上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * 是否刷新
     * @return 是否刷新
     */
    public boolean isRefresh() {
        return isRefresh;
    }

    /**
     * 设置是否刷新
     * @param refresh 是否刷新
     */
    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    /**
     * 是否更多数据
     * @return 是否更多数据
     */
    public boolean isHasMoreData() {
        return hasMoreData;
    }

    /**
     * 设置是否更多数据
     * @param hasMoreData 是否更多数据
     */
    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    /**
     * 取得数据适配器
     * @return 数据适配器
     */
    public BaseListAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置数据适配器
     * @param adapter 数据适配器
     */
    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
    }

    public LRecyclerView getRecyclerView() {
        return recyclerView;
    }


}
