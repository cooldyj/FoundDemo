package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.view.UnusedListView;
import com.chinayszc.mobile.module.home.view.UsedListView;
import com.chinayszc.mobile.utils.Logs;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 已使用Fragment
 * Created by Jerry on 2017/3/25.
 */

public class UsedFragment extends BaseFragment {

    public static UsedFragment newInstance(){
        Bundle args = new Bundle();
        UsedFragment moreFragment = new UsedFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.used_fragment, null);
        LRecyclerView recyclerView = (LRecyclerView) mView.findViewById(R.id.used_recycler_view);
        UsedListView usedListView = new UsedListView(recyclerView, getActivity());
        usedListView.forceRefresh();
        Logs.d("UsedFragment--onCreateView");
        return mView;
    }
}
