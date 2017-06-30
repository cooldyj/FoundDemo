package com.chinayszc.mobile.module.me.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 吐个槽Activity
 * Created by Jerry on 2017/4/4.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private EditText contentET;
    private EditText contactET;
    private TextView submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        initView();
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.feedback_title);
        titleView.setTitle(getResources().getString(R.string.feed_back));
        titleView.getBackIV().setOnClickListener(this);

        contentET = (EditText) findViewById(R.id.feed_back_content);
        contactET = (EditText) findViewById(R.id.feed_back_contact);
        submitBtn = (TextView) findViewById(R.id.feed_back_submit);

        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.feed_back_submit:
                verify();
                break;
            default:
                break;
        }
    }

    /**
     * 修改
     */
    private void submit(String contentStr, String contactStr) {
        OkHttpUtils.post().url(Urls.FEED_BACK)
                .addCommonHeaderAndBody()
                .addParams("content", contentStr)
                .addParams("link", contactStr)
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        int success = response.optInt("success", 0);
                        if(success == 1){
                            Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.submit_failed), Toast.LENGTH_SHORT).show();
                        }
                        FeedbackActivity.this.finish();
                    }
                });
    }

    private void verify() {
        String contentStr = contentET.getText().toString();
        String contactStr = contentET.getText().toString();

        if(TextUtils.isEmpty(contentStr)){
            Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.please_input_content), Toast.LENGTH_SHORT).show();
            return;
        }
        submit(contentStr, contactStr);
    }
}
