package com.zjhj.commom.result;

import java.util.List;

/**
 * Created by brain on 2017/5/6.
 */
public class MapiItemResult extends MapiBaseResult{

    private String img;
    private String distance;
    private String customer_consumption;
    private String feature;
    private String discount_rate;
    private String cover_pic;
    private String is_favorite;

    private String address;
    private String longitude;
    private String latitude;
    private String score;
    private String tel;
    private String mobile;
    private String merchant_pic_count;
    private String sales_volume;
    private String scenic_spot;

    private String order_id;
    private String merchant_name;
    private String use_date;
    private String use_begin_time;
    private String use_end_time;
    private String total_price;
    private List<MapiFoodResult> order_detail_list;
    private String merchant_cover_pic;

    private String merchant_id;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_cover_pic() {
        return merchant_cover_pic;
    }

    public void setMerchant_cover_pic(String merchant_cover_pic) {
        this.merchant_cover_pic = merchant_cover_pic;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public List<MapiFoodResult> getOrder_detail_list() {
        return order_detail_list;
    }

    public void setOrder_detail_list(List<MapiFoodResult> order_detail_list) {
        this.order_detail_list = order_detail_list;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUse_begin_time() {
        return use_begin_time;
    }

    public void setUse_begin_time(String use_begin_time) {
        this.use_begin_time = use_begin_time;
    }

    public String getUse_date() {
        return use_date;
    }

    public void setUse_date(String use_date) {
        this.use_date = use_date;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getScenic_spot() {
        return scenic_spot;
    }

    public void setScenic_spot(String scenic_spot) {
        this.scenic_spot = scenic_spot;
    }

    private List<ShopServiceResult> service;
    private ShopBasicResult jdjbxx;
    private ShopPolicyResult jdzc;
    private ShopPhoneResult lxfs;
    private String remind_available;

    public String getRemind_available() {
        return remind_available;
    }

    public void setRemind_available(String remind_available) {
        this.remind_available = remind_available;
    }

    private MapiMerchantResult merchant_pic;

    public ShopBasicResult getJdjbxx() {
        return jdjbxx;
    }

    public void setJdjbxx(ShopBasicResult jdjbxx) {
        this.jdjbxx = jdjbxx;
    }

    public MapiMerchantResult getMerchant_pic() {
        return merchant_pic;
    }

    public void setMerchant_pic(MapiMerchantResult merchant_pic) {
        this.merchant_pic = merchant_pic;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public ShopPolicyResult getJdzc() {
        return jdzc;
    }

    public void setJdzc(ShopPolicyResult jdzc) {
        this.jdzc = jdzc;
    }

    public ShopPhoneResult getLxfs() {
        return lxfs;
    }

    public void setLxfs(ShopPhoneResult lxfs) {
        this.lxfs = lxfs;
    }

    public String getMerchant_pic_count() {
        return merchant_pic_count;
    }

    public void setMerchant_pic_count(String merchant_pic_count) {
        this.merchant_pic_count = merchant_pic_count;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<ShopServiceResult> getService() {
        return service;
    }

    public void setService(List<ShopServiceResult> service) {
        this.service = service;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getCustomer_consumption() {
        return customer_consumption;
    }

    public void setCustomer_consumption(String customer_consumption) {
        this.customer_consumption = customer_consumption;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
