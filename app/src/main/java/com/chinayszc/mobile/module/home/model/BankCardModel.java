package com.chinayszc.mobile.module.home.model;

/**
 * 银行卡model
 * Created by Jerry on 2017/4/3.
 */

public class BankCardModel {

    /**
     * id : 1
     * logo : http://img.azhongzhi.com/201703/58d66f92c05a2.jpg
     * name : 中国银行
     * quota : 单笔0.01万
     * thumb_logo : http://img.azhongzhi.com/201703/58d66f92c05a2.jpg
     */

    private int id;
    private String logo;
    private String name;
    private String quota;
    private String thumb_logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getThumb_logo() {
        return thumb_logo;
    }

    public void setThumb_logo(String thumb_logo) {
        this.thumb_logo = thumb_logo;
    }
}
