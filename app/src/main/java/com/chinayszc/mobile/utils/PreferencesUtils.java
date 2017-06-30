package com.chinayszc.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference 工具类
 * Created by jerry on 2016/7/15.
 */
public class PreferencesUtils {

    public static final String APP_CONTENT = "appContent";
    public static final String SETTING_CONTENT = "settingContent";
    public static final String USER_CONTENT = "userContent";

    /**
     * 设置SharedPreferences
     * 模式有以下三种，默认都是MODE_PRIVATE
     *  MODE_PRIVATE
     *  MODE_WORLD_READABLE
     *  MODE_WORLD_WRITEABLE
     */
    public static void setPreferences(Context context, String preference, String key, boolean value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static void setPreferences(Context context, String preference, String key, int value) {
        if(context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static void setPreferences(Context context, String preference, String key, long value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public static void setPreferences(Context context, String preference, String key, String value) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static boolean getPreference(Context context, String preference, String key, boolean defaultValue) {
        if(context == null) {
            return defaultValue;
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean(key, defaultValue);
        }
    }

    public static int getPreference(Context context, String preference, String key, int defaultValue) {
        if(context == null) {
            return -1;
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getInt(key, defaultValue);
        }
    }

    public static long getPreference(Context context, String preference, String key, long defaultValue) {
        if(context == null) {
            return -1L;
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getLong(key, defaultValue);
        }
    }

    public static String getPreference(Context context, String preference, String key, String defaultValue) {
        if (context == null){
            return defaultValue;
        }else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, defaultValue);
        }
    }

    public static void clearPreference(Context context, String preference) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
