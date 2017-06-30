package com.chinayszc.mobile.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.chinayszc.mobile.utils.Logs;

/**
 * 个人信息数据库操作类
 * Created by Jerry on 2017/4/16.
 */

public class PersonalInfoDBManager {

    private static PersonalInfoDBManager instance = null;
    private ContentResolver contentResolver;

    private PersonalInfoDBManager(Context context) {
        contentResolver = context.getContentResolver();
    }

    public static PersonalInfoDBManager getInstance(Context context) {
        if (instance == null) {
            instance = new PersonalInfoDBManager(context);
        }
        return instance;
    }

    /**
     * 添加个人信息数据
     */
    public void addPersonalInfo(ContentValues contentValues, OnInsertListener onInsertListener) {
        try {
            Uri uri = contentResolver.insert(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, contentValues);
            if (uri != null) {
                String rowId = uri.getLastPathSegment();
                Logs.d("addPersonalInfo rowId = " + rowId);
                if (onInsertListener != null) {
                    onInsertListener.onInsertSuccess();
                }
            } else {
                if (onInsertListener != null) {
                    onInsertListener.onInsertFailed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onInsertListener != null) {
                onInsertListener.onInsertFailed();
            }
        }
    }

    /**
     * 更新个人信息数据
     */
    public void updataPersonalInfo(String account, ContentValues contentValues, OnInsertListener onInsertListener) {
        if(isPersonalInfoExisted(account)){
            String where = PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT + " = '" + account + "'";
            contentResolver.update(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, contentValues, where, null);
        } else {
            addPersonalInfo(contentValues, onInsertListener);
        }
    }

    /**
     * 查找某条个人信息数据是否已存在
     */
    public boolean isPersonalInfoExisted(String account) {
        boolean isExisted = false;
        Cursor cursor = null;
        try {
            String selection = PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT + " = '" + account + "'";
            cursor = contentResolver.query(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, null, selection, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                isExisted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExisted;
    }

    /**
     * 查找用户Token
     */
    public String getUsrToken(String account) {
        String token = null;
        Cursor cursor = null;
        try {
            String selection = PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT + " = '" + account + "'";
            cursor = contentResolver.query(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, null, selection, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                token = cursor.getString(cursor.getColumnIndex(PersonalInfoDB.PersonalInfoTB.PERSONAL_TOKEN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return token;
    }

    /**
     * 删除指定个人信息条目
     */
    public void deletePersonalInfo(String account, OnDeleteListener onDeleteListener) {
        String deleteSelection = PersonalInfoDB.PersonalInfoTB.PERSONAL_ACCOUNT + " = '" + account + "'";
        try {
            int count = contentResolver.delete(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, deleteSelection, null);
            Logs.d("deleteGroupDetail count = " + count);
            if (onDeleteListener != null) {
                if (count > 0) {
                    onDeleteListener.onDeleteSuccess();
                } else {
                    onDeleteListener.onDeleteFailed();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (onDeleteListener != null) {
                onDeleteListener.onDeleteFailed();
            }
        }
    }
}
