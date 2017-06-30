package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.view.ExpiredListView;
import com.chinayszc.mobile.module.home.view.UsedListView;
import com.chinayszc.mobile.utils.Logs;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 未使用Fragment
 * Created by Jerry on 2017/3/25.
 */

public class ExpiredFragment extends BaseFragment {

    public static ExpiredFragment newInstance(){
        Bundle args = new Bundle();
        ExpiredFragment moreFragment = new ExpiredFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.expired_fragment, null);
        LRecyclerView recyclerView = (LRecyclerView) mView.findViewById(R.id.expired_recycler_view);
        ExpiredListView usedListView = new ExpiredListView(recyclerView, getActivity());
        usedListView.forceRefresh();
        Logs.d("ExpiredFragment--onCreateView");
        return mView;
    }
}
