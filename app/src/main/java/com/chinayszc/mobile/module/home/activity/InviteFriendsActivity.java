package com.chinayszc.mobile.module.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.utils.BitmapUtils;
import com.chinayszc.mobile.widget.CommonTitleView;

/**
 * 邀请好友Activity
 * Created by Jerry on 2017/4/2.
 */

public class InviteFriendsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView qrImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends_activity);
        initView();
    }

    private void initView(){
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.invite_title);
        titleView.setTitle(getResources().getString(R.string.home_invite_reward));
        titleView.getBackIV().setOnClickListener(this);

        TextView moment = (TextView) findViewById(R.id.invite_moment);
        TextView wechat = (TextView) findViewById(R.id.invite_wechat);
        TextView qqZone = (TextView) findViewById(R.id.invite_qqzone);
        TextView qqFriend = (TextView) findViewById(R.id.invite_qq_friends);
        TextView sinaWeibo = (TextView) findViewById(R.id.invite_sina_weibo);
        qrImg = (ImageView) findViewById(R.id.invite_qr_img);

        moment.setOnClickListener(this);
        wechat.setOnClickListener(this);
        qqZone.setOnClickListener(this);
        qqFriend.setOnClickListener(this);
        sinaWeibo.setOnClickListener(this);

        qrImg.setImageBitmap(BitmapUtils.createQRCodeBitmap(InviteFriendsActivity.this, "http://m.chinayszc.com"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.invite_moment:
                break;
            case R.id.invite_wechat:
                break;
            case R.id.invite_qqzone:
                break;
            case R.id.invite_qq_friends:
                break;
            case R.id.invite_sina_weibo:
                break;
            default:
                break;
        }
    }
}
