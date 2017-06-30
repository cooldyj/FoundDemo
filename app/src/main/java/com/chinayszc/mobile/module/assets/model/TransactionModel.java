package com.chinayszc.mobile.module.assets.model;

/**
 * 交易记录Model
 * Created by Jerry on 2017/4/3.
 */

public class TransactionModel {
    /**
     * create_time : 2017-04-06 16:09:34
     * id : 2
     * money : 500
     * product_name : sss
     * state : 购买成功
     */

    private String create_time;
    private int id;
    private int money;
    private String product_name;
    private String state;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
