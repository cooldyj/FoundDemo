package com.chinayszc.mobile.database;

/**
 * 调用数据库删除回调接口
 * Created by Jerry on 2017/1/13.
 */

public interface OnDeleteListener {

    void onDeleteSuccess();

    void onDeleteFailed();

}
