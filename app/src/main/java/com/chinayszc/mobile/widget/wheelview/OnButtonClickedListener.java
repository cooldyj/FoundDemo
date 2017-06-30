package com.chinayszc.mobile.widget.wheelview;

import android.content.DialogInterface;

/**
 * 自定义Dialog按钮点击回调
 * Created by Jerry on 2016/12/12.
 */

public interface OnButtonClickedListener {

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param returnMsg1 The message that return to the caller
     */
    void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2);

}
