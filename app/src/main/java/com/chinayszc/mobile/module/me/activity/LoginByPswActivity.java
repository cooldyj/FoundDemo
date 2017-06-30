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

import com.chinayszc.mobile.MyApplication;
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
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.MD5Utils;
import com.chinayszc.mobile.utils.PreferencesUtils;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/**
 * 密码登录Activity
 * Created by Jerry on 2017/4/4.
 */

public class LoginByPswActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneET;
    private EditText pswET;
    private TextView saveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_psw_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.login_by_psw_title);
        titleView.setTitle(getResources().getString(R.string.login));
        titleView.getBackIV().setOnClickListener(this);
        titleView.getActionTV().setText(getString(R.string.register_quickly));
        titleView.getActionTV().setVisibility(View.VISIBLE);

        phoneET = (EditText) findViewById(R.id.login_by_psw_phone_num);
        pswET = (EditText) findViewById(R.id.login_by_psw_code);
        saveTV = (TextView) findViewById(R.id.login_by_psw_btn);
        TextView forgetTV = (TextView) findViewById(R.id.login_by_psw_forget);
        TextView phoneLoginTV = (TextView) findViewById(R.id.login_by_psw_quick_login);

        saveTV.setOnClickListener(this);
        forgetTV.setOnClickListener(this);
        phoneLoginTV.setOnClickListener(this);
        titleView.getActionTV().setOnClickListener(this);

        pswET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            case R.id.login_by_psw_btn:
                attemptLogin();
                break;
            case R.id.app_title_action:
                IntentUtils.startActivity(this, RegisterActivity.class);
                break;
            case R.id.login_by_psw_forget:
                IntentUtils.startActivity(this, ForgetPswActivity.class);
                break;
            case R.id.login_by_psw_quick_login:
                IntentUtils.startActivity(this, LoginActivity.class);
                LoginByPswActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        final String accountStr = phoneET.getText().toString();
        String pswStr = pswET.getText().toString();
        String encodedPsw = MD5Utils.getMD5(pswStr);
        Logs.i("encodedPsw ==" + encodedPsw);

        OkHttpUtils.post().url(Urls.USER_LOGIN)
                .addCommonHeaderAndBody()
                .addParams("mobile", accountStr)
                .addParams("password", encodedPsw)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(LoginByPswActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject jo, int id) {
                        if (jo != null) {
                            String token = jo.optString("token", "");
                            if(!TextUtils.isEmpty(token)){
                                PreferencesUtils.setPreferences(LoginByPswActivity.this,
                                        PreferencesUtils.USER_CONTENT, "isLoggedIn", true);
                                PreferencesUtils.setPreferences(LoginByPswActivity.this,
                                        PreferencesUtils.USER_CONTENT, "userAccount", accountStr);
                                Env.isLoggedIn = true;
                                Env.token = token;

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT, accountStr);
                                contentValues.put(PersonalInfoDB.PersonalInfoTB.PERSONAL_TOKEN, token);

                                PersonalInfoDBManager.getInstance(LoginByPswActivity.this).updataPersonalInfo(accountStr, contentValues,
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

//                                if(!PersonalInfoDBManager.getInstance(LoginByPswActivity.this).isPersonalInfoExisted(accountStr)){
//                                    PersonalInfoDBManager.getInstance(LoginByPswActivity.this)
//                                            .addPersonalInfo(contentValues, new OnInsertListener() {
//                                                @Override
//                                                public void onInsertSuccess() {
//                                                    Logs.i("onInsertSuccess");
//                                                }
//
//                                                @Override
//                                                public void onInsertFailed() {
//                                                    Logs.i("onInsertFailed");
//                                                }
//                                            });
//                                }

                                Toast.makeText(LoginByPswActivity.this,
                                        getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                JPushInterface.setAlias(LoginByPswActivity.this, accountStr, null);
                                LoginByPswActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 校验输入格式
     */
    private void attemptLogin() {
        SoftInputUtils.closedSoftInput(LoginByPswActivity.this);

        phoneET.setError(null);
        pswET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String phoneStr = phoneET.getText().toString();
        String codeStr = pswET.getText().toString();

        if(TextUtils.isEmpty(codeStr)){
            pswET.setError(getString(R.string.psw_not_null));
            focusView = pswET;
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
