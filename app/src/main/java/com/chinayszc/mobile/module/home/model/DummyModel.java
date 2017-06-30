package com.chinayszc.mobile.module.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * Dummy model
 * tobe deleted
 */

public class DummyModel {
    /**
     * name : Jerry
     * email : cooldyj@126.com
     * address : 上海市浦东新区牛顿路350号
     * phoneNumber : 18923456666
     * verification : true
     * region : 中国
     */

    private String name;
    private String email;
    private String address;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String verification;
    private String region;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
