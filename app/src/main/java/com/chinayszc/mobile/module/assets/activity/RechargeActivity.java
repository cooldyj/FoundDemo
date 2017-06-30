package com.chinayszc.mobile.module.assets.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.CommonH5Activity;
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
 * 充值Activity
 * Created by Jerry on 2017/4/3.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener{

    private EditText inputET;
    private TextView balanceTV;
    private TextView bankCardTV;
    private TextView rechargeTV;
    private CheckBox checkBox;
    private float balanceAmount = 0.00f;
    private int isBank = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_activity);
        getData();
        initView();
        loadBankCard();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            String balanceStr = intent.getStringExtra("balanceAmount");
            try {
                balanceAmount = Float.parseFloat(balanceStr);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.recharge_title);
        titleView.setTitle(getResources().getString(R.string.i_want_recharge));
        titleView.getBackIV().setOnClickListener(this);

        inputET = (EditText) findViewById(R.id.recharge_input);
        balanceTV = (TextView) findViewById(R.id.recharge_balance);
        bankCardTV = (TextView) findViewById(R.id.recharge_bank_card);
        rechargeTV = (TextView) findViewById(R.id.recharge_btn);
        TextView instruction1TV = (TextView) findViewById(R.id.recharge_instruction1);
        TextView instruction2TV = (TextView) findViewById(R.id.recharge_instruction2);
        checkBox = (CheckBox) findViewById(R.id.recharge_checkbox);

        rechargeTV.setOnClickListener(this);

        inputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.input_recharge_amount || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptRecharge();
                    return true;
                }
                return false;
            }
        });

        balanceTV.setText(balanceAmount + "元");

        bankCardTV.setOnClickListener(this);
        instruction1TV.setOnClickListener(this);
        instruction2TV.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rechargeTV.setEnabled(true);
                    rechargeTV.setBackground(getResources().getDrawable(R.drawable.common_btn_bg));
                } else {
                    rechargeTV.setEnabled(false);
                    rechargeTV.setBackgroundColor(getResources().getColor(R.color.btn_gray));
                }
            }
        });
    }

    private void attemptRecharge() {
        SoftInputUtils.closedSoftInput(RechargeActivity.this);
        inputET.setError(null);
        String amount = inputET.getText().toString();

        if(TextUtils.isEmpty(amount)){
            inputET.setError(getString(R.string.please_input));
        }else {
            recharge(amount);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.recharge_btn:
                attemptRecharge();
                break;
            case R.id.recharge_bank_card:
                if(isBank == 0){
                    IntentUtils.startActivity(RechargeActivity.this, AddBankCardActivity.class);
                }
                break;
            case R.id.recharge_instruction1:
                Bundle b2 = new Bundle();
                b2.putString("url", "http://m.azhongzhi.com/client/info/cashoutinfo.htm");
                b2.putString("title", "投资服务协议");
                IntentUtils.startActivity(RechargeActivity.this, CommonH5Activity.class, b2);
                break;
            case R.id.recharge_instruction2:
                Bundle b3 = new Bundle();
                b3.putString("url", "http://m.azhongzhi.com/client/info/networkinvestrisk.htm");
                b3.putString("title", "网络借贷风险和禁止性行为");
                IntentUtils.startActivity(RechargeActivity.this, CommonH5Activity.class, b3);
                break;
            default:
                break;
        }
    }

    /**
     * 加载银行卡
     */
    private void loadBankCard() {
        OkHttpUtils.post().url(Urls.CASH_OUT)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RechargeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", -1);
                            if(isLogin == 0){
                                Toast.makeText(RechargeActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(RechargeActivity.this, LoginActivity.class);
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
     * 充值
     */
    private void recharge(String money){
        OkHttpUtils.post().url(Urls.RECHARGE)
                .addCommonHeaderAndBody()
                .addParams("money", money)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RechargeActivity.this, "充值失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                showVerifyCodeDialog();
                            } else {
                                Toast.makeText(RechargeActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(RechargeActivity.this, LoginActivity.class);
                                RechargeActivity.this.finish();
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
    private void showVerifyCodeDialog() {
        if(verifyCodeDialog == null){
            verifyCodeLayout = getLayoutInflater().inflate(R.layout.verify_code_view, null);
            codeET = (EditText) verifyCodeLayout.findViewById(R.id.add_bankcard_name);
            TextView hintTV = (TextView) verifyCodeLayout.findViewById(R.id.add_bankcard_hint);
            hintTV.setText("您的手机将收到国付宝发送的短信验证码，请输入验证码完成充值");
            verifyCodeDialog = new CustomDialog.Builder(RechargeActivity.this)
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
                                Toast.makeText(RechargeActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                            } else {
                                confirmRecharge();
                            }

                        }
                    }).build();
        }
        verifyCodeDialog.show();
    }

    /**
     * 确认充值
     */
    private void confirmRecharge() {
        OkHttpUtils.post().url(Urls.CASH_IN_CONFIRM)
                .addCommonHeaderAndBody()
                .addParams("smsCode", codeET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RechargeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("PAY_CONFIRM onResponse---" + response);
                        Toast.makeText(RechargeActivity.this, getResources().getString(R.string.recharge_succee), Toast.LENGTH_SHORT).show();
                        RechargeActivity.this.finish();
                    }
                });
    }

}
