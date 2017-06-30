package com.chinayszc.mobile.module.push;

/**
 * 推送消息体
 * Created by Jerry on 2017/4/8.
 */

class PushModel {

    /**
     * msgId : 001
     * msgTitle : 推送标题
     * msgContent : 推送内容来啦！！！！！！！
     * type : coupon
     */

    private String msgId;
    private String msgTitle;
    private String msgContent;
    private String type;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
