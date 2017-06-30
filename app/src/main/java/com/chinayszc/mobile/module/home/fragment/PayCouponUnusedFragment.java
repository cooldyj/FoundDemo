package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.view.PayCouponUnusedListView;
import com.chinayszc.mobile.module.home.view.UnusedListView;
import com.chinayszc.mobile.utils.Logs;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

/**
 * 支付--优惠券--可以使用Fragment
 * Created by Jerry on 2017/3/25.
 */

public class PayCouponUnusedFragment extends BaseFragment implements View.OnClickListener {

    private PayCouponUnusedListView unusedListView;

    public static PayCouponUnusedFragment newInstance(Bundle args){
        PayCouponUnusedFragment moreFragment = new PayCouponUnusedFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.pay_coupon_unused_fragment, null);
        mView.findViewById(R.id.pay_coupon_btn).setOnClickListener(this);

        String productId = "";
        String amount = "";
        int selectedPosition = -1;
        Bundle bundle = getArguments();
        if(bundle != null){
            productId = bundle.getString("productId");
            amount = bundle.getString("amount");
            selectedPosition = bundle.getInt("selectedPosition", -1);
        }
        LRecyclerView recyclerView = (LRecyclerView) mView.findViewById(R.id.unused_recycler_view);
        unusedListView = new PayCouponUnusedListView(recyclerView, getActivity(), productId, amount, selectedPosition);
        recyclerView.setLoadMoreEnabled(false);
        unusedListView.forceRefresh();
        Logs.d("UnusedFragment--onCreateView");
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_coupon_btn:
                unusedListView.confirm();
                break;
            default:
                break;
        }
    }
}
