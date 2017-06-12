package com.zjhj.commom.result;

/**
 * Created by brain on 2017/5/10.
 */
public class MapiFoodResult extends MapiBaseResult{

    private String cover_pic;
    private String price;
    private String sales_volume;

    private String set_meal_id;
    private String merchant_id;
    private String merchant_name;
    private String set_meal_name;
    private String original_price;
    private String pic_url;
    private String original_single_price;
    private String single_price;
    private String original_total_price;
    private String total_price;
    private String num;
    private String cart_id;

    private String set_meal_num;
    private String order_detail_id;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getSet_meal_num() {
        return set_meal_num;
    }

    public void setSet_meal_num(String set_meal_num) {
        this.set_meal_num = set_meal_num;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOriginal_single_price() {
        return original_single_price;
    }

    public void setOriginal_single_price(String original_single_price) {
        this.original_single_price = original_single_price;
    }

    public String getOriginal_total_price() {
        return original_total_price;
    }

    public void setOriginal_total_price(String original_total_price) {
        this.original_total_price = original_total_price;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getSet_meal_id() {
        return set_meal_id;
    }

    public void setSet_meal_id(String set_meal_id) {
        this.set_meal_id = set_meal_id;
    }

    public String getSet_meal_name() {
        return set_meal_name;
    }

    public void setSet_meal_name(String set_meal_name) {
        this.set_meal_name = set_meal_name;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }
}
