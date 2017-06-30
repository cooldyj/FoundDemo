package com.chinayszc.mobile.module.me.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.builder.PostFormBuilder;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.CountDownTimerUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.MD5Utils;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 忘记密码Activity
 * Created by Jerry on 2017/4/4.
 */

public class ForgetPswActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneET;
    private EditText codeET;
    private EditText newET;
    private EditText confirmET;
    private TextView verifyCodeTV;
    private TextView saveTV;
    private LinearLayout phoneLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psw_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.forget_psw_title);
        titleView.setTitle(getResources().getString(R.string.forget_psw));
        titleView.getBackIV().setOnClickListener(this);

        phoneET = (EditText) findViewById(R.id.forget_psw_num);
        codeET = (EditText) findViewById(R.id.forget_psw_code);
        newET = (EditText) findViewById(R.id.forget_psw_new);
        confirmET = (EditText) findViewById(R.id.forget_psw_confirm);
        verifyCodeTV = (TextView) findViewById(R.id.forget_psw_get_code);
        saveTV = (TextView) findViewById(R.id.forget_psw_btn);
        phoneLL = (LinearLayout) findViewById(R.id.forget_psw_num_layout);

        verifyCodeTV.setOnClickListener(this);
        saveTV.setOnClickListener(this);

        confirmET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.input_forget_psw_amount || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptSave();
                    return true;
                }
                return false;
            }
        });

        if (Env.isLoggedIn) {
            phoneLL.setVisibility(View.GONE);
        } else {
            phoneLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.forget_psw_get_code:
                String phone = phoneET.getText().toString();
                if(!Env.isLoggedIn && TextUtils.isEmpty(phone)){
                    Toast.makeText(ForgetPswActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else {
                    getVerifyCode();
                }
                break;
            case R.id.forget_psw_btn:
                attemptSave();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        PostFormBuilder formBuilder = OkHttpUtils.post().url(Urls.GET_PSW_SMS)
                .addCommonHeaderAndBody();

        if (!Env.isLoggedIn) {
            formBuilder.addParams("mobile", phoneET.getText().toString());
        }

        formBuilder.build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ForgetPswActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Toast.makeText(ForgetPswActivity.this, getResources().getString(R.string.verify_code_sent_success), Toast.LENGTH_SHORT).show();
                        CountDownTimerUtils utils = new CountDownTimerUtils(verifyCodeTV,
                                90000, 1000);
                        utils.start();
                    }
                });
    }

    /**
     * 修改
     */
    private void save(String phoneNum, String passWord, String word, String rptWord) {
        Logs.i("phoneNum---" + phoneNum
                + "---passWord---" + passWord
                + "---word---" + word);
        PostFormBuilder formBuilder = OkHttpUtils.post().url(Urls.FORGET_PSW)
                .addCommonHeaderAndBody();

        if (!Env.isLoggedIn) {
            formBuilder.addParams("mobile", phoneNum);
            Logs.i("mobile---" + phoneNum);
        }

        formBuilder.addParams("verifyCode", passWord)
                .addParams("word", word)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ForgetPswActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        int isLogin = response.optInt("isLogin", 0);
                        if (isLogin == 1) {
                            Toast.makeText(ForgetPswActivity.this, getResources().getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPswActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                            IntentUtils.startActivity(ForgetPswActivity.this, LoginActivity.class);
                        }
                        ForgetPswActivity.this.finish();
                    }
                });
    }

    /**
     * 校验输入格式
     */
    private void attemptSave() {
        SoftInputUtils.closedSoftInput(ForgetPswActivity.this);

        phoneET.setError(null);
        codeET.setError(null);
        newET.setError(null);
        confirmET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String phoneStr = phoneET.getText().toString();
        String codeStr = codeET.getText().toString();
        String newStr = newET.getText().toString();
        String confirmStr = confirmET.getText().toString();

        if (TextUtils.isEmpty(confirmStr)) {
            confirmET.setError(getString(R.string.psw_not_null));
            focusView = confirmET;
            cancel = true;
        }
        if (TextUtils.isEmpty(newStr)) {
            newET.setError(getString(R.string.psw_not_null));
            focusView = newET;
            cancel = true;
        }
        if (TextUtils.isEmpty(codeStr)) {
            codeET.setError(getString(R.string.code_not_null));
            focusView = codeET;
            cancel = true;
        }
        if (TextUtils.isEmpty(phoneStr) && !Env.isLoggedIn) {
            phoneET.setError(getString(R.string.phone_num_not_null));
            focusView = phoneET;
            cancel = true;
        }
        if (!TextUtils.isEmpty(newStr)
                & !TextUtils.isEmpty(confirmStr)
                & !newStr.equals(confirmStr)) {
            newET.setError(getString(R.string.two_different));
            focusView = newET;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            String encodednew = MD5Utils.getMD5(newStr);
            String encodedConfirm = MD5Utils.getMD5(confirmStr);
            save(phoneStr, codeStr, encodednew, encodedConfirm);
        }
    }

}
