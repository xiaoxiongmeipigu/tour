package com.zjhj.commom.result;

/**
 * Created by brain on 2017/5/15.
 */
public class MapiCarResult extends MapiBaseResult{

    private boolean isSel;
    private String cart_id;
    private String num;
    private String cover_pic;
    private String total_price;
    private String original_total_price;

    private String set_meal_id;

    public String getSet_meal_id() {
        return set_meal_id;
    }

    public void setSet_meal_id(String set_meal_id) {
        this.set_meal_id = set_meal_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOriginal_total_price() {
        return original_total_price;
    }

    public void setOriginal_total_price(String original_total_price) {
        this.original_total_price = original_total_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }


    //重写hashcode和equals使得根据id来判断是否是同一个bean

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (cart_id == null ? 0 : cart_id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapiCarResult)) {
            return false;
        }
        MapiCarResult other = (MapiCarResult) obj;
        if (cart_id == null) {
            if (other.cart_id != null) {
                return false;
            }
        } else if (!cart_id.equals(other.cart_id)) {
            return false;
        }
        return true;
    }



}
