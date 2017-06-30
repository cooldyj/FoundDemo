package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.home.fragment.PayCouponUnusedFragment;
import com.chinayszc.mobile.module.home.fragment.PayCouponUsedFragment;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.NoSlidingViewPager;

/**
 * 支付--优惠券Activity
 * Created by Jerry on 2017/4/1.
 */

public class PayCouponActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private NoSlidingViewPager mViewPager;
    private RadioButton unusedBtn, usedBtn;
    private int selectedPosition = -1;  //已选择的优惠券position

    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_coupon_activity);
        initData();
        initView();
    }

    private void initData(){
        String productId = "";
        String amount = "";
        Intent intent = getIntent();
        if(intent != null){
            productId = intent.getStringExtra("productId");
            amount = intent.getStringExtra("amount");
            selectedPosition = intent.getIntExtra("selectedPosition", -1);
        }
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("amount", amount);
        bundle.putInt("selectedPosition", selectedPosition);

        mFragments = new Fragment[]{PayCouponUnusedFragment.newInstance(bundle)
                , PayCouponUsedFragment.newInstance(bundle)
        };

    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.pay_coupon_title);
        titleView.setTitle(getResources().getString(R.string.coupon));
        titleView.getBackIV().setOnClickListener(this);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (NoSlidingViewPager) findViewById(R.id.pay_coupon_view_pager);
        mViewPager.setNoSlide(false);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);

        unusedBtn = (RadioButton) findViewById(R.id.pay_coupon_unused);
        usedBtn = (RadioButton) findViewById(R.id.pay_coupon_expired);
        unusedBtn.setOnClickListener(this);
        usedBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.pay_coupon_unused:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.pay_coupon_expired:
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    public void setTab1Name(int num){
        unusedBtn.setText("可用优惠券(" + num + ")");
    }

    public void setTab2Name(int num){
        usedBtn.setText("不可用优惠券(" + num + ")");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                if(unusedBtn != null){
                    unusedBtn.setChecked(true);
                }
                break;
            case 1:
                if(usedBtn != null){
                    usedBtn.setChecked(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
