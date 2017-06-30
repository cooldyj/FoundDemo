package com.chinayszc.mobile.module.home.model;

/**
 * 积分商城商品Model
 * Created by Jerry on 2017/3/30.
 */

public class PointProductModel {
    /**
     * credit : 2222
     * id : 19
     * product_img : http://my.image.ezhi.com/201704/58ec30b48e4ef.jpg
     * product_name : 2222222
     * product_thumb_img : http://my.image.ezhi.com/201704/58ec30b48e4ef_210_210.jpg
     */

    private int credit;
    private int id;
    private String product_img;
    private String product_name;
    private String product_thumb_img;
    private String content;
    private String instructions;

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_thumb_img() {
        return product_thumb_img;
    }

    public void setProduct_thumb_img(String product_thumb_img) {
        this.product_thumb_img = product_thumb_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}
