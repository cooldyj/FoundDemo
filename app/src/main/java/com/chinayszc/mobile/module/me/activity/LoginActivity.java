package com.chinayszc.mobile.module.me.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.database.OnInsertListener;
import com.chinayszc.mobile.database.PersonalInfoDB;
import com.chinayszc.mobile.database.PersonalInfoDBManager;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.CountDownTimerUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.PreferencesUtils;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * 登录Activity
 * Created by Jerry on 2017/4/4.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneET;
    private EditText codeET;
    private TextView verifyCodeTV;
    private TextView saveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.login_title);
        titleView.setTitle(getResources().getString(R.string.login));
        titleView.getBackIV().setOnClickListener(this);
        titleView.getActionTV().setText(getString(R.string.register_quickly));
        titleView.getActionTV().setVisibility(View.VISIBLE);

        phoneET = (EditText) findViewById(R.id.login_phone_num);
        codeET = (EditText) findViewById(R.id.login_code);
        verifyCodeTV = (TextView) findViewById(R.id.login_get_code);
        saveTV = (TextView) findViewById(R.id.login_btn);
        TextView pswLoginTV = (TextView) findViewById(R.id.login_psw_login);

        verifyCodeTV.setOnClickListener(this);
        saveTV.setOnClickListener(this);
        pswLoginTV.setOnClickListener(this);
        titleView.getActionTV().setOnClickListener(this);

        codeET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login_code_et || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
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
            case R.id.login_get_code:
                String phone = phoneET.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else {
                    getVerifyCode();
                }
                break;
            case R.id.login_btn:
                attemptLogin();
                break;
            case R.id.app_title_action:
                IntentUtils.startActivity(this, RegisterActivity.class);
                break;
            case R.id.login_psw_login:
                IntentUtils.startActivity(this, LoginByPswActivity.class);
                LoginActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        OkHttpUtils.post().url(Urls.LOGIN_SMS)
                .addCommonHeaderAndBody()
                .addParams("mobile", phoneET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.verify_code_sent_success), Toast.LENGTH_SHORT).show();
                        CountDownTimerUtils utils = new CountDownTimerUtils(verifyCodeTV,
                                90000, 1000);
                        utils.start();
                    }
                });
    }

    /**
     * 登录
     */
    private void login() {
        final String accountStr = phoneET.getText().toString();
        OkHttpUtils.post().url(Urls.USER_LOGIN_MOBILE)
                .addCommonHeaderAndBody()
                .addParams("mobile", accountStr)
                .addParams("verifyCode", codeET.getText().toString())
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (response != null) {
                            String token = response.optString("token", "");
                            if(!TextUtils.isEmpty(token)){
                                PreferencesUtils.setPreferences(LoginActivity.this,
                                        PreferencesUtils.USER_CONTENT, "isLoggedIn", true);
                                PreferencesUtils.setPreferences(LoginActivity.this,
                                        PreferencesUtils.USER_CONTENT, "userAccount", accountStr);
                                Env.isLoggedIn = true;
                                Env.token = token;

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT, accountStr);
                                contentValues.put(PersonalInfoDB.PersonalInfoTB.PERSONAL_TOKEN, token);

                                PersonalInfoDBManager.getInstance(LoginActivity.this).updataPersonalInfo(accountStr, contentValues,
                                        new OnInsertListener() {
                                            @Override
                                            public void onInsertSuccess() {
                                                Logs.i("onInsertSuccess");
                                            }

                                            @Override
                                            public void onInsertFailed() {
                                                Logs.i("onInsertFailed");
                                            }
                                        });
                                JPushInterface.setAlias(LoginActivity.this, accountStr, null);
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                LoginActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 校验输入格式
     */
    private void attemptLogin() {
        SoftInputUtils.closedSoftInput(LoginActivity.this);

        phoneET.setError(null);
        codeET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String phoneStr = phoneET.getText().toString();
        String codeStr = codeET.getText().toString();

        if(TextUtils.isEmpty(codeStr)){
            codeET.setError(getString(R.string.code_not_null));
            focusView = codeET;
            cancel = true;
        }
        if(TextUtils.isEmpty(phoneStr)){
            phoneET.setError(getString(R.string.phone_num_not_null));
            focusView = phoneET;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login();
        }
    }

}
