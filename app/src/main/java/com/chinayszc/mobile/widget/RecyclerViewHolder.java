package com.chinayszc.mobile.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * RecyclerView的公用ViewHolder封装类
 * Created by xupan on 2016/12/6.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private SparseArray<View> mViews;
    //    private int mPosition;
    private View mConvertView;

    public RecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mViews = new SparseArray<>();
        mConvertView = itemView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);

            if (view == null) {
                String resName = mContext.getResources().getResourceEntryName(viewId);
                throw new RuntimeException("Can not find " + resName + " in parent view !");
            }

            mViews.put(viewId, view);
        }
        return (T) view;

    }

}
