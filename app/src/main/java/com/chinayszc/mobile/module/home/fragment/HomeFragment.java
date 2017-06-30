package com.chinayszc.mobile.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.AboutActivity;
import com.chinayszc.mobile.module.home.activity.CommonH5Activity;
import com.chinayszc.mobile.module.home.activity.CouponActivity;
import com.chinayszc.mobile.module.home.activity.FinacialProductDetailActivity;
import com.chinayszc.mobile.module.home.activity.FinancialProductActivity;
import com.chinayszc.mobile.module.home.activity.InviteFriendsActivity;
import com.chinayszc.mobile.module.home.activity.NewGuideActivity;
import com.chinayszc.mobile.module.home.activity.ProductDetailActivity;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.module.home.model.GalleryModel;
import com.chinayszc.mobile.module.home.model.HomeProductModel;
import com.chinayszc.mobile.module.home.view.GlideLoader;
import com.chinayszc.mobile.module.home.activity.PointMallActivity;
import com.chinayszc.mobile.module.main.MainNavigationActivity;
import com.chinayszc.mobile.module.me.activity.RegisterActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.GsonCallBack;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.DecimalUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 主页Fragment
 * Created by Jerry on 2017/3/25.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private Banner topGallery;
    private Banner middleGallery;
    private List<String> topUrls;
    private List<String> middleUrls;
    private List<GalleryModel> topList;
    private List<GalleryModel> middleList;
    private TextView prodNameTV;
    private TextView profitTV;
    private TextView regularTV;
    private int prodId;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_fragment, null);
        Logs.d("HomeFragment--onCreateView");
        initView();
        loadTopGallery();
        loadMiddleGallery();
        loadProduct();
        return mView;
    }

    private void initView() {
        topUrls = new ArrayList<>();
        middleUrls = new ArrayList<>();

        TextView buyBtn = (TextView) mView.findViewById(R.id.home_buy_btn);
        LinearLayout buyLL = (LinearLayout) mView.findViewById(R.id.home_one_product_layout);
        topGallery = (Banner) mView.findViewById(R.id.home_top_gallery);
        middleGallery = (Banner) mView.findViewById(R.id.home_middle_gallery);
        LinearLayout pointBtn = (LinearLayout) mView.findViewById(R.id.home_point_layout);
        LinearLayout couponBtn = (LinearLayout) mView.findViewById(R.id.home_coupon_layout);
        LinearLayout inviteBtn = (LinearLayout) mView.findViewById(R.id.home_invite_layout);
//        TextView checkInBtn = (TextView) mView.findViewById(R.id.home_check_in);
        LinearLayout checkInBtn = (LinearLayout) mView.findViewById(R.id.home_check_in_layout);
        TextView moreProductBtn = (TextView) mView.findViewById(R.id.home_more_product);
        TextView newGuideBtn = (TextView) mView.findViewById(R.id.home_new_guide);
        TextView aboutYsBtn = (TextView) mView.findViewById(R.id.home_about_ys);

        prodNameTV = (TextView) mView.findViewById(R.id.home_new_discount);
        profitTV = (TextView) mView.findViewById(R.id.home_percent);
        regularTV = (TextView) mView.findViewById(R.id.home_regular);

        pointBtn.setOnClickListener(this);
        couponBtn.setOnClickListener(this);
        inviteBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        buyLL.setOnClickListener(this);
        checkInBtn.setOnClickListener(this);
        moreProductBtn.setOnClickListener(this);
        newGuideBtn.setOnClickListener(this);
        aboutYsBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_point_layout:
                IntentUtils.startActivity(getActivity(), PointMallActivity.class);
//                Intent intent = new Intent();
//                ComponentName componentName = new ComponentName("com.jerry.payment.mobile",
//                        "com.jerry.payment.mobile.module.discover.FeedBackActivity");
//                intent.setComponent(componentName);
//                getActivity().startActivity(intent);
                break;
            case R.id.home_coupon_layout:
                IntentUtils.startActivity(getActivity(), CouponActivity.class);
                break;
            case R.id.home_invite_layout:
                Bundle b2 = new Bundle();
                if(Env.isLoggedIn && !TextUtils.isEmpty(Env.token)){
                    b2.putString("url", "http://m.azhongzhi.com/client/friend.htm" + "?" + "token=" + Env.token);
                } else {
                    b2.putString("url", "http://m.azhongzhi.com/client/friend.htm" + "?" + "token=144711D33E25F926124B8C89671E74FE");
                }
//                b2.putString("url", "http://m.azhongzhi.com/client/friend.htm");
                b2.putString("title", "邀请好友");
                b2.putBoolean("isShowIcon", true);
                b2.putString("nextTitle", "邀请有奖");
                b2.putString("nextUrl", "http://m.azhongzhi.com/client/info/inviteinfo.htm");
                IntentUtils.startActivity(getActivity(), CommonH5Activity.class, b2);
//                IntentUtils.startActivity(getActivity(), InviteFriendsActivity.class);
                break;
            case R.id.home_buy_btn:
                Bundle b1 = new Bundle();
                b1.putInt("prodId", prodId);
                b1.putInt("from", 0);
                IntentUtils.startActivity(getActivity(), FinancialProductActivity.class, b1);
                break;
            case R.id.home_one_product_layout:
                Bundle b0 = new Bundle();
                b0.putInt("prodId", prodId);
                b0.putInt("from", 0);
                IntentUtils.startActivity(getActivity(), FinancialProductActivity.class, b0);
                break;
            case R.id.home_check_in_layout:
                Bundle b3 = new Bundle();
                if(Env.isLoggedIn && !TextUtils.isEmpty(Env.token)){
                    b3.putString("url", "http://m.azhongzhi.com/client/sign.htm" + "?" + "token=" + Env.token);
                } else {
                    b3.putString("url", "http://m.azhongzhi.com/client/sign.htm" + "?" + "token=144711D33E25F926124B8C89671E74FE");
                }
//                b3.putString("url", "http://m.azhongzhi.com/client/sign.htm");
                b3.putString("title", "签到");
                b3.putBoolean("isShowIcon", true);
                b3.putString("nextTitle", "用户签到");
                b3.putString("nextUrl", "http://m.azhongzhi.com/client/info/signinfo.htm");
                IntentUtils.startActivity(getActivity(), CommonH5Activity.class, b3);
                break;
            case R.id.home_more_product:
                ((MainNavigationActivity) getActivity()).jumpToTab(R.id.navigation_product);
                break;
            case R.id.home_new_guide:
                IntentUtils.startActivity(getActivity(), NewGuideActivity.class);
                break;
            case R.id.home_about_ys:
                IntentUtils.startActivity(getActivity(), AboutActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转逻辑
     */
    private void jump(GalleryModel model) {
        if (model != null) {
            GalleryModel.ObjBean objBean = model.getObj();
            if (objBean != null) {
                String type = objBean.getType();
                switch (type) {
                    case "shop":  //积分商城产品
                        Bundle bundle0 = new Bundle();
                        bundle0.putInt("id", objBean.getId());
                        IntentUtils.startActivity(getActivity(), ProductDetailActivity.class, bundle0);
                        break;
                    case "product":  //理财产品
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("prodId", objBean.getId());
                        bundle1.putInt("from", 0);
                        IntentUtils.startActivity(getActivity(), FinancialProductActivity.class, bundle1);
                        break;
                    case "register":  //注册页
                        IntentUtils.startActivity(getActivity(), RegisterActivity.class);
                        break;
                    case "web":  //web活动页
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("url", objBean.getUrl());
                        IntentUtils.startActivity(getActivity(), CommonH5Activity.class, bundle2);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 初始化顶部画廊
     */
    private void initTopGallery() {
        topGallery.setImageLoader(new GlideLoader());
        topGallery.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        topGallery.setIndicatorGravity(BannerConfig.CENTER);
        topGallery.setBannerAnimation(Transformer.Default);
        topGallery.setImages(topUrls);

        topGallery.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                Logs.d("OnBannerClick--" + i);
                if (topList != null && topList.size() > i) {
                    GalleryModel model = topList.get(i);
                    jump(model);
                }
            }
        });

        topGallery.start();
    }

    /**
     * 初始化中部画廊
     */
    private void initMiddleGallery() {
        middleGallery.setImageLoader(new GlideLoader());
        middleGallery.setImages(middleUrls);

        middleGallery.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                Logs.d("OnBannerClick--" + i);
                if (middleList != null && middleList.size() > i) {
                    GalleryModel model = middleList.get(i);
                    jump(model);
                }
            }
        });

        middleGallery.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        topGallery.startAutoPlay();
        middleGallery.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        topGallery.stopAutoPlay();
        middleGallery.stopAutoPlay();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 加载顶部轮播图
     */
    private void loadTopGallery() {
        OkHttpUtils.post().url(Urls.HOME_IMG_LIST)
                .addCommonHeaderAndBody()
                .addParams("item", "1")
                .build()
                .execute(new StringArrayCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i("onResponse--" + response);
                        if (!TextUtils.isEmpty(response)) {
                            topList = ParseJason.convertToList(response, GalleryModel.class);
                            if (!topUrls.isEmpty()) {
                                topUrls.clear();
                            }
                            if (topList != null && topList.size() > 0) {
                                for (int i = 0; i < topList.size(); i++) {
                                    topUrls.add(topList.get(i).getImg_thumb_path());
                                }
                                Logs.i("initTopGallery--" + topList.size());
                                initTopGallery();
                            }
                        }
                    }
                });
    }

    /**
     * 加载中部轮播图
     */
    private void loadMiddleGallery() {
        OkHttpUtils.post().url(Urls.HOME_IMG_LIST)
                .addCommonHeaderAndBody()
                .addParams("item", "2")
                .build()
                .execute(new StringArrayCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i("onResponse--" + response);
                        if (!TextUtils.isEmpty(response)) {
                            middleList = ParseJason.convertToList(response, GalleryModel.class);
                            if (!middleUrls.isEmpty()) {
                                middleUrls.clear();
                            }
                            if (middleList != null && middleList.size() > 0) {
                                for (int i = 0; i < middleList.size(); i++) {
                                    middleUrls.add(middleList.get(i).getImg_thumb_path());
                                }
                                Logs.i("initMiddleGallery--" + middleList.size());
                                initMiddleGallery();
                            }
                        }
                    }
                });
    }

    /**
     * 加载推荐产品
     */
    private void loadProduct() {
        OkHttpUtils.post().url(Urls.HOME_PRODUCT)
                .addCommonHeaderAndBody()
                .build()
                .execute(new GsonCallBack<HomeProductModel>(HomeProductModel.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(HomeProductModel model, int id) {
                        if (model != null) {
                            String profit = DecimalUtils.get2DecimalStr(model.getProfit()) + "%";
                            String regular = "定期" + model.getDay_no() + "天";
                            prodNameTV.setText(model.getProduct_name());
                            profitTV.setText(profit);
                            regularTV.setText(regular);
                            prodId = model.getId();
                        }

                    }
                });
    }

}
