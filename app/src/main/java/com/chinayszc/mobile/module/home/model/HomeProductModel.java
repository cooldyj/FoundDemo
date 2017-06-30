package com.chinayszc.mobile.module.home.model;

/**
 * 推荐产品Model
 * Created by Jerry on 2017/4/15.
 */

public class HomeProductModel {

    /**
     * day_no : 12
     * id : 2
     * product_name : 新手专享
     * profit : 12
     */

    private int day_no;
    private int id;
    private String product_name;
    private float profit;

    public int getDay_no() {
        return day_no;
    }

    public void setDay_no(int day_no) {
        this.day_no = day_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }
}
