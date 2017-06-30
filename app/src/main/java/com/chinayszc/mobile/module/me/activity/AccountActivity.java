package com.chinayszc.mobile.module.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.PreferencesUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 账户Activity
 * Created by Jerry on 2017/4/4.
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.account_title);
        titleView.setTitle(getResources().getString(R.string.account));
        titleView.getBackIV().setOnClickListener(this);

        FrameLayout modifyBtn = (FrameLayout) findViewById(R.id.account_modify_psw);
        FrameLayout forgetBtn = (FrameLayout) findViewById(R.id.account_forget_psw);

        TextView accounTV = (TextView) findViewById(R.id.account_account);

        if(Env.isLoggedIn){
            String userAccount = PreferencesUtils.getPreference(this,
                    PreferencesUtils.USER_CONTENT, "userAccount", "");
            accounTV.setText("当前登录：" + userAccount);
            accounTV.setVisibility(View.VISIBLE);
        } else {
            accounTV.setVisibility(View.GONE);
        }

        modifyBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.account_modify_psw:
                if(Env.isLoggedIn){
                    IntentUtils.startActivity(AccountActivity.this, ModifyPswActivity.class);
                }else {
                    Toast.makeText(AccountActivity.this, "您还未登录，请登录后修改密码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.account_forget_psw:
                IntentUtils.startActivity(AccountActivity.this, ForgetPswActivity.class);
                break;
            default:
                break;
        }
    }

}
