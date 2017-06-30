package com.chinayszc.mobile.module.push;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.home.activity.CommonH5Activity;
import com.chinayszc.mobile.module.home.activity.FinancialProductActivity;
import com.chinayszc.mobile.module.home.activity.ProductDetailActivity;
import com.chinayszc.mobile.module.home.model.GalleryModel;
import com.chinayszc.mobile.module.me.activity.NotificationCenterActivity;
import com.chinayszc.mobile.module.me.activity.RegisterActivity;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.ParseJason;

/**
 * 推送消息显示及处理
 *
 * @author xjzhao
 */
public class JPushService {

    private static NotificationManager notificationManager;

    /**
     * 收到推送消息处理
     */
    static void onNotificationReceive(Context context, String content) {
        pushToClientMessage(context, content);
    }

    /**
     * 把消息推送到通知栏
     */
    private static void pushToClientMessage(Context context, String msgContent) {

        if (!TextUtils.isEmpty(msgContent)) {
            PushModel pushModel = ParseJason.convertToEntity(msgContent, PushModel.class);
            if (null == pushModel)
                return;
            if(null == pushModel.getMsgId())
                return;

            PendingIntent messagePendingIntent;

            Intent messageIntent = new Intent(context, NotificationCenterActivity.class);
            messageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            messageIntent.putExtra("notification", pushModel.getMsgId());
            messagePendingIntent = PendingIntent.getActivity(context,
                    Integer.parseInt(pushModel.getMsgId()), messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder messageNotification = new NotificationCompat.Builder(context);
//            String appName = context.getString(R.string.app_name);
            NotificationCompat.BigTextStyle bitTextStyle = new NotificationCompat.BigTextStyle();
            bitTextStyle.bigText(pushModel.getMsgTitle());

            messageNotification.setContentTitle(pushModel.getMsgTitle())
                    .setContentText(pushModel.getMsgContent())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(messagePendingIntent)
                    .setAutoCancel(true)
                    .setTicker(pushModel.getMsgTitle()) // 设置状态栏提示信息
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)  // 设置使用所有默认值（声音、震动）
                    .setStyle(bitTextStyle)
                    .build();

            notificationManager.notify(Integer.parseInt(pushModel.getMsgId()), messageNotification.build());

        }
    }

    /**
     * 跳转逻辑
     */
    private static Intent jump(Context context, GalleryModel model) {
        Intent intent = null;
        if(model != null){
            GalleryModel.ObjBean objBean = model.getObj();
            if(objBean != null){
                String type = objBean.getType();
                switch (type){
                    case "shop":  //积分商城产品
                        intent = new Intent();
                        intent.putExtra("id", objBean.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, ProductDetailActivity.class);
                        break;
                    case "product":  //理财产品
                        intent = new Intent();
                        intent.putExtra("prodId", objBean.getId());
                        intent.putExtra("from", 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, FinancialProductActivity.class);
                        break;
                    case "register":  //注册页
                        intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, RegisterActivity.class);
                        break;
                    case "web":  //web活动页
                        intent = new Intent();
                        intent.putExtra("url", objBean.getUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, CommonH5Activity.class);
                        break;
                    default:
                        break;
                }
            }
        }
        return intent;
    }


    /**
     * 清除通知栏消息
     */
    public static void cancelAllNotification() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    /**
     * 播放系统通知铃声
     *
     * @param context context
     */
    public static void playNotificationSound(Context context) {
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.start();
    }

    /**
     * 震动1秒
     *
     * @param context context
     */
    public static void shake1Second(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }

}
