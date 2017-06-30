package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.GalleryModel;
import com.chinayszc.mobile.module.home.view.GlideLoader;
import com.chinayszc.mobile.module.home.model.PointProductModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.module.me.activity.RegisterActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 积分商城
 * Created by Jerry on 2017/3/29.
 */

public class PointMallActivity extends BaseActivity implements View.OnClickListener {

    private Banner gallery;
    private List<String> galleryUrls;
    private List<GalleryModel> galleryList;
    private TextView pointsTV;
    private ListView productLV;
    private BaseAdapter adapter;
    private List<PointProductModel> productList;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_point_mall_activity);
        initView();
        initListView();

        loadProdList();
        loadTopUrl();
        loadPoint();
    }

    private void initView(){
        productList = new ArrayList<>();
        galleryUrls = new ArrayList<>();
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.point_title_bar);
        titleView.setTitle(getResources().getString(R.string.home_point_store));
        titleView.getBackIV().setOnClickListener(this);
        titleView.getIconTV().setVisibility(View.VISIBLE);
        titleView.getIconTV().setOnClickListener(this);

        View headerView = getLayoutInflater().inflate(R.layout.point_mall_list_header_view, null);
        gallery = (Banner) headerView.findViewById(R.id.point_gallery);
        pointsTV = (TextView) headerView.findViewById(R.id.point_points);
        TextView recordsTV = (TextView) headerView.findViewById(R.id.point_records);
        LinearLayout exchangeLL = (LinearLayout) headerView.findViewById(R.id.point_exchange_layout);
        LinearLayout getPointsLL = (LinearLayout) headerView.findViewById(R.id.point_get_layout);

        productLV = (ListView) findViewById(R.id.point_recyclerview);
        productLV.addHeaderView(headerView);

        pointsTV.setOnClickListener(this);
        recordsTV.setOnClickListener(this);
        exchangeLL.setOnClickListener(this);
        getPointsLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.point_points:
                Bundle bundle = new Bundle();
                bundle.putString("points", String.valueOf(points));
                IntentUtils.startActivity(PointMallActivity.this, MyPointsActivity.class, bundle);
                break;
            case R.id.point_records:
                IntentUtils.startActivity(PointMallActivity.this, ConvertRecordsActivity.class);
                break;
            case R.id.point_exchange_layout:
                IntentUtils.startActivity(PointMallActivity.this, ExchangeableActivity.class);
                break;
            case R.id.point_get_layout:
                IntentUtils.startActivity(PointMallActivity.this, GetPointsActivity.class);
                break;
            case R.id.app_title_icon:
                Bundle b2 = new Bundle();
                b2.putString("url", "http://m.azhongzhi.com/client/info/shopinfo.htm");
                b2.putString("title", "积分商城活动规则");
                IntentUtils.startActivity(PointMallActivity.this, CommonH5Activity.class, b2);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化轮播图
     */
    private void initGallery(){
        gallery.setImageLoader(new GlideLoader());
        gallery.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        gallery.setIndicatorGravity(BannerConfig.RIGHT);
        gallery.setBannerAnimation(Transformer.Default);
        gallery.setImages(galleryUrls);

        gallery.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                Logs.d("OnBannerClick--" + i);
                if(galleryList != null && galleryList.size() > i){
                    GalleryModel model = galleryList.get(i);
                    jump(model);
                }
            }
        });

        gallery.start();
    }

    private int pageSize = 1;         //当前页

    /**
     * 加载产品列表
     */
    private void loadProdList(){
        OkHttpUtils.post().url(Urls.SHOP_PRO_LIST)
                .addCommonHeaderAndBody()
                .addParams("page", "1")
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("PRODUCT_LIST onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("PRODUCT_LIST onResponse---" + response);
                        if(null != response){
                            JSONArray list = response.optJSONArray("list");
                            if(list != null){
                                List<PointProductModel> modelList = ParseJason.convertToList(list.toString(), PointProductModel.class);
                                if(null != modelList){
                                    if(!productList.isEmpty()){
                                        productList.clear();
                                    }
                                    productList.addAll(modelList);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 加载顶部轮播图
     */
    private void loadTopUrl(){
        OkHttpUtils.post().url(Urls.SHOP_IMG_LIST)
                .addCommonHeaderAndBody()
                .build()
                .execute(new StringArrayCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("SHOP_IMG_LIST onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i("SHOP_IMG_LIST onResponse---" + response);
                        if(!TextUtils.isEmpty(response)){
                            galleryList = ParseJason.convertToList(response, GalleryModel.class);
                            if(!galleryUrls.isEmpty()){
                                galleryUrls.clear();
                            }
                            if(galleryList != null && galleryList.size() > 0){
                                for (int i = 0; i < galleryList.size(); i++){
                                    galleryUrls.add(galleryList.get(i).getImg_thumb_path());
                                }
                                Logs.i("initGallery--" + galleryList.size());
                                initGallery();
                            }
                        }
                    }
                });
    }

    /**
     * 加载积分数
     */
    private void loadPoint(){
        OkHttpUtils.post().url(Urls.GET_CREDIT)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("Point onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if(null != response){
                            int isLogin = response.optInt("isLogin", 0);
                            if(isLogin == 1){
                                int poi = response.optInt("credit", 0);
                                String pointsStr = getResources().getString(R.string.points);
                                points = poi;
                                pointsTV.setText(getString(pointsStr, String.valueOf(poi)));
                            } else {
                                String pointsStr = getResources().getString(R.string.points);
                                pointsTV.setText(getString(pointsStr, "--"));
                            }
                        }
                    }
                });
    }

    private void initListView(){
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if(productList.size() < 1){
                    return 0;
                } else {
                    float count = productList.size();
                    return (int) Math.ceil(count / 2);
                }
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.point_mall_list_item2, null);
                    viewHolder = new ViewHolder();
                    viewHolder.productLayout0 = (LinearLayout) convertView.findViewById(R.id.point_item_layout0);
                    viewHolder.productImg0 = (ImageView) convertView.findViewById(R.id.point_item_img0);
                    viewHolder.productName0 = (TextView) convertView.findViewById(R.id.point_item_name0);
                    viewHolder.productPoint0 = (TextView) convertView.findViewById(R.id.point_item_point0);
                    viewHolder.productLayout1 = (LinearLayout) convertView.findViewById(R.id.point_item_layout1);
                    viewHolder.productImg1 = (ImageView) convertView.findViewById(R.id.point_item_img1);
                    viewHolder.productName1 = (TextView) convertView.findViewById(R.id.point_item_name1);
                    viewHolder.productPoint1 = (TextView) convertView.findViewById(R.id.point_item_point1);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                if(productList.size() > position * 2){ //判断item是否在数据集里
                    final PointProductModel model0 = productList.get(position * 2);
                    if(null != model0){
                        Glide.with(PointMallActivity.this).load(model0.getProduct_thumb_img())
                                .placeholder(R.mipmap.home_place_holder).into(viewHolder.productImg0);
                        viewHolder.productName0.setText(model0.getProduct_name());
                        viewHolder.productPoint0.setText(String.valueOf(model0.getCredit()));
                    }
                    if(productList.size() <= position * 2 + 1){ //判断最后一个item数据
                        viewHolder.productLayout1.setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.productLayout1.setVisibility(View.VISIBLE);
                        PointProductModel model1 = productList.get(position * 2 + 1);
                        if(null != model1){
                            Glide.with(PointMallActivity.this).load(model1.getProduct_thumb_img())
                                    .placeholder(R.mipmap.home_place_holder).into(viewHolder.productImg1);
                            viewHolder.productName1.setText(model1.getProduct_name());
                            viewHolder.productPoint1.setText(String.valueOf(model1.getCredit()));
                        }
                    }

                    viewHolder.productLayout0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(null != model0){
                                Logs.i("id--"  + model0.getId());
                                Bundle bundle0 = new Bundle();
                                bundle0.putInt("id", model0.getId());
                                IntentUtils.startActivity(PointMallActivity.this, ProductDetailActivity.class, bundle0);
                            }
                        }
                    });

                    viewHolder.productLayout1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(productList.size() > position * 2 + 1){
                                PointProductModel model = productList.get(position * 2 + 1);
                                if(null != model){
                                    Logs.i("id--"  + model.getId());
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putInt("id", model.getId());
                                    IntentUtils.startActivity(PointMallActivity.this, ProductDetailActivity.class, bundle1);
                                }
                            }
                        }
                    });
                }

                return convertView;
            }
        };
        productLV.setAdapter(adapter);
    }

    private class ViewHolder{
        LinearLayout productLayout0;
        ImageView productImg0;
        TextView productName0;
        TextView productPoint0;

        LinearLayout productLayout1;
        ImageView productImg1;
        TextView productName1;
        TextView productPoint1;
    }

    /**
     * 跳转逻辑
     */
    private void jump(GalleryModel model) {
        if(model != null){
            GalleryModel.ObjBean objBean = model.getObj();
            if(objBean != null){
                String type = objBean.getType();
                switch (type){
                    case "shop":  //积分商城产品
                        Bundle bundle0 = new Bundle();
                        bundle0.putInt("id", objBean.getId());
                        IntentUtils.startActivity(PointMallActivity.this, ProductDetailActivity.class, bundle0);
                        break;
                    case "product":  //理财产品
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("prodId", objBean.getId());
                        bundle1.putInt("from", 0);
                        IntentUtils.startActivity(PointMallActivity.this, FinancialProductActivity.class, bundle1);
                        break;
                    case "register":  //注册页
                        IntentUtils.startActivity(PointMallActivity.this, RegisterActivity.class);
                        break;
                    case "web":  //web活动页
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("url", objBean.getUrl());
                        IntentUtils.startActivity(PointMallActivity.this, CommonH5Activity.class, bundle2);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        gallery.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        gallery.stopAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private SpannableStringBuilder getString(String text1, String text2){
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        ssBuilder.append(text1);
        ssBuilder.append(text2);

        //构造改变字体颜色的Span
        ForegroundColorSpan text1ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_dark));
        ForegroundColorSpan text2ColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_light_red));

        ssBuilder.setSpan(text1ColorSpan, 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(text2ColorSpan, text1.length(), ssBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssBuilder;
    }
}
