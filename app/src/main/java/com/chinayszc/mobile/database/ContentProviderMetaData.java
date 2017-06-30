package com.chinayszc.mobile.database;

/**
 * ContentProvider常量类
 * Created by Jerry on 2017/1/12.
 */

public class ContentProviderMetaData {
    /**
     * URI的指定，此处的字符串必须和 Manifest 声明的authorities一致
     */
    public static final String AUTHORITY = "com.chinayszc.mobile";

    /**
     * 乙晟DB
     */
    public static final String DATABASE_NAME = "ys.db";

    /**
     * 默认版本
     */
    public static final int DB_VERSION = 1;

    /**
     * 个人信息 uri 匹配返回码
     */
    public static final int PERSONAL_INFO_TABLE_RETURN = 0x1;

}
