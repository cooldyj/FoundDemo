package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 填写购买金额Activity
 * Created by Jerry on 2017/4/1.
 */

public class PurchaseAmountActivity extends BaseActivity implements View.OnClickListener {

    private FinancialProductModel proModel;
    private EditText inputET;
    private long buyLimit;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_amount_activity);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            proModel = (FinancialProductModel) intent.getSerializableExtra("proModel");
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.purchase_title);
        titleView.setTitle(getResources().getString(R.string.input_purchase_amount));
        titleView.getBackIV().setOnClickListener(this);

        TextView nameTV = (TextView) findViewById(R.id.purchase_name);
        TextView purchaseTV = (TextView) findViewById(R.id.purchase_btn);
        inputET = (EditText) findViewById(R.id.purchase_amount);

        if (proModel != null) {
            nameTV.setText(proModel.getProduct_name());
            if (TextUtils.isEmpty(proModel.getLowest())) {
                buyLimit = 0L;
            } else {
                buyLimit = Long.parseLong(proModel.getLowest());
            }
            String hintStr = buyLimit + "元起购";
            inputET.setHint(hintStr);
            productId = String.valueOf(proModel.getId());
        }

        purchaseTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.purchase_btn:
                attemptSubmit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交
     */
    private void submit(final String money) {
        OkHttpUtils.post().url(Urls.SET_MONEY)
                .addCommonHeaderAndBody()
                .addParams("product", productId)
                .addParams("money", money)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("Point onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("productId", productId);
                                bundle.putString("money", money);
                                IntentUtils.startActivity(PurchaseAmountActivity.this, PayActivity.class, bundle);
                            } else {
                                Toast.makeText(PurchaseAmountActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(PurchaseAmountActivity.this, LoginActivity.class);
                                PurchaseAmountActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 判断格式
     */
    private void attemptSubmit() {
        SoftInputUtils.closedSoftInput(PurchaseAmountActivity.this);
        inputET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String inputStr = inputET.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            inputET.setError(getString(R.string.pls_input_amount));
            focusView = inputET;
            cancel = true;
        } else if (Long.parseLong(inputStr) < buyLimit) {
            inputET.setError("金额不能小于" + buyLimit + "元");
            focusView = inputET;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            submit(inputStr);
        }
    }
}
