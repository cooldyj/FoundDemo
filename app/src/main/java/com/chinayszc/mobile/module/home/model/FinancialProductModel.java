package com.chinayszc.mobile.module.home.model;

import java.io.Serializable;

/**
 * 金融产品Model
 * Created by Jerry on 2017/4/3.
 */

public class FinancialProductModel implements Serializable{
    /**
     * count : 10
     * day_no : 1
     * end_time : 2017-04-21
     * id : 9
     * lowest : 1
     * product_name : 新手专享
     * profit : 1.00
     * start_time : 2017-04-19
     * product_money: 123万
     */

    private String count;           //30天销售数量
    private String day_no;          //投资期限
    private String end_time;        //到期日期
    private int id;                 //id
    private String lowest;          //起购金额
    private String money_limit;          //起购金额
    private String product_name;    //产品名称
    private float profit;          //预期年化收益
    private String start_time;      //起息日期
    private String product_money;   //产品总额

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDay_no() {
        return day_no;
    }

    public void setDay_no(String day_no) {
        this.day_no = day_no;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getProduct_money() {
        return product_money;
    }

    public void setProduct_money(String product_money) {
        this.product_money = product_money;
    }

    public String getMoney_limit() {
        return money_limit;
    }

    public void setMoney_limit(String money_limit) {
        this.money_limit = money_limit;
    }
}
