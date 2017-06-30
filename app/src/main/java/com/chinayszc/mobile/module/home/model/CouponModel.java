package com.chinayszc.mobile.module.home.model;

/**
 * 优惠券model
 * Created by Jerry on 2017/4/2.
 */

public class CouponModel {

    private String category;   //优惠券类型文字显示
    private String content;    //其它说明
    private String coupon_name;   //优惠券名称
    private int coupon_type;    //优惠券类型（0：加息券；1：直减券；2：满减券）
    private float coupon_value;  //优惠券值（加息时为加息百分比，其它为优惠值）
    private String end_time;   //过期日期
    private boolean isSelected;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public float getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(float coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
