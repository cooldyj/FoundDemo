package com.chinayszc.mobile.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.chinayszc.mobile.R;

/**
 * Created by Jerry on 2017/4/30.
 */

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView mTextView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = mTextView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setTextColor(Color.parseColor("#FFFFFF"));
        mTextView.setText(millisUntilFinished / 1000 + "秒可重发");  //设置倒计时时间
        mTextView.setBackgroundResource(R.drawable.verify_unclicked_bg); //设置按钮为灰色，这时是不能点击的
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取");
        mTextView.setTextColor(Color.parseColor("#cc8d0e"));
        mTextView.setClickable(true);//重新获得点击
        mTextView.setBackgroundResource(R.drawable.verify_code_bg);  //还原背景色
    }
}
