package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.view.PayCouponUsedListView;
import com.chinayszc.mobile.module.home.view.UsedListView;
import com.chinayszc.mobile.utils.Logs;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 支付--优惠券--不可使用Fragment
 * Created by Jerry on 2017/3/25.
 */

public class PayCouponUsedFragment extends BaseFragment {

    public static PayCouponUsedFragment newInstance(Bundle args){
        PayCouponUsedFragment moreFragment = new PayCouponUsedFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.used_fragment, null);

        String productId = "";
        String amount = "";
        Bundle bundle = getArguments();
        if(bundle != null){
            productId = bundle.getString("productId");
            amount = bundle.getString("amount");
        }

        LRecyclerView recyclerView = (LRecyclerView) mView.findViewById(R.id.used_recycler_view);
        PayCouponUsedListView usedListView = new PayCouponUsedListView(recyclerView, getActivity(), productId, amount);
        recyclerView.setLoadMoreEnabled(false);
        usedListView.forceRefresh();
        Logs.d("UsedFragment--onCreateView");
        return mView;
    }
}
