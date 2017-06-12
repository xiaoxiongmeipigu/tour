package com.zjhj.commom.result;

/**
 * Created by brain on 2017/5/20.
 */
public class ShopPolicyResult extends MapiBaseResult{

    private String accept_booking_time;
    private String booking_time;
    private String children_price;
    private String pay_type;
    private String dining_time;

    public String getAccept_booking_time() {
        return accept_booking_time;
    }

    public void setAccept_booking_time(String accept_booking_time) {
        this.accept_booking_time = accept_booking_time;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getChildren_price() {
        return children_price;
    }

    public void setChildren_price(String children_price) {
        this.children_price = children_price;
    }

    public String getDining_time() {
        return dining_time;
    }

    public void setDining_time(String dining_time) {
        this.dining_time = dining_time;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
