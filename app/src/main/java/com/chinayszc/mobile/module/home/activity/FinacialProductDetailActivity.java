package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.BankCardModel;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 金融产品详情页Activity
 * Created by Jerry on 2017/4/1.
 */

public class FinacialProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private ListView bankLV;
    private BaseAdapter adapter;
    private List<BankCardModel> bankList;
    private int proId;

    private TextView proTitleTV;
    private TextView proTotalAmountTV;
    private TextView percentTV;
    private TextView limitDayTV;
    private TextView startDayTV;
    private TextView endDayTV;
    private TextView buyLimitTV;
    private TextView feeTV;
    private TextView taxTV;
    private TextView withdrawTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finacial_product_detail_activity);
        getData();
        initView();
        initListView();
        loadProduct();
    }

    private void getData() {
        Intent intent = getIntent();
        if(intent != null){
            proId = intent.getIntExtra("proId", 0);
        }
    }

    private void initView() {
        bankList = new ArrayList<>();
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.financial_product_detail_title);
        titleView.setTitle(getResources().getString(R.string.product_detail));
        titleView.getBackIV().setOnClickListener(this);

        bankLV = (ListView) findViewById(R.id.financial_product_detail_list);
        View header = getLayoutInflater().inflate(R.layout.financial_product_detail_header, null);

        proTitleTV = (TextView) header.findViewById(R.id.pro_det_title);
        proTotalAmountTV = (TextView) header.findViewById(R.id.prod_total);
        percentTV = (TextView) header.findViewById(R.id.prod_expected_annualization);
        limitDayTV = (TextView) header.findViewById(R.id.prod_deadline);
        startDayTV = (TextView) header.findViewById(R.id.prod_intrest_day);
        endDayTV = (TextView) header.findViewById(R.id.prod_end_day);
        buyLimitTV = (TextView) header.findViewById(R.id.prod_buy_limit);
        feeTV = (TextView) header.findViewById(R.id.prod_fees);
        taxTV = (TextView) header.findViewById(R.id.prod_income_tax);
        withdrawTV = (TextView) header.findViewById(R.id.prod_income_redemption);

        bankLV.addHeaderView(header);
    }

    private void initListView() {
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return bankList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.bank_card_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.bankIcon = (ImageView) convertView.findViewById(R.id.bank_card_icon);
                    viewHolder.bankName = (TextView) convertView.findViewById(R.id.bank_card_name);
                    viewHolder.bankQuota = (TextView) convertView.findViewById(R.id.bank_card_quota);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                if (bankList.size() > position) {
                    BankCardModel model = bankList.get(position);
                    if (null != model) {
                        viewHolder.bankName.setText(model.getName());
                        viewHolder.bankQuota.setText(model.getQuota());
                        Glide.with(FinacialProductDetailActivity.this).load(model.getThumb_logo())
                                .into(viewHolder.bankIcon);
                    }
                }
                return convertView;
            }
        };
        bankLV.setAdapter(adapter);
    }

    private class ViewHolder {
        ImageView bankIcon;
        TextView bankName;
        TextView bankQuota;
    }

    /**
     * 加载产品列表
     */
    private void loadProduct(){
        OkHttpUtils.post().url(Urls.PRODUCT)
                .addCommonHeaderAndBody()
                .addParams("product", String.valueOf(proId))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("PRODUCT_LIST onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("PRODUCT_LIST onResponse---" + response);
                        if(null != response){
                            JSONArray list = response.optJSONArray("bank");
                            JSONObject pro = response.optJSONObject("product");
                            if(list != null){
                                List<BankCardModel> modelList = ParseJason.convertToList(list.toString(), BankCardModel.class);
                                if(null != modelList){
                                    if(!bankList.isEmpty()){
                                        bankList.clear();
                                    }
                                    bankList.addAll(modelList);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if(pro != null){
                                FinancialProductModel productModel = ParseJason.convertToEntity(pro.toString(), FinancialProductModel.class);
                                if(productModel != null){
                                    proTitleTV.setText(productModel.getProduct_name());
                                    proTotalAmountTV.setText(productModel.getProduct_money());
                                    percentTV.setText(productModel.getProfit() + "%");
                                    limitDayTV.setText(productModel.getDay_no() + "天");
                                    startDayTV.setText(productModel.getStart_time());
                                    endDayTV.setText(productModel.getEnd_time());
                                    buyLimitTV.setText(productModel.getMoney_limit() + "元");
                                }
                            }
                            String feeStr = response.optString("fee");
                            String taxStr = response.optString("tax");
                            String withdrawStr = response.optString("take");
                            feeTV.setText(feeStr);
                            taxTV.setText(taxStr);
                            withdrawTV.setText(withdrawStr);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
