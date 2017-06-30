package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.AddressModel;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.GsonCallBack;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

/**
 * 确认订单页
 * Created by Jerry on 2017/4/17.
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView nameTV;
    private TextView phoneTV;
    private TextView addressTV;
    private TextView prodNameTV;
    private TextView prodPointTV;
    private TextView submitTV;
    private ImageView prodIV;
    private int productId;
    private String imgUrl;
    private String proName;
    private String points;
    private int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order_activity);
        getData();
        initView();
        loadData();
    }

    private void getData(){
        if(getIntent() != null){
            productId = getIntent().getIntExtra("productId", 0);
            imgUrl = getIntent().getStringExtra("imgUrl");
            proName = getIntent().getStringExtra("proName");
            points = getIntent().getStringExtra("points");
        }
    }

    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.confirm_order_header);
        titleView.setTitle(getResources().getString(R.string.confirm_order));
        titleView.getBackIV().setOnClickListener(this);

        nameTV = (TextView) findViewById(R.id.confirm_order_name);
        phoneTV = (TextView) findViewById(R.id.confirm_order_phone);
        addressTV = (TextView) findViewById(R.id.confirm_order_address);
        prodNameTV = (TextView) findViewById(R.id.confirm_order_prod_name);
        prodPointTV = (TextView) findViewById(R.id.confirm_order_prod_points);
        prodIV = (ImageView) findViewById(R.id.confirm_order_prod_icon);
        submitTV = (TextView) findViewById(R.id.confirm_order_buy);
        RelativeLayout addressRL = (RelativeLayout) findViewById(R.id.confirm_order_add_layout);

        submitTV.setOnClickListener(this);
        addressRL.setOnClickListener(this);

        Glide.with(ConfirmOrderActivity.this).load(imgUrl)
                .placeholder(R.mipmap.home_place_holder)
                .bitmapTransform(new RoundedCornersTransformation(ConfirmOrderActivity.this,
                        10, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(prodIV);
        prodNameTV.setText(proName);
        prodPointTV.setText(getString("所需积分：", points, "积分"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.confirm_order_buy:
                submitOrder();
                break;
            case R.id.confirm_order_add_layout:
                Intent intent = new Intent();
                intent.setClass(ConfirmOrderActivity.this, AddressManagementActivity.class);
                startActivityForResult(intent, 2);
                break;
            default:
                break;
        }
    }

    /**
     * 获取默认地址
     */
    private void loadData(){
        OkHttpUtils.post().url(Urls.BUY_ADDRESS)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                        setAddEmpty();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                JSONObject jo = response.optJSONObject("list");
                                if(jo != null){
                                    AddressModel addressModel = ParseJason.convertToEntity(jo.toString(), AddressModel.class);
                                    if(addressModel != null){
                                        addressId = addressModel.getId();
                                        String nameStr = "收货人：" + addressModel.getName();
                                        String addStr = "地址：" + addressModel.getAddress();
                                        nameTV.setText(nameStr);
                                        phoneTV.setText(addressModel.getPhone());
                                        addressTV.setText(addStr);
                                        phoneTV.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    setAddEmpty();
                                }
                            } else {
                                Toast.makeText(ConfirmOrderActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(ConfirmOrderActivity.this, LoginActivity.class);
                                ConfirmOrderActivity.this.finish();
                            }
                        }
                    }
                });

    }

    private void setAddEmpty(){
        nameTV.setText(getResources().getString(R.string.pls_add_address));
        phoneTV.setVisibility(View.GONE);
        addressTV.setText(getResources().getString(R.string.add_address_remarks));
    }

    /**
     * 提交订单
     */
    private void submitOrder(){
        Logs.i("product--" + productId + "---addressId---" + addressId);
        OkHttpUtils.post().url(Urls.ADD_ORDER)
                .addCommonHeaderAndBody()
                .addParams("product", String.valueOf(productId))
                .addParams("address", String.valueOf(addressId))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                        Toast.makeText(ConfirmOrderActivity.this, getResources().getString(R.string.exchange_failed) + ":" + e.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("onResponse---" + response);
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                Toast.makeText(ConfirmOrderActivity.this, getResources().getString(R.string.exchange_succefull), Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("url", Urls.POINT_MALL_SUCCESS);
                                IntentUtils.startActivity(ConfirmOrderActivity.this, BoughtSuccessActivity.class, bundle);
                                ConfirmOrderActivity.this.finish();
                            } else {
                                Toast.makeText(ConfirmOrderActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(ConfirmOrderActivity.this, LoginActivity.class);
                                ConfirmOrderActivity.this.finish();
                            }
                        }
                    }
                });
    }

    private SpannableStringBuilder getString(String text1, String text2, String text3){
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        ssBuilder.append(text1);
        ssBuilder.append(text2);
        ssBuilder.append(text3);

        //构造改变字体颜色的Span
        ForegroundColorSpan text1ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_dark));
        ForegroundColorSpan text2ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_light_red));
        ForegroundColorSpan text3ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_dark));

        ssBuilder.setSpan(text1ColorSpan, 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(text2ColorSpan, text1.length(), text1.length() + text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(text3ColorSpan, text1.length() + text2.length(), ssBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssBuilder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == 3){
            if (data != null) {
                addressId = data.getIntExtra("adressId", 0);
                String nameStr = "收货人：" + data.getStringExtra("name");
                String addStr = "地址：" + data.getStringExtra("distrcit") + data.getStringExtra("address");
                nameTV.setText(nameStr);
                phoneTV.setText(data.getStringExtra("phone"));
                addressTV.setText(addStr);
                phoneTV.setVisibility(View.VISIBLE);
            }
        }
    }

}
