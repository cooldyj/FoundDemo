package com.chinayszc.mobile.module.home.model;

/**
 * 地址model
 * Created by Jerry on 2017/4/18.
 */

public class AddressModel {

    /**
     * address : 河北省 邢台 威县 天河小区121号1701
     * is_default : 0
     * name : 小张
     * phone : 13100000000
     */

    private int id;
    private int district_id;
    private String district;
    private String address;
    private int is_default;
    private String name;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
