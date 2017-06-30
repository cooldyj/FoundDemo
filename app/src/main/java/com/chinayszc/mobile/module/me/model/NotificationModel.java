package com.chinayszc.mobile.module.me.model;

/**
 * 消息model
 * Created by Jerry on 2017/4/4.
 */

public class NotificationModel {
    /**
     * createTime : 2017-04-24 09:27:50
     * id : 3
     * msgContent : 最新理财产品上线，优惠多多，优势多多！
     * msgId : 3
     * msgType : product
     */

    private String createTime;
    private int id;
    private String msgContent;
    private int msgId;
    private String msgType;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


//    private String notificationId;
//    private String notificationContent;
//    private String notificationDate;
//
//    public String getNotificationId() {
//        return notificationId;
//    }
//
//    public void setNotificationId(String notificationId) {
//        this.notificationId = notificationId;
//    }
//
//    public String getNotificationContent() {
//        return notificationContent;
//    }
//
//    public void setNotificationContent(String notificationContent) {
//        this.notificationContent = notificationContent;
//    }
//
//    public String getNotificationDate() {
//        return notificationDate;
//    }
//
//    public void setNotificationDate(String notificationDate) {
//        this.notificationDate = notificationDate;
//    }
}
