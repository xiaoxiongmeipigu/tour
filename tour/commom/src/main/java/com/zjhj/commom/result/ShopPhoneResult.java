package com.zjhj.commom.result;

/**
 * Created by brain on 2017/5/20.
 */
public class ShopPhoneResult extends MapiBaseResult{

    private String tel;
    private String mobile;

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
}
