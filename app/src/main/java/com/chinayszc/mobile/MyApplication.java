package com.chinayszc.mobile;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

import com.chinayszc.mobile.database.PersonalInfoDBManager;
import com.chinayszc.mobile.module.cache.CacheManager;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.utils.EasyPermissions;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.PreferencesUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Project Application
 * Created by Jerry on 2017/3/25.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initAppSdDir();
        getIsFirstIn();
        getAppVersionInfo();
        getDisplayMetrics();
        getStatusBarHeight();
        initOkHttp();
        Logger.init("Jerry");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(MyApplication.this);
        Env.deviceToken = getDeviceId();
        initUserLoginStatus();
    }

    /**
     * 初始化文件存储目录
     */
    private void initAppSdDir() {
//        CacheManager.userAvatar = new File(Environment.getExternalStorageDirectory(), Env.sdName + "/userAvatar");
        CacheManager.cacheDir = new File(Environment.getExternalStorageDirectory(), Env.sdName + "/cacheDir");
        if (Environment.getExternalStorageState().equals("mounted")) {
//            if(!CacheManager.userAvatar.exists() || !CacheManager.userAvatar.isDirectory()){
//                CacheManager.userAvatar.mkdirs();
//            }
            if (!CacheManager.cacheDir.exists() || !CacheManager.cacheDir.isDirectory()) {
                CacheManager.cacheDir.mkdirs();
            }
        }

    }

    /**
     * 获取是否初次进入App
     */
    private void getIsFirstIn() {
        Env.isFirstIn = PreferencesUtils.getPreference(MyApplication.this, PreferencesUtils.APP_CONTENT, "isFirstIn", true);
        if (Env.isFirstIn) {
            PreferencesUtils.setPreferences(MyApplication.this, PreferencesUtils.APP_CONTENT, "isFirstIn", false);
        }
    }

    /**
     * 获取版本信息
     */
    private void getAppVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            Env.packageName = packageInfo.packageName;
            Env.versionCode = packageInfo.versionCode;
            Env.versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取屏幕分辨率和密度
     */
    private void getDisplayMetrics() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int rotation = wm.getDefaultDisplay().getRotation();
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        Env.screenWidth = rotation == Surface.ROTATION_0 ? displayMetrics.widthPixels : displayMetrics.heightPixels;
        Env.screenHeight = rotation == Surface.ROTATION_0 ? displayMetrics.heightPixels : displayMetrics.widthPixels;
        Env.density = displayMetrics.density;
    }

    /**
     * 获取状态栏高度/像素
     */
    private void getStatusBarHeight() {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Env.statusBarHeight = statusBarHeight;
    }

    /**
     * 初始化OKHttp
     */
    private void initOkHttp() {
//        Cache cache= new Cache(CacheManager.cacheDir, OkHttpUtils.CACHE_SIZE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(6000L, TimeUnit.MILLISECONDS)
                .readTimeout(6000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(client);
    }

    /**
     * IMEI
     */
    public String getDeviceId() {
        // 如果之前有保存过设备id
        String deviceId = PreferencesUtils.getPreference(MyApplication.this, PreferencesUtils.APP_CONTENT, "device", "");
        if (!TextUtils.isEmpty(deviceId)) {
            Logs.i("deviceId--" + deviceId);
            return deviceId;
        }
        // 如果没有权限
        if (!EasyPermissions.hasPermissions(MyApplication.this, Manifest.permission.READ_PHONE_STATE)) {
            // 随机生成一个
            deviceId = UUID.randomUUID().toString();
            Logs.i("UUID--" + deviceId);
        } else {
            // 有权限，使用系统设备id
            TelephonyManager tm = (TelephonyManager) MyApplication.this.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            Logs.i("imei--" + deviceId);
        }
        // 保存起来吧
        PreferencesUtils.setPreferences(MyApplication.this, PreferencesUtils.APP_CONTENT, "device", deviceId);
        return deviceId;
    }

//    /**
//     * 获取设备Id
//     */
//    private String getDeviceId() {
//        StringBuilder deviceId = new StringBuilder();
//        try {
//            int permissionCheck = ContextCompat.checkSelfPermission(MyApplication.this,
//                    Manifest.permission.READ_PHONE_STATE);
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) { //未被授予权限
//                //IMEI
//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                String imei = tm.getDeviceId();
//                if (!TextUtils.isEmpty(imei)) {
//                    Logs.i("imei--" + imei);
//                    return imei;
//                }
//                //序列号（sn）
//                String sn = tm.getSimSerialNumber();
//                if (!TextUtils.isEmpty(imei)) {
//                    Logs.i("sn--" + sn);
//                    return sn;
//                }
//            }
////            //wifi mac地址
////            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
////            WifiInfo info = wifi.getConnectionInfo();
////            String wifiMac = info.getMacAddress();
////            if(!TextUtils.isEmpty(wifiMac)){
////                Logs.i("wifiMac--" + wifiMac);
////                return wifiMac;
////            }
////            //Android ID
////            String androidId = Settings.System.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
////            if(!TextUtils.isEmpty(androidId)){
////                Logs.i("androidId--" + androidId);
////                return androidId;
////            }
//            getUUID();
//        } catch (Exception e) {
//            e.printStackTrace();
//            deviceId.append(getUUID());
//        }
//        return deviceId.toString();
//    }
//
//    /**
//     * 得到全局唯一UUID
//     */
//    public String getUUID() {
//        String uuid = PreferencesUtils.getPreference(MyApplication.this,
//                PreferencesUtils.APP_CONTENT, "uuid", "");
//        if (TextUtils.isEmpty(uuid)) {
//            uuid = UUID.randomUUID().toString();
//            PreferencesUtils.setPreferences(MyApplication.this,
//                    PreferencesUtils.APP_CONTENT, "uuid", uuid);
//        }
//        Logs.i("uuid" + uuid);
//        return uuid;
//    }

    /**
     * 判断用户登录状态
     */
    public void initUserLoginStatus() {
        Env.isLoggedIn = PreferencesUtils.getPreference(this,
                PreferencesUtils.USER_CONTENT, "isLoggedIn", false);
        if (Env.isLoggedIn) {
            String userAccount = PreferencesUtils.getPreference(this,
                    PreferencesUtils.USER_CONTENT, "userAccount", "");
            String tokenStr = PersonalInfoDBManager.getInstance(this).getUsrToken(userAccount);
            if (!TextUtils.isEmpty(tokenStr)) {
                Env.token = tokenStr;
            }
        }
    }
}
