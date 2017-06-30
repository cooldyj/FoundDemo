package com.chinayszc.mobile.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 个人信息数据库表
 * Created by Jerry on 2017/4/16.
 */

public class PersonalInfoDB {

    interface IBaseColumns extends BaseColumns {
        String CONTENT_URI_BASE = "content://" + ContentProviderMetaData.AUTHORITY + "/";
    }

    public static final class PersonalInfoTB implements IBaseColumns{
        public static final String TABLE_NAME = "personal_info_TB";
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_BASE + TABLE_NAME);

        /** 个人信息id */
        public static final String PERSONAL_ID = "personal_id";
        /** 个人账户 */
        public static final String PERSONAL_ACCOUNT = "personal_account";
        /** 个人密码 */
        public static final String PERSONAL_PSW = "personal_psw";
        /** 个人TOKEN */
        public static final String PERSONAL_TOKEN = "personal_token";
        /** 个人性别 */
        public static final String PERSONAL_GENDER = "personal_gender";
        /** 个人名字 */
        public static final String PERSONAL_NAME = "personal_name";
        /** 个人生日 */
        public static final String PERSONAL_BIRTHDAY = "personal_birthday";
        /** 个人手机 */
        public static final String PERSONAL_PHONE_NUM = "personal_phone_num";
        /** 个人地址1 */
        public static final String PERSONAL_ADDRE_1 = "personal_addre1";
        /** 个人地址2 */
        public static final String PERSONAL_ADDRE_2 = "personal_addre2";
        /** 扩展column */
        public static final String EXP1 = "exp1";
        public static final String EXP2 = "exp2";
        public static final String EXP3 = "exp3";
        public static final String EXP4 = "exp4";
        public static final String EXP5 = "exp5";
    }
}
