package com.chinayszc.mobile.utils;

import java.text.DecimalFormat;

/**
 * 数字运算util
 * Created by Jerry on 2017/4/30.
 */

public class DecimalUtils {

    /**
     * float转2位小数
     */
    public static String get2DecimalStr(float num){
        DecimalFormat fnum = new DecimalFormat(".00");
        return fnum.format(num);
    }

}
