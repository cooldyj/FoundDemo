package com.chinayszc.mobile.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能滑动的ViewPager
 * Created by jerry on 2016/7/29.
 */
public class NoSlidingViewPager extends ViewPager {
    private boolean noSlide = true;

    public NoSlidingViewPager(Context context) {
        super(context);
    }

    public NoSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoSlide(boolean noSlide) {
        this.noSlide = noSlide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(noSlide){
            return false;
        }else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noSlide){
            return false;
        }else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
