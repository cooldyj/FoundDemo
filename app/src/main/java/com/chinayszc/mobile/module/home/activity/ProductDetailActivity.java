package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.GsonCallBack;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.widget.CommonTitleView;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

/**
 * 产品详情页
 * Created by Jerry on 2017/4/1.
 */

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private CommonTitleView titleView;
    private ImageView productImg;
    private TextView productPoints;
    private TextView productIntroduction;
    private TextView productInstruction;
    private TextView exchangeBtn;
    private int productId;
    private String imgUrl;
    private String proName;
    private String points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);
        initView();
        loadData();
    }

    private void initView(){
        productId = getIntent().getIntExtra("id", 0);
        titleView = (CommonTitleView) findViewById(R.id.product_detail_title);
        productImg = (ImageView) findViewById(R.id.product_detail_img);
        productPoints = (TextView) findViewById(R.id.product_detail_points);
        productIntroduction = (TextView) findViewById(R.id.product_detail_introduction);
        productInstruction = (TextView) findViewById(R.id.product_detail_instructoin);
        exchangeBtn = (TextView) findViewById(R.id.product_detail_exchange);

        titleView.getBackIV().setOnClickListener(this);
        exchangeBtn.setOnClickListener(this);
    }

    private void loadData(){
        OkHttpUtils.post().url(Urls.SHOP_PRODUCT)
                .addCommonHeaderAndBody()
                .addParams("item", String.valueOf(productId))
                .build()
                .execute(new GsonCallBack<PointProductModel>(PointProductModel.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(PointProductModel model, int id) {
                        if (model != null) {
                            titleView.setTitle(model.getProduct_name());
                            Glide.with(ProductDetailActivity.this).load(model.getProduct_thumb_img())
                                    .placeholder(R.mipmap.home_place_holder)
                                    .bitmapTransform(new RoundedCornersTransformation(ProductDetailActivity.this,
                                            10, 0, RoundedCornersTransformation.CornerType.ALL))
                                    .into(productImg);
                            productPoints.setText(getString("所需积分：", String.valueOf(model.getCredit()), "积分"));
                            productIntroduction.setText(model.getContent());
                            productInstruction.setText(model.getInstructions());

                            imgUrl = model.getProduct_thumb_img();
                            proName = model.getProduct_name();
                            points = String.valueOf(model.getCredit());
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.product_detail_exchange:
                Bundle bundle = new Bundle();
                bundle.putInt("productId", productId);
                bundle.putString("imgUrl", imgUrl);
                bundle.putString("proName", proName);
                bundle.putString("points", points);
                IntentUtils.startActivity(ProductDetailActivity.this, ConfirmOrderActivity.class, bundle);
                break;
            default:
                break;
        }
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

}
