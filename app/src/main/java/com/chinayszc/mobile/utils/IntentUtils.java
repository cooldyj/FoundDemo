package com.chinayszc.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Intent 跳转公用类
 * Created by jerry on 2016/7/8.
 */
public class IntentUtils {

    public static void startActivity(Context context, Class<?> cls){
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class<?> cls, Bundle bundle){
        if(context != null){
            Intent intent = new Intent(context, cls);
            if(bundle != null){
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

}
