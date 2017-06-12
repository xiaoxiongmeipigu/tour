package com.zjhj.commom.result;

/**
 * Created by brain on 2017/5/20.
 */
public class ShopBasicResult extends MapiBaseResult{

    private String address;
    private String desc;
    private String population_max;
    private String parking_location;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParking_location() {
        return parking_location;
    }

    public void setParking_location(String parking_location) {
        this.parking_location = parking_location;
    }

    public String getPopulation_max() {
        return population_max;
    }

    public void setPopulation_max(String population_max) {
        this.population_max = population_max;
    }
}
