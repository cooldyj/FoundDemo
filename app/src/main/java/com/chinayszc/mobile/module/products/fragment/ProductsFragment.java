package com.chinayszc.mobile.module.products.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.FinancialProductModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 产品列表Fragment
 * Created by Jerry on 2017/3/25.
 */

public class ProductsFragment extends BaseFragment {

    private ViewPager viewPager;                          //view pager
    private List<FinancialProductModel> productList;      //产品集合
    private MyFragmentPagerAdapter fragmentAdapter;
    private FrameLayout layout;

    public static ProductsFragment newInstance() {
        Bundle args = new Bundle();
        ProductsFragment productsFragment = new ProductsFragment();
        productsFragment.setArguments(args);
        return productsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logs.d("ProductsFragment--onCreateView");
        productList = new ArrayList<>();
        View mView = inflater.inflate(R.layout.products_fragment, null);
        viewPager = (ViewPager) mView.findViewById(R.id.product_view_pager);

        viewPager.setPageTransformer(true, new GalleryTransformation());
        int pagerWidth = (int) (Env.screenWidth * 3.0f / 5.0f);
        ViewGroup.LayoutParams lp = viewPager.getLayoutParams();

        int pagerHeight = Env.screenHeight / 2;

        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, pagerHeight);
        } else {
            lp.width = pagerWidth;
            lp.height = pagerHeight;
        }
        viewPager.setLayoutParams(lp);
        viewPager.setPageMargin(-50);

        layout = (FrameLayout) mView.findViewById(R.id.product_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
//        layout.setBackgroundColor(getResources().getColor(R.color.product_bg));

//        fragmentAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager());
//        viewPager.setAdapter(fragmentAdapter);
//        viewPager.setOffscreenPageLimit(3);

        loadProdList();
        return mView;
    }

    /**
     * 加载产品列表
     */
    private void loadProdList() {
        OkHttpUtils.post().url(Urls.PRODUCT_LIST)
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
                        if (null != response) {
                            String bgColor = response.optString("bgColor");
                            JSONArray ja = response.optJSONArray("list");
                            if(ja != null){
                                List<FinancialProductModel> modelList = ParseJason.convertToList(ja.toString(), FinancialProductModel.class);
                                if (null != modelList) {
                                    if (!productList.isEmpty()) {
                                        productList.clear();
                                    }
                                    productList.addAll(modelList);

                                    fragmentAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager());
                                    viewPager.setAdapter(fragmentAdapter);
                                    viewPager.setOffscreenPageLimit(3);

                                    fragmentAdapter.notifyDataSetChanged();
                                }
                            }
                            if(!TextUtils.isEmpty(bgColor)){
                                layout.setBackgroundColor(Color.parseColor(bgColor));
                            }
                        }
                    }
                });
    }

    /**
     * Fragment PagerAdapter
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FinancialProductModel model = productList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("proModel", model);
            return ProductItemFragment.newInstance(bundle);
        }

        @Override
        public int getCount() {
            return productList.size();
        }
    }

    /**
     * Viewpager切换效果
     */
    private class GalleryTransformation implements ViewPager.PageTransformer {

        private final float MIN_SCALE = 0.85f;

        @Override
        public void transformPage(View page, float position) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float rotate = 20 * Math.abs(position);
            if (position < -1) {

            } else if (position < 0) {
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(rotate);
            } else if (position >= 0 && position < 1) {
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(-rotate);
            } else if (position >= 1) {
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setRotationY(-rotate);
            }
        }
    }
}
