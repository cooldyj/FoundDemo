package com.chinayszc.mobile.module.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.module.me.activity.MyBankCardActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.CustomDialog;
import com.chinayszc.mobile.widget.wheelview.OnButtonClickedListener;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 支付Activity
 * Created by Jerry on 2017/4/1.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private TextView balanceAmountTV;
    private ImageView balanceIV;
    private TextView couponTV;
    private ImageView couponIV;
    private TextView bankAmountTV;
    private ImageView bankIconIV;
    private TextView bankNameTV;
    private LinearLayout bankLayout;
    private LinearLayout addbankLayout;
    private CheckBox checkBox;
    private TextView payTV;

    private String productId;           //产品Id
    private int isCoupon;               //是否有优惠券可用
    private boolean isUseBalance;       //是否使用余额
    private boolean isUseCoupon;        //是否使用优惠券
    private int selectedPosition = -1;  //已选择的优惠券position

    private long payAmount;             //总金额
    private long originalBalanceAmount; //原始的余额
    private long balanceAmount;         //可使用的余额
    private long bankAmount;            //需银行卡支付的金额
    private int couponId = 0;           //使用的优惠券Id
    private String couponName;          //使用的优惠券名称
    private long couponAmount;          //使用的优惠券金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        getData();
        initView();
        loadData();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            String money = intent.getStringExtra("money");
            payAmount = Long.parseLong(money);
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.pay_title);
        titleView.setTitle(getResources().getString(R.string.pay));
        titleView.getBackIV().setOnClickListener(this);

        TextView payAmountTV = (TextView) findViewById(R.id.pay_amount);
        balanceAmountTV = (TextView) findViewById(R.id.pay_zc_amount);
        couponTV = (TextView) findViewById(R.id.pay_coupon_status);
        bankAmountTV = (TextView) findViewById(R.id.pay_bank_pay_amount);
        bankNameTV = (TextView) findViewById(R.id.pay_bank_name);
        balanceIV = (ImageView) findViewById(R.id.pay_zc_icon);
        couponIV = (ImageView) findViewById(R.id.pay_coupon_icon);
        bankIconIV = (ImageView) findViewById(R.id.pay_bank_icon);
        bankLayout = (LinearLayout) findViewById(R.id.pay_bank_layout);
        addbankLayout = (LinearLayout) findViewById(R.id.pay_add_bank_layout);
        payTV = (TextView) findViewById(R.id.pay_btn);
        TextView instruction1TV = (TextView) findViewById(R.id.pay_instruction1);
        TextView instruction2TV = (TextView) findViewById(R.id.pay_instruction2);
        checkBox = (CheckBox) findViewById(R.id.pay_checkbox);

        payAmountTV.setText(String.valueOf(payAmount) + "元");

        balanceIV.setOnClickListener(this);
        balanceAmountTV.setOnClickListener(this);
        bankLayout.setOnClickListener(this);
        addbankLayout.setOnClickListener(this);
        payTV.setOnClickListener(this);
        instruction1TV.setOnClickListener(this);
        instruction2TV.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    payTV.setEnabled(true);
                    payTV.setBackground(getResources().getDrawable(R.drawable.common_btn_bg));
                } else {
                    payTV.setEnabled(false);
                    payTV.setBackgroundColor(getResources().getColor(R.color.btn_gray));
                }
            }
        });
    }

    /**
     * 充值
     */
    private void payOrder(){
        int cashMoney;
        if(isUseBalance){
            cashMoney = (int) balanceAmount;
        }else {
            cashMoney = 0;
        }
        Logs.i("product---" + productId +
                "---totalMoney---" + payAmount +
                "---cashMoney---" + cashMoney +
                "---bankMoney---" + bankAmount +
                "---coupon---" + couponId +
                "---couponMoney---" + couponAmount);
        OkHttpUtils.post().url(Urls.SUBMIT_ORDER)
                .addCommonHeaderAndBody()
                .addParams("product", productId)
                .addParams("totalMoney", String.valueOf((int)payAmount))
                .addParams("cashMoney", String.valueOf(cashMoney))
                .addParams("bankMoney", String.valueOf((int)bankAmount))
                .addParams("coupon", String.valueOf(couponId))
                .addParams("couponMoney", String.valueOf((int)couponAmount))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PayActivity.this, "支付失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                if(bankAmount == 0){ //如果使用余额支付，不使用银行卡，就直接支付成功
                                    Toast.makeText(PayActivity.this, getResources().getString(R.string.pay_succee), Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", Urls.FINANCIAL_PRODUCT_SUCCESS);
                                    IntentUtils.startActivity(PayActivity.this, BoughtSuccessActivity.class, bundle);
                                    PayActivity.this.finish();
                                } else {
                                    showVerifyCodeDialog();
                                }
                            } else {
                                Toast.makeText(PayActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(PayActivity.this, LoginActivity.class);
                                PayActivity.this.finish();
                            }
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
            case R.id.pay_coupon_status:
                Intent intent = new Intent();
                intent.putExtra("productId", productId);
                intent.putExtra("amount", String.valueOf(payAmount));
                intent.putExtra("selectedPosition", selectedPosition);
                intent.setClass(PayActivity.this, PayCouponActivity.class);
                startActivityForResult(intent, 12);
                break;
            case R.id.pay_zc_icon:
                clickBalance();
                break;
            case R.id.pay_zc_amount:
                clickBalance();
                break;
            case R.id.pay_bank_layout:
                break;
            case R.id.pay_add_bank_layout:
                IntentUtils.startActivity(PayActivity.this, MyBankCardActivity.class);
                break;
            case R.id.pay_btn:
                payOrder();
                break;
            case R.id.pay_instruction1:
                Bundle b2 = new Bundle();
                b2.putString("url", "http://m.azhongzhi.com/client/info/cashoutinfo.htm");
                b2.putString("title", "投资服务协议");
                IntentUtils.startActivity(PayActivity.this, CommonH5Activity.class, b2);
                break;
            case R.id.pay_instruction2:
                Bundle b3 = new Bundle();
                b3.putString("url", "http://m.azhongzhi.com/client/info/networkinvestrisk.htm");
                b3.putString("title", "网络借贷风险和禁止性行为");
                IntentUtils.startActivity(PayActivity.this, CommonH5Activity.class, b3);
                break;
            default:
                break;
        }
    }

    /**
     * 计算金额
     */
    private void calculateAmount() {
        balanceAmount = originalBalanceAmount;
        if (isUseCoupon) {
            if(couponAmount >= payAmount){   //如果优惠金额比总金额大，则优惠金额设置成总金额
                couponAmount = payAmount;
            }
            long tem = payAmount - couponAmount;
            if(tem < balanceAmount){
                balanceAmount = tem;
            }else {
                balanceAmount = originalBalanceAmount;
            }
            if(isUseBalance){  //使用优惠券,使用余额
                bankAmount = payAmount - couponAmount - balanceAmount;
            }else {            //使用优惠券,不使用余额
                bankAmount = payAmount - couponAmount;
            }
        } else {
            if(isUseBalance){  //不使用优惠券,使用余额
                bankAmount = payAmount - balanceAmount;
            }else {            //不使用优惠券,不使用余额
                bankAmount = payAmount;
            }
        }
        String amountStr = "可支付\n" + String.valueOf(balanceAmount) + "元";
        balanceAmountTV.setText(amountStr);
        bankAmountTV.setText(String.valueOf(bankAmount) + "元");
    }

    /**
     * 点击使用余额
     */
    private void clickBalance() {
        if (isUseBalance) {
            isUseBalance = false;
            balanceIV.setImageDrawable(getResources().getDrawable(R.mipmap.checked_false));
        } else {
            isUseBalance = true;
            balanceIV.setImageDrawable(getResources().getDrawable(R.mipmap.checked_true));
        }
        calculateAmount();
    }

    /**
     * 加载支付数据
     */
    private void loadData() {
        Logs.i("productId---" + productId + "---payAmount---" + payAmount);
        OkHttpUtils.post().url(Urls.GET_MONEY)
                .addCommonHeaderAndBody()
                .addParams("product", productId)
                .addParams("money", String.valueOf(payAmount))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("Point onError---" + e.getMessage());

                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            balanceAmount = response.optInt("cash", 0);
                            originalBalanceAmount = response.optInt("cash", 0);
                            isCoupon = response.optInt("is_coupon", 0);
                            JSONObject bank = response.optJSONObject("bank");
                            if (bank != null) {
                                int bankId = bank.optInt("id", 0);
                                if (bankId == 0) {
                                    bankLayout.setVisibility(View.GONE);
                                    addbankLayout.setVisibility(View.VISIBLE);
                                } else {
                                    bankLayout.setVisibility(View.VISIBLE);
                                    addbankLayout.setVisibility(View.GONE);
                                    Glide.with(PayActivity.this).load(bank.optString("logo"))
                                            .into(bankIconIV);
                                    bankNameTV.setText(bank.optString("name"));
                                }
                            }
                            String amountStr = "可支付\n" + String.valueOf(balanceAmount) + "元";
                            balanceAmountTV.setText(amountStr);

                            if (isCoupon == 1) {
                                couponTV.setText("有可用优惠券");
                                couponIV.setVisibility(View.VISIBLE);
                                couponTV.setOnClickListener(PayActivity.this);
                                couponTV.setClickable(true);
                            } else {
                                couponTV.setText("无可用优惠券");
                                couponIV.setVisibility(View.GONE);
                                couponTV.setClickable(false);
                            }

                            bankAmountTV.setText(String.valueOf(payAmount) + "元");

                            calculateAmount();
                        }
                    }
                });
    }

    private CustomDialog verifyCodeDialog;
    private View verifyCodeLayout;
    private EditText codeET;

    /**
     * 显示验证码输入框
     */
    private void showVerifyCodeDialog() {
        if(verifyCodeDialog == null){
            verifyCodeLayout = getLayoutInflater().inflate(R.layout.verify_code_view, null);
            codeET = (EditText) verifyCodeLayout.findViewById(R.id.add_bankcard_name);
            TextView hintTV = (TextView) verifyCodeLayout.findViewById(R.id.add_bankcard_hint);
            hintTV.setText("您的手机将收到国付宝发送的短信验证码，请输入验证码完成支付");
            verifyCodeDialog = new CustomDialog.Builder(PayActivity.this)
                    .setContentView(verifyCodeLayout)
                    .setNegativeBtn(getResources().getString(R.string.cancel), new OnButtonClickedListener() {
                        @Override
                        public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveBtn(getResources().getString(R.string.confirm), new OnButtonClickedListener() {
                        @Override
                        public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
                            if(TextUtils.isEmpty(codeET.getText().toString())){
                                Toast.makeText(PayActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                            } else {
                                confirmPay();
                            }

                        }
                    }).build();
        }
        verifyCodeDialog.show();
    }

    /**
     * 确认付款
     */
    private void confirmPay() {
        OkHttpUtils.post().url(Urls.PAY_CONFIRM)
                .addCommonHeaderAndBody()
                .addParams("smsCode", codeET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("PAY_CONFIRM onResponse---" + response);
                        Toast.makeText(PayActivity.this, "支付成功！", Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", Urls.FINANCIAL_PRODUCT_SUCCESS);
                        IntentUtils.startActivity(PayActivity.this, BoughtSuccessActivity.class, bundle);
                        PayActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == 13) {
            if (data != null) {
                couponId = data.getIntExtra("couponId", 0);
                couponName = data.getStringExtra("couponName");
                Logs.i("couponAmount---" + data.getLongExtra("couponAmount", 0L));
                couponAmount = data.getLongExtra("couponAmount", 0L);
                selectedPosition = data.getIntExtra("selectedPosition", -1);
                if (couponId == 0) {
                    isUseCoupon = false;
                } else {
                    isUseCoupon = true;
                }
                if(couponId != 0){
                    couponTV.setText(couponName);
                }else {
                    couponTV.setText("有可用优惠券");
                }
                calculateAmount();
            }
        }
    }
}
