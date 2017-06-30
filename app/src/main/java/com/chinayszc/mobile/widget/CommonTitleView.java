package com.chinayszc.mobile.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinayszc.mobile.R;

/**
 * 通用Title控件
 * Created by Jerry on 2017/3/29.
 */

public class CommonTitleView extends LinearLayout {

    private ImageView backIV;
    private TextView titleTV;
    private TextView actionTV;
    private ImageView iconTV;

    public CommonTitleView(Context context) {
        super(context);
        initView(context);
    }

    public CommonTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        ((Activity)context).getLayoutInflater().inflate(R.layout.app_custom_title, this);
        backIV = (ImageView) findViewById(R.id.app_title_back);
        titleTV = (TextView) findViewById(R.id.app_title_text);
        actionTV = (TextView) findViewById(R.id.app_title_action);
        iconTV = (ImageView) findViewById(R.id.app_title_icon);
    }

    public ImageView getBackIV() {
        return backIV;
    }

    public TextView getTitleTV() {
        return titleTV;
    }

    public void setTitle(String title) {
        if(!TextUtils.isEmpty(title)){
            this.titleTV.setText(title);
        }
    }

    public TextView getActionTV() {
        return actionTV;
    }

    public ImageView getIconTV() {
        return iconTV;
    }
}
