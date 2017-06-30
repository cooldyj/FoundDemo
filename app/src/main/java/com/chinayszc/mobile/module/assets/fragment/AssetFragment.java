package com.chinayszc.mobile.module.assets.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.activity.EarningRecordActivity;
import com.chinayszc.mobile.module.assets.activity.RechargeActivity;
import com.chinayszc.mobile.module.assets.activity.TransactionRecordActivity;
import com.chinayszc.mobile.module.assets.activity.WithdrawActivity;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.main.MainNavigationActivity;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 我的资产Fragment
 * Created by Jerry on 2017/3/25.
 */

public class AssetFragment extends BaseFragment implements View.OnClickListener {

    private TextView totalAsset;
    private TextView transactionRecord;
    private TextView earningRecord;
    private TextView totalIncome;
    private TextView withdrawAmount;
    private TextView onWayAmount;
    private TextView recharge;
    private TextView withdraw;
    private TextView goto_manage;

    private String cash ;          //可提现金额
    private String fund ;          //总资产
    private String profit ;        //总收益
    private String onWay ;         //在途金额

    public static AssetFragment newInstance(){
        Bundle args = new Bundle();
        AssetFragment myAssetFragment = new AssetFragment();
        myAssetFragment.setArguments(args);
        return myAssetFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.asset_fragment, null);
        totalAsset = (TextView) mView.findViewById(R.id.asset_total_asset);
        transactionRecord = (TextView) mView.findViewById(R.id.asset_transaction_record);
        earningRecord = (TextView) mView.findViewById(R.id.asset_transaction_earnings);
        totalIncome = (TextView) mView.findViewById(R.id.asset_total_income);
        withdrawAmount = (TextView) mView.findViewById(R.id.asset_withdraw_amount);
        onWayAmount = (TextView) mView.findViewById(R.id.asset_on_way_amount);
        recharge = (TextView) mView.findViewById(R.id.asset_recharge);
        withdraw = (TextView) mView.findViewById(R.id.asset_withdraw);
        goto_manage = (TextView) mView.findViewById(R.id.asset_goto_manage);

        transactionRecord.setOnClickListener(this);
        earningRecord.setOnClickListener(this);
        recharge.setOnClickListener(this);
        withdraw.setOnClickListener(this);
        goto_manage.setOnClickListener(this);
        Logs.d("AssetFragment--onCreateView");
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.asset_transaction_record:
                IntentUtils.startActivity(getActivity(), TransactionRecordActivity.class);
                break;
            case R.id.asset_transaction_earnings:
                IntentUtils.startActivity(getActivity(), EarningRecordActivity.class);
                break;
            case R.id.asset_recharge:
                Bundle bundle0 = new Bundle();
                bundle0.putString("balanceAmount", fund);
                IntentUtils.startActivity(getActivity(), RechargeActivity.class, bundle0);
                break;
            case R.id.asset_withdraw:
                Bundle bundle1 = new Bundle();
                bundle1.putString("withdrawAmount", cash);
                IntentUtils.startActivity(getActivity(), WithdrawActivity.class, bundle1);
                break;
            case R.id.asset_goto_manage:
                ((MainNavigationActivity)getActivity()).jumpToTab(R.id.navigation_product);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAsset();
    }

    /**
     * 加载推荐产品
     */
    private void loadAsset() {
        OkHttpUtils.post().url(Urls.GET_ASSET)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("Point onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                JSONObject jo = response.optJSONObject("asset");
                                if(jo != null){
                                    cash = jo.optString("cash", "0.00");        //可提现金额
                                    fund = jo.optString("fund", "0.00");        //总资产
                                    profit = jo.optString("profit", "0.00");    //总收益
                                    onWay = jo.optString("on_way", "0.00");    //总收益
                                    totalAsset.setText(String.valueOf(fund));
                                    totalIncome.setText(String.valueOf(profit));
                                    withdrawAmount.setText(String.valueOf(cash));
                                    onWayAmount.setText(String.valueOf(onWay));
                                } else {
                                    totalAsset.setText("0.00");
                                    totalIncome.setText("0.00");
                                    withdrawAmount.setText("0.00");
                                    onWayAmount.setText("0.00");
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
//                                IntentUtils.startActivity(getActivity(), LoginActivity.class);
                            }
                        }
                    }
                });
    }
}
