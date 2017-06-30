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
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.MD5Utils;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 修改密码Activity
 * Created by Jerry on 2017/4/4.
 */

public class ModifyPswActivity extends BaseActivity implements View.OnClickListener {

    private EditText currentET;
    private EditText newET;
    private EditText confirmET;
    private TextView saveTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_psw_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.modify_psw_title);
        titleView.setTitle(getResources().getString(R.string.modify_psw));
        titleView.getBackIV().setOnClickListener(this);

        currentET = (EditText) findViewById(R.id.modify_psw_current);
        newET = (EditText) findViewById(R.id.modify_psw_new);
        confirmET = (EditText) findViewById(R.id.modify_psw_confirm);
        saveTV = (TextView) findViewById(R.id.modify_psw_btn);

        saveTV.setOnClickListener(this);

        confirmET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.input_modify_psw_amount || actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptSave();
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
            case R.id.modify_psw_btn:
                attemptSave();
                break;
            default:
                break;
        }
    }

    /**
     * 修改
     */
    private void save(String passWord, String word, String rptWord) {
        OkHttpUtils.post().url(Urls.EDIT_PSW)
                .addCommonHeaderAndBody()
                .addParams("passWord", passWord)
                .addParams("word", word)
                .addParams("rptWord", rptWord)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ModifyPswActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        int isLogin = response.optInt("isLogin", 0);
                        if(isLogin == 1){
                            Toast.makeText(ModifyPswActivity.this, getResources().getString(R.string.modify_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ModifyPswActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                            IntentUtils.startActivity(ModifyPswActivity.this, LoginActivity.class);
                        }
                        ModifyPswActivity.this.finish();
                    }
                });
    }

    /**
     * 校验输入格式
     */
    private void attemptSave() {
        SoftInputUtils.closedSoftInput(ModifyPswActivity.this);

        currentET.setError(null);
        newET.setError(null);
        confirmET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String currentStr = currentET.getText().toString();
        String newStr = newET.getText().toString();
        String confirmStr = confirmET.getText().toString();

        if(TextUtils.isEmpty(confirmStr)){
            confirmET.setError(getString(R.string.psw_not_null));
            focusView = confirmET;
            cancel = true;
        }
        if(TextUtils.isEmpty(newStr)){
            newET.setError(getString(R.string.psw_not_null));
            focusView = newET;
            cancel = true;
        }
        if(TextUtils.isEmpty(currentStr)){
            currentET.setError(getString(R.string.psw_not_null));
            focusView = currentET;
            cancel = true;
        }
        if(!TextUtils.isEmpty(newStr)
                & !TextUtils.isEmpty(confirmStr)
                & !newStr.equals(confirmStr)){
            newET.setError(getString(R.string.two_different));
            focusView = newET;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            String encodedCurrent = MD5Utils.getMD5(currentStr);
            String encodednew = MD5Utils.getMD5(newStr);
            String encodedConfirm = MD5Utils.getMD5(confirmStr);
            save(encodedCurrent, encodednew, encodedConfirm);
        }
    }

}
