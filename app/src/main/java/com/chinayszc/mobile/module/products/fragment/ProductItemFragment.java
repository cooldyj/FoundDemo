package com.chinayszc.mobile.module.products.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.home.activity.FinacialProductDetailActivity;
import com.chinayszc.mobile.module.home.activity.FinancialProductActivity;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.utils.DecimalUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;

/**
 * 产品画廊ItemFragment
 * Created by Jerry on 2017/4/6.
 */

public class ProductItemFragment extends BaseFragment implements View.OnClickListener {

    private FinancialProductModel model;

    public static ProductItemFragment newInstance(Bundle args){
        ProductItemFragment ProductFragment = new ProductItemFragment();
        ProductFragment.setArguments(args);
        return ProductFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logs.d("ProductItemFragment--onCreateView");
        View mView = inflater.inflate(R.layout.product_item_fragment, null);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.pro_item_layout);
        TextView titleTV = (TextView) mView.findViewById(R.id.pro_item_title);
        TextView percentTV = (TextView) mView.findViewById(R.id.pro_item_percent);
        TextView buyNumTV = (TextView) mView.findViewById(R.id.pro_item_buy_num);
        TextView limitDayTV = (TextView) mView.findViewById(R.id.pro_item_limit_day);
        TextView startDayTV = (TextView) mView.findViewById(R.id.pro_item_start_day);
        TextView buyLimitTV = (TextView) mView.findViewById(R.id.pro_item_buy_limit);
        TextView endDayTV = (TextView) mView.findViewById(R.id.pro_item_end_day);

        Bundle bundle = getArguments();
        if(bundle != null){
            model = (FinancialProductModel) bundle.getSerializable("proModel");
            if(model != null){
                String titleStr = model.getProduct_name();
                String percentStr = String.valueOf(DecimalUtils.get2DecimalStr(model.getProfit()));
                String buyNumStr = model.getCount();
                String limitDayStr = model.getDay_no() + "天";
                String startDayStr = model.getStart_time();
                String buyLimitStr = model.getLowest() + "元";
                String endDayStr = model.getEnd_time();

                titleTV.setText(titleStr);
                percentTV.setText(percentStr);
                limitDayTV.setText(limitDayStr);
                startDayTV.setText(startDayStr);
                buyLimitTV.setText(buyLimitStr);
                endDayTV.setText(endDayStr);

                if(TextUtils.isEmpty(buyNumStr)){
                    buyNumStr = "0";
                }
                buyNumTV.setText(getString("最近30天已有", buyNumStr, "人购买该系列"));
            }
        }

        layout.setOnClickListener(this);
        return mView;
    }

    private SpannableStringBuilder getString(String text1, String text2, String text3){
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        ssBuilder.append(text1);
        ssBuilder.append(text2);
        ssBuilder.append(text3);

        //构造改变字体颜色的Span
        ForegroundColorSpan text1ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_dark));
        ForegroundColorSpan text2ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_light_red));
        ForegroundColorSpan text3ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_dark));

        ssBuilder.setSpan(text1ColorSpan, 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(text2ColorSpan, text1.length(), text1.length() + text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(text3ColorSpan, text1.length() + text2.length(), ssBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssBuilder;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pro_item_layout:
                Bundle bundle = new Bundle();
                bundle.putSerializable("proModel", model);
                bundle.putInt("from", 1);
                IntentUtils.startActivity(getActivity(), FinancialProductActivity.class, bundle);
                break;
            default:
                break;
        }
    }
}
