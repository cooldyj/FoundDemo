package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.view.UnusedListView;
import com.chinayszc.mobile.utils.Logs;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 未使用Fragment
 * Created by Jerry on 2017/3/25.
 */

public class UnusedFragment extends BaseFragment {

    public static UnusedFragment newInstance(){
        Bundle args = new Bundle();
        UnusedFragment moreFragment = new UnusedFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.unused_fragment, null);
        LRecyclerView recyclerView = (LRecyclerView) mView.findViewById(R.id.unused_recycler_view);
        UnusedListView unusedListView = new UnusedListView(recyclerView, getActivity());
        unusedListView.forceRefresh();
        Logs.d("UnusedFragment--onCreateView");
        return mView;
    }
}
