package com.chinayszc.mobile.utils;

import com.orhanobut.logger.Logger;

/**
 * Log工具类, DEBUG设置开关
 * Created by jerry on 2016/7/6.
 */
public class Logs {

    private static boolean DEBUG = false;

    public static void d(String msg) {
        if (DEBUG) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Logger.e(msg);
        }
    }

    public static void json(String msg) {
        if (DEBUG) {
            Logger.json(msg);
        }
    }
}
