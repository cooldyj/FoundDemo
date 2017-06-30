package com.chinayszc.mobile.module.me.activity;

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
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.CountDownTimerUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.MD5Utils;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 注册Activity
 * Created by Jerry on 2017/4/4.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneET;
    private EditText codeET;
    private EditText setPswET;
    private EditText inviteCodeET;
    private TextView verifyCodeTV;
    private TextView saveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.register_title);
        titleView.setTitle(getResources().getString(R.string.register));
        titleView.getBackIV().setOnClickListener(this);

        phoneET = (EditText) findViewById(R.id.register_phone_num);
        codeET = (EditText) findViewById(R.id.register_code);
        setPswET = (EditText) findViewById(R.id.register_set_psw);
        inviteCodeET = (EditText) findViewById(R.id.register_invite_code);
        verifyCodeTV = (TextView) findViewById(R.id.register_get_code);
        saveTV = (TextView) findViewById(R.id.register_btn);
        TextView agreementTV = (TextView) findViewById(R.id.register_agreement);

        verifyCodeTV.setOnClickListener(this);
        saveTV.setOnClickListener(this);
        agreementTV.setOnClickListener(this);

        codeET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.register_code_et || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.register_get_code:
                String phone = phoneET.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else {
                    getVerifyCode();
                }
                break;
            case R.id.register_btn:
                attemptRegister();
                break;
            case R.id.register_agreement:
                IntentUtils.startActivity(this, UserAgreementActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        OkHttpUtils.post().url(Urls.REGISTER_SMS)
                .addCommonHeaderAndBody()
                .addParams("mobile", phoneET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.verify_code_sent_success), Toast.LENGTH_SHORT).show();
                        CountDownTimerUtils utils = new CountDownTimerUtils(verifyCodeTV,
                                90000, 1000);
                        utils.start();
                    }
                });
    }

    /**
     * 注册
     */
    private void register() {
        String pswStr = setPswET.getText().toString();
        String encodedPsw = MD5Utils.getMD5(pswStr);

        OkHttpUtils.post().url(Urls.USER_REGISTER)
                .addCommonHeaderAndBody()
                .addParams("mobile", phoneET.getText().toString())
                .addParams("password", encodedPsw)
                .addParams("registerCode", inviteCodeET.getText().toString())
                .addParams("verifyCode", codeET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (response != null) {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();
                        }
                    }
                });
    }

    /**
     * 校验输入格式
     */
    private void attemptRegister() {
        SoftInputUtils.closedSoftInput(RegisterActivity.this);

        phoneET.setError(null);
        codeET.setError(null);
        setPswET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String phoneStr = phoneET.getText().toString();
        String codeStr = codeET.getText().toString();
        String setPswStr = setPswET.getText().toString();

        if (TextUtils.isEmpty(setPswStr)) {
            setPswET.setError(getString(R.string.psw_not_null));
            focusView = setPswET;
            cancel = true;
        }

        if (TextUtils.isEmpty(codeStr)) {
            codeET.setError(getString(R.string.code_not_null));
            focusView = codeET;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneStr)) {
            phoneET.setError(getString(R.string.phone_num_not_null));
            focusView = phoneET;
            cancel = true;
        } else if (phoneET.length() < 11) {
            phoneET.setError(getString(R.string.phone_num_should_correct));
            focusView = phoneET;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            register();
        }
    }

}
