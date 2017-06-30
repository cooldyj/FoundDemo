package com.chinayszc.mobile.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.chinayszc.mobile.R;

/**
 * 改变text文字样式工具类
 * 这个类并不会被调用到,写这里是用来自定义text样式的时候过来复制粘贴样式用的<(￣︶￣)>
 * Created by jerry on 2016/7/27.
 */
final class TextSpanUtils {


    /**
     * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式
     * Spannable.SPAN_EXCLUSIVE_INCLUSIVE：前面不包括，后面包括。即仅在范围字符的后面插入新字符时会应用新样式
     * Spannable.SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括。
     * Spannable.SPAN_INCLUSIVE_INCLUSIVE：前后都包括。
     */

    private SpannableStringBuilder getString(String text1, String text2, Context context){
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        ssBuilder.append(text1);
        ssBuilder.append(text2);

        //构造改变字体颜色的Span
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);

        //构造改变字体背景颜色的Span
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.YELLOW);

        //构造改变字体大小的Span
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(16);

        //构造改变字体风格(粗体、斜体)的Span
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);

        //构造删除线span
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

        //构造下划线span
        UnderlineSpan underlineSpan = new UnderlineSpan();

        Drawable d = context.getResources().getDrawable(R.mipmap.app_tab_home);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //构造图片span,可以将文字替换
        //这个函数的不同之处在于，前几都是在原来文字的基础上加上特效，而这里却是利用图片将文字替换。
        //如果遇到不支持显示图片的函数，比如canvas绘图。就会退化成String，即以原来的String字符串来显示。
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);

        ssBuilder.setSpan(imageSpan, 1, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssBuilder;
    }
}
