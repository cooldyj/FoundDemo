package com.chinayszc.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;

/**
 * @author zjz
 */
public class PhoneUtils {

    /**
     * 打电话
     *
     * @param context
     * @param phonenum 手机号
     */
    public static void dial(Context context, String phonenum) {
        Uri uri = Uri.parse("tel:" + phonenum);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param context
     * @param phonenum 手机号
     */
    public static void sendMsg(Context context, String phonenum) {
        Uri uri = Uri.parse("smsto:" + phonenum);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.putExtra("sms_body", "");
        intent.setType("vnd.android-dir/mms-sms");
        intent.setData(uri);
        context.startActivity(intent);
    }

}
