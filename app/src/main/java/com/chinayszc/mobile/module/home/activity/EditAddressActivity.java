package com.chinayszc.mobile.module.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 编辑/新增地址Activity
 * Created by Jerry on 2017/4/1.
 */

public class EditAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText addNameET;
    private EditText phoneET;
    private EditText addressET;
    private TextView districtTV;
    private TextView defaultTextTV;
    private ImageView defaultIcon;
    private int id;                  //新增时为0；大于0时为修改
    private boolean isDefault;
    private String districtId;
    private String name;
    private String phone;
    private String district;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_add_activity);
        getData();
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            isDefault = intent.getBooleanExtra("isDefault", false);
            if (id != 0) {
                name = intent.getStringExtra("name");
                phone = intent.getStringExtra("phone");
                district = intent.getStringExtra("district");
                address = intent.getStringExtra("address");
                districtId = String.valueOf(intent.getIntExtra("districtId", 0));
            }
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.edit_add_title);
        titleView.setTitle(getResources().getString(R.string.edit_add));
        titleView.getBackIV().setOnClickListener(this);

        TextView saveTV = (TextView) findViewById(R.id.edit_add_save);
        TextView deleteTV = (TextView) findViewById(R.id.edit_add_delete);
        addNameET = (EditText) findViewById(R.id.edit_add_name);
        phoneET = (EditText) findViewById(R.id.edit_add_phone);
        addressET = (EditText) findViewById(R.id.edit_add_detail);
        districtTV = (TextView) findViewById(R.id.edit_add_receiver);
        defaultTextTV = (TextView) findViewById(R.id.edit_add_default_text);
        defaultIcon = (ImageView) findViewById(R.id.edit_add_default_icon);
        LinearLayout defaultLL = (LinearLayout) findViewById(R.id.edit_add_default_layout);

        if (id == 0) {
            deleteTV.setVisibility(View.GONE);
        } else {
            deleteTV.setVisibility(View.VISIBLE);
            addNameET.setText(name);
            phoneET.setText(phone);
            districtTV.setText(district);
            addressET.setText(address);
        }

        if (isDefault) {
            defaultTextTV.setTextColor(getResources().getColor(R.color.text_bule));
            defaultIcon.setImageResource(R.mipmap.checked_true);
        }

        saveTV.setOnClickListener(this);
        deleteTV.setOnClickListener(this);
        defaultLL.setOnClickListener(this);
        districtTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.edit_add_save:
                attemptAdd();
                break;
            case R.id.edit_add_delete:
                AlertDialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("确定删除？").
                        setMessage("您确定删除该地址吗？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAdd();
                            }
                        }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).
                        create();
                alertDialog.show();
                break;
            case R.id.edit_add_default_layout:
                setIsDefault();
                break;
            case R.id.edit_add_receiver:
                selectAdd();
                break;
            default:
                break;
        }
    }

    /**
     * 保存地址
     */
    private void saveAdd() {
        OkHttpUtils.post().url(Urls.SET_ADDRESS)
                .addCommonHeaderAndBody()
                .addParams("id", String.valueOf(id))
                .addParams("areaId", districtId)
                .addParams("name", addNameET.getText().toString())
                .addParams("phone", phoneET.getText().toString())
                .addParams("address", addressET.getText().toString())
                .addParams("isDefault", String.valueOf(isDefault))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(EditAddressActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                Toast.makeText(EditAddressActivity.this,
                                        EditAddressActivity.this.getResources().getString(R.string.add_address_success), Toast.LENGTH_SHORT).show();
                                EditAddressActivity.this.finish();
                            } else {
                                Toast.makeText(EditAddressActivity.this,
                                        EditAddressActivity.this.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(EditAddressActivity.this, LoginActivity.class);
                                EditAddressActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 删除地址
     */
    private void deleteAdd() {
        OkHttpUtils.post().url(Urls.DELETE_ADD)
                .addCommonHeaderAndBody()
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(EditAddressActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                int isDelete = response.optInt("isDelete", 0);
                                if (isDelete == 1) {
                                    Toast.makeText(EditAddressActivity.this,
                                            EditAddressActivity.this.getResources().getString(R.string.delete_address_success), Toast.LENGTH_SHORT).show();
                                    EditAddressActivity.this.finish();
                                } else {
                                    Toast.makeText(EditAddressActivity.this,
                                            EditAddressActivity.this.getResources().getString(R.string.delete_address_failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(EditAddressActivity.this,
                                        EditAddressActivity.this.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(EditAddressActivity.this, LoginActivity.class);
                                EditAddressActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 选择地址
     */
    private void selectAdd() {
        Intent intent = new Intent();
        intent.setClass(EditAddressActivity.this, SelectProvinceActivity.class);
        startActivityForResult(intent, 4);
    }

    /**
     * 设置是否为默认
     */
    private void setIsDefault() {
        if (isDefault) {
            defaultTextTV.setTextColor(getResources().getColor(R.color.text_gray));
            defaultIcon.setImageResource(R.mipmap.checked_false);
        } else {
            defaultTextTV.setTextColor(getResources().getColor(R.color.text_bule));
            defaultIcon.setImageResource(R.mipmap.checked_true);
        }
        isDefault = !isDefault;
    }

    /**
     * 格式校验
     */
    private void attemptAdd() {
        SoftInputUtils.closedSoftInput(EditAddressActivity.this);

        addNameET.setError(null);
        phoneET.setError(null);
        addressET.setError(null);

        boolean cancel = false;
        View focusView = null;

        String addNameStr = addNameET.getText().toString();
        String phoneStr = phoneET.getText().toString();
        String detailStr = addressET.getText().toString();

        if (TextUtils.isEmpty(detailStr)) {
            addressET.setError(getString(R.string.add_not_null));
            focusView = addressET;
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
        if (TextUtils.isEmpty(addNameStr)) {
            addNameET.setError(getString(R.string.receiver_not_null));
            focusView = addNameET;
            cancel = true;
        }
        if (TextUtils.isEmpty(districtId)) {
            cancel = true;
        }

        if (cancel) {
            if (focusView != null) {
                focusView.requestFocus();
            } else {
                Toast.makeText(this, getResources().getString(R.string.pls_select_add), Toast.LENGTH_SHORT).show();
            }
        } else {
            saveAdd();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 5) {
            if (data != null) {
                districtId = data.getStringExtra("districtId");
                String districtName = data.getStringExtra("districtName");
                String cityName = data.getStringExtra("cityName");
                String provinceName = data.getStringExtra("provinceName");
                String address = provinceName + cityName + districtName;
                districtTV.setText(address);
            }
        }
    }
}
