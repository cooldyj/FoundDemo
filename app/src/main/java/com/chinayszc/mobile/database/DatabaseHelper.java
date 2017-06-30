package com.chinayszc.mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库生产，升级类
 * Created by Jerry on 2017/4/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * Notes:基本构造方法
     */
    public DatabaseHelper(Context context) {
        super(context, ContentProviderMetaData.DATABASE_NAME,
                null, ContentProviderMetaData.DB_VERSION);
    }

    /**
     * Notes:更新版本构造方法
     */
    public DatabaseHelper(Context context, int version) {
        super(context, ContentProviderMetaData.DATABASE_NAME,
                null, version);
    }

    /**
     * Notes:基本构造方法
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPersonalInfoTB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            onCreate(db);
        }
    }

    /**
     * 添加个人信息Table
     */
    private void createPersonalInfoTB(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append(" create table if not exists ");
        builder.append(PersonalInfoDB.PersonalInfoTB.TABLE_NAME + " ");
        builder.append(" ( ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_ID);
        builder.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT);
        builder.append(" TEXT UNIQUE, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_PSW);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_TOKEN);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_GENDER);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_NAME);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_BIRTHDAY);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_PHONE_NUM);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_ADDRE_1);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.PERSONAL_ADDRE_2);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.EXP1);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.EXP2);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.EXP3);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.EXP4);
        builder.append(" text, ");
        builder.append(PersonalInfoDB.PersonalInfoTB.EXP5);
        builder.append(" text );");
        db.execSQL(builder.toString());
    }

}
