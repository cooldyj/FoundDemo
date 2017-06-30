package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.base.BaseWebView;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.BankCardModel;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.DecimalUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * 金融产品Activity
 * Created by Jerry on 2017/4/1.
 */

public class FinancialProductActivity extends BaseActivity implements View.OnClickListener {

    private FinancialProductModel model;
    private int from;   //0:首页跳转过来；1:产品页跳转过来
    private CommonTitleView titleView;
    private TextView percentTV;
    private TextView limitDayTV;
    private TextView startDayTV;
    private TextView buyLimitTV;
    private TextView endDayTV;
    private TextView buyNumTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financial_product_activity);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getIntExtra("from", 0);
            if (from == 1) {
                model = (FinancialProductModel) intent.getSerializableExtra("proModel");
            } else {
                int prodId = intent.getIntExtra("prodId", 0);
                loadProduct(prodId);
            }
        }
    }

    private void initView() {
        titleView = (CommonTitleView) findViewById(R.id.financial_product_title);
        titleView.getBackIV().setOnClickListener(this);

        FrameLayout safetyBtn = (FrameLayout) findViewById(R.id.financial_product_safety);
        FrameLayout detailBtn = (FrameLayout) findViewById(R.id.financial_product_detail);
        FrameLayout disclaimerBtn = (FrameLayout) findViewById(R.id.financial_product_disclaimer);
        TextView buyBtn = (TextView) findViewById(R.id.financial_product_buy);

        percentTV = (TextView) findViewById(R.id.financial_product_percent);
        buyNumTV = (TextView) findViewById(R.id.financial_product_buy_num);
        limitDayTV = (TextView) findViewById(R.id.financial_product_limit_day);
        startDayTV = (TextView) findViewById(R.id.financial_product_start_day);
        buyLimitTV = (TextView) findViewById(R.id.financial_product_buy_limit);
        endDayTV = (TextView) findViewById(R.id.financial_product_end_day);

        if (model != null) {
            String titleStr = model.getProduct_name();
            String percentStr = DecimalUtils.get2DecimalStr(model.getProfit());
            String buyNumStr = model.getCount();
            String limitDayStr = model.getDay_no() + "天";
            String startDayStr = model.getStart_time();
            String buyLimitStr = model.getLowest() + "元";
            String endDayStr = model.getEnd_time();

            titleView.setTitle(titleStr);
            percentTV.setText(percentStr);
            limitDayTV.setText(limitDayStr);
            startDayTV.setText(startDayStr);
            buyLimitTV.setText(buyLimitStr);
            endDayTV.setText(endDayStr);

            if (TextUtils.isEmpty(buyNumStr)) {
                buyNumStr = "0";
            }
            buyNumTV.setText(getString("最近30天已有", buyNumStr, "人购买该系列"));
        }

        safetyBtn.setOnClickListener(this);
        detailBtn.setOnClickListener(this);
        disclaimerBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.financial_product_safety:
                IntentUtils.startActivity(FinancialProductActivity.this, AssetSafetyActivity.class);
                break;
            case R.id.financial_product_detail:
                int proId = 0;
                if (model != null) {
                    proId = model.getId();
                }
                Bundle bundle = new Bundle();
                bundle.putInt("proId", proId);
                IntentUtils.startActivity(FinancialProductActivity.this, FinacialProductDetailActivity.class, bundle);
                break;
            case R.id.financial_product_disclaimer:
                IntentUtils.startActivity(FinancialProductActivity.this, DisclaimerActivity.class);
                break;
            case R.id.financial_product_buy:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("proModel", model);
                IntentUtils.startActivity(FinancialProductActivity.this, PurchaseAmountActivity.class, bundle1);
                break;
            default:
                break;
        }
    }

    private SpannableStringBuilder getString(String text1, String text2, String text3) {
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

    /**
     * 加载产品列表
     */
    private void loadProduct(int proId) {
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
                        if (null != response) {
                            JSONObject pro = response.optJSONObject("product");
                            if (pro != null) {
                                FinancialProductModel productModel = ParseJason.convertToEntity(pro.toString(), FinancialProductModel.class);
                                if (productModel != null) {
                                    model = productModel;
                                    String titleStr = productModel.getProduct_name();
                                    String percentStr = String.valueOf(DecimalUtils.get2DecimalStr(productModel.getProfit()));
                                    String buyNumStr = productModel.getCount();
                                    String limitDayStr = productModel.getDay_no() + "天";
                                    String startDayStr = productModel.getStart_time();
                                    String buyLimitStr = productModel.getLowest() + "元";
                                    String endDayStr = productModel.getEnd_time();

                                    titleView.setTitle(titleStr);
                                    percentTV.setText(percentStr);
                                    limitDayTV.setText(limitDayStr);
                                    startDayTV.setText(startDayStr);
                                    buyLimitTV.setText(buyLimitStr);
                                    endDayTV.setText(endDayStr);

                                    if (TextUtils.isEmpty(buyNumStr)) {
                                        buyNumStr = "0";
                                    }
                                    buyNumTV.setText(getString("最近30天已有", buyNumStr, "人购买该系列"));
                                }
                            }
                        }
                    }
                });
    }
}
