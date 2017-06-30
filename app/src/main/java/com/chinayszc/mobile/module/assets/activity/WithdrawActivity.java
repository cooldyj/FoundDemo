package com.chinayszc.mobile.module.assets.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.me.activity.AddBankCardActivity;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.module.me.model.MyBankCardModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.CustomDialog;
import com.chinayszc.mobile.widget.wheelview.OnButtonClickedListener;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 提现Activity
 * Created by Jerry on 2017/4/3.
 */

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {

    private EditText inputET;
    private TextView canWithdrawTV;
    private TextView bankCardTV;
    private TextView withdrawTV;
    private float withdrawAmount = 0.00f;
    private int isBank = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_activity);
        getData();
        initView();
        loadBankCard();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            String withdrawStr = intent.getStringExtra("withdrawAmount");
            try {
                withdrawAmount = Float.parseFloat(withdrawStr);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.withdraw_title);
        titleView.setTitle(getResources().getString(R.string.i_want_withdraw));
        titleView.getBackIV().setOnClickListener(this);

        inputET = (EditText) findViewById(R.id.withdraw_input);
        canWithdrawTV = (TextView) findViewById(R.id.withdraw_balance);
        bankCardTV = (TextView) findViewById(R.id.withdraw_bank_card);
        withdrawTV = (TextView) findViewById(R.id.withdraw_btn);

        withdrawTV.setOnClickListener(this);

        inputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.input_withdraw_amount || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptWithdraw();
                    return true;
                }
                return false;
            }
        });

        canWithdrawTV.setText(withdrawAmount + "元");

        bankCardTV.setOnClickListener(this);
    }

    private void attemptWithdraw() {
        SoftInputUtils.closedSoftInput(WithdrawActivity.this);
        inputET.setError(null);
        String amount = inputET.getText().toString();

        if (TextUtils.isEmpty(amount)) {
            inputET.setError(getString(R.string.please_input));
        } else {
			getVerifyCode(amount);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.withdraw_btn:
                attemptWithdraw();
                break;
            case R.id.withdraw_bank_card:
                if(isBank == 0){
                    IntentUtils.startActivity(WithdrawActivity.this, AddBankCardActivity.class);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 加载银行卡
     */
    private void loadBankCard() {
        OkHttpUtils.post().url(Urls.SHOW_CASH_IN)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WithdrawActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", -1);
                            if(isLogin == 0){
                                Toast.makeText(WithdrawActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(WithdrawActivity.this, LoginActivity.class);
                                return;
                            }

                            isBank = response.optInt("isBank", 0);
                            if(isBank == 1){    //已绑定银行卡
                                JSONObject bankCardJO = response.optJSONObject("bank");
                                if(bankCardJO != null){
                                    MyBankCardModel bankCardModel = new MyBankCardModel();
                                    bankCardModel.setAccount(bankCardJO.optString("account"));
                                    bankCardModel.setBankImg(bankCardJO.optString("bankImg"));
                                    bankCardModel.setBankName(bankCardJO.optString("bankName"));
                                    bankCardModel.setId(bankCardJO.optString("id"));
                                    bankCardTV.setText(bankCardModel.getBankName() + "(尾号" + bankCardModel.getAccount() + ")");
                                }
                            } else {
                                bankCardTV.setText("添加银行卡");
                            }
                        }
                    }
                });
    }

    /**
     * 提现
     */
    private void withdraw(String money, String verifyCode) {
        OkHttpUtils.post().url(Urls.WITHDRAW)
                .addCommonHeaderAndBody()
                .addParams("money", money)
                .addParams("verifyCode", verifyCode)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WithdrawActivity.this, "提现失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
								Toast.makeText(WithdrawActivity.this, getResources().getString(R.string.withdraw_succee), Toast.LENGTH_SHORT).show();
								WithdrawActivity.this.finish();
                            } else {
                                Toast.makeText(WithdrawActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(WithdrawActivity.this, LoginActivity.class);
                                WithdrawActivity.this.finish();
                            }
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
    private void showVerifyCodeDialog(final String money) {
        if(verifyCodeDialog == null){
            verifyCodeLayout = getLayoutInflater().inflate(R.layout.verify_code_view, null);
            codeET = (EditText) verifyCodeLayout.findViewById(R.id.add_bankcard_name);
            TextView hintTV = (TextView) verifyCodeLayout.findViewById(R.id.add_bankcard_hint);
            hintTV.setText("您的手机将收到众指网发送的短信验证码，请输入验证码完成提现");
            verifyCodeDialog = new CustomDialog.Builder(WithdrawActivity.this)
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
                                Toast.makeText(WithdrawActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                            } else {
								withdraw(money, codeET.getText().toString());
                            }

                        }
                    }).build();
        }
        verifyCodeDialog.show();
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(final String money) {
        OkHttpUtils.post().url(Urls.SMS_CASH_OUT)
                .addCommonHeaderAndBody()
                .addParams("money", money)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WithdrawActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("SMS_CASH_OUT onResponse---" + response);
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", -1);
                            if (isLogin == 0) {
                                Toast.makeText(WithdrawActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(WithdrawActivity.this, LoginActivity.class);
                                WithdrawActivity.this.finish();
                            }
                        } else {
                            showVerifyCodeDialog(money);
                        }
                    }
                });
    }

}
