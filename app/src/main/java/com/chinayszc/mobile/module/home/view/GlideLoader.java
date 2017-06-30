package com.chinayszc.mobile.module.home.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.youth.banner.loader.ImageLoader;

/**
 * 自定义图片加载器
 * Created by Jerry on 2017/3/29.
 */

public class GlideLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path)
                .placeholder(context.getResources().getDrawable(R.mipmap.home_place_holder))
                .error(context.getResources().getDrawable(R.mipmap.home_place_holder))
                .into(imageView);
    }
}
