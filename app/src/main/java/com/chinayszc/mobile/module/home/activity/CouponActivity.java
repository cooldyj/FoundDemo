package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.home.fragment.ExpiredFragment;
import com.chinayszc.mobile.module.home.fragment.UnusedFragment;
import com.chinayszc.mobile.module.home.fragment.UsedFragment;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.NoSlidingViewPager;

/**
 * 优惠券Activity
 * Created by Jerry on 2017/4/1.
 */

public class CouponActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private NoSlidingViewPager mViewPager;
    private RadioButton unusedBtn, usedBtn, expiredBtn;

    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_activity);
        initData();
        initView();
    }

    private void initData(){
        mFragments = new Fragment[]{UnusedFragment.newInstance()
                , UsedFragment.newInstance()
                , ExpiredFragment.newInstance()
        };

    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.coupon_title);
        titleView.setTitle(getResources().getString(R.string.home_coupon));
        titleView.getBackIV().setOnClickListener(this);
        titleView.getIconTV().setVisibility(View.VISIBLE);
        titleView.getIconTV().setOnClickListener(this);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (NoSlidingViewPager) findViewById(R.id.coupon_view_pager);
        mViewPager.setNoSlide(false);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);

        unusedBtn = (RadioButton) findViewById(R.id.coupon_unused);
        usedBtn = (RadioButton) findViewById(R.id.coupon_used);
        expiredBtn = (RadioButton) findViewById(R.id.coupon_expired);
        unusedBtn.setOnClickListener(this);
        usedBtn.setOnClickListener(this);
        expiredBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.coupon_unused:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.coupon_used:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.coupon_expired:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.app_title_icon:
                Bundle b2 = new Bundle();
                b2.putString("url", "http://m.azhongzhi.com/client/info/couponinfo.htm");
                b2.putString("title", "优惠券使用规则");
                IntentUtils.startActivity(CouponActivity.this, CommonH5Activity.class, b2);
                break;
            default:
                break;
        }
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
            case 2:
                if(expiredBtn != null){
                    expiredBtn.setChecked(true);
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
