package com.chinayszc.mobile.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 全局Content Provider
 * Created by Jerry on 2017/4/16.
 */

public class MyProvider extends ContentProvider {

    private SQLiteDatabase db;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContentProviderMetaData.AUTHORITY,
                PersonalInfoDB.PersonalInfoTB.TABLE_NAME,
                ContentProviderMetaData.PERSONAL_INFO_TABLE_RETURN);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case ContentProviderMetaData.PERSONAL_INFO_TABLE_RETURN:
                cursor = db.query(PersonalInfoDB.PersonalInfoTB.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri iUri = null;
        long rowId;
        switch (uriMatcher.match(uri)) {
            case ContentProviderMetaData.PERSONAL_INFO_TABLE_RETURN:
                rowId = db.insert(PersonalInfoDB.PersonalInfoTB.TABLE_NAME, null, values);
                if (rowId > 0) {
                    iUri = ContentUris.withAppendedId(PersonalInfoDB.PersonalInfoTB.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(iUri, null);
                }
                break;
            default:
                break;
        }
        return iUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ContentProviderMetaData.PERSONAL_INFO_TABLE_RETURN:
                count = db.delete(PersonalInfoDB.PersonalInfoTB.TABLE_NAME, selection, selectionArgs);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ContentProviderMetaData.PERSONAL_INFO_TABLE_RETURN:
                count = db.update(PersonalInfoDB.PersonalInfoTB.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                break;
        }

        return count;
    }
}
