package com.zjhj.commom.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2017/5/20.
 */
public class MapiDiscussResult extends MapiBaseResult{

    private String guide_id;
    private String avatar;
    private String addtime;
    private String score;
    private String content;
    private ArrayList<MapiResourceResult> pic;
    private String reply;
    private String merchant_id;
    private String merchant_name;
    private String merchant_cover_pic;
    private String feature;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getMerchant_cover_pic() {
        return merchant_cover_pic;
    }

    public void setMerchant_cover_pic(String merchant_cover_pic) {
        this.merchant_cover_pic = merchant_cover_pic;
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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(String guide_id) {
        this.guide_id = guide_id;
    }

    public ArrayList<MapiResourceResult> getPic() {
        return pic;
    }

    public void setPic(ArrayList<MapiResourceResult> pic) {
        this.pic = pic;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
