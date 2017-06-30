package com.chinayszc.mobile.module.common;

/**
 * 通用系统环境变量
 * Created by jerry on 2016/7/6.
 */
public class Env {

    /**
     * 应用名，当前APP的包名
     */
    public static String packageName;
    /**
     * 版本号，对应于Manifest文件里的versionCode
     */
    public static int versionCode;
    /**
     * 用作显示的版本号，对应于Manifest文件里的versionName
     */
    public static String versionName;
    /**
     * 设备token
     */
    public static String deviceToken;
    /**
     * 是否是第一次进入App
     */
    public static boolean isFirstIn;

    /**
     * SD卡根目录名
     */
    public static String sdName = "ys";

    /**
     * 协议头
     */
    public static String schema = "ys";

    /**
     * 屏幕宽度（像素）
     */
    public static int screenWidth;
    /**
     * 屏幕高度（像素）
     */
    public static int screenHeight;
    /**
     * 状态栏高度 (像素）
     */
    public static int statusBarHeight;
    /**
     * 屏幕密度
     */
    public static float density;

    /**
     * 主页标题高度
     */
    public static int titleHeight;
    /**
     * 主页Tab高度
     */
    public static int tabHeight;
    /**
     * 是否已登录
     */
    public static boolean isLoggedIn;
    /**
     * 用户Token
     */
    public static String token;

}
