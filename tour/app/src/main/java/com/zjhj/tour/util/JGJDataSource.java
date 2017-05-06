package com.zjhj.tour.util;


import com.zjhj.commom.result.MapiResourceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2016/7/29.
 */
public class JGJDataSource {


    public static final String TYPE_XIANHUO = 0x01+"";
    public static final String TYPE_XIANDAN = 0x02+"";
    public static final String TYPE_HUODONG = 0x03+"";
    public static final String TYPE_PINTUAN = 0x04+"";
    public static final String TYPE_DINGZHI = 0x05+"";
    public static final String TYPE_TUIHUO = 0x06+"";
    public static final String TYPE_VIP = 0x07+"";
    public static final String TYPE_MORE = 0x08+"";
    /**
     * 获取所长/站长功能菜单
     * @return
     */
    public static List<MapiResourceResult> getMainResource(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult(TYPE_XIANHUO,"现货"));
        list.add(new MapiResourceResult(TYPE_XIANDAN,"下单"));
        list.add(new MapiResourceResult(TYPE_PINTUAN,"拼单"));
        list.add(new MapiResourceResult(TYPE_HUODONG,"活动"));
        list.add(new MapiResourceResult(TYPE_VIP,"VIP"));
        list.add(new MapiResourceResult(TYPE_DINGZHI,"来样定制"));
        list.add(new MapiResourceResult(TYPE_TUIHUO,"售后"));
        list.add(new MapiResourceResult(TYPE_MORE,"更多"));
        return list;
    }

    /**
     * 获取头条
     * @return
     */
    public static List<MapiResourceResult> getHeadResource(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult(TYPE_XIANHUO,"星尚阁欢迎您"));
        list.add(new MapiResourceResult(TYPE_XIANDAN,"恭喜小熊成功加入会员"));
        list.add(new MapiResourceResult(TYPE_HUODONG,"恭喜大福成功加入会员"));
        list.add(new MapiResourceResult(TYPE_PINTUAN,"恭喜萌萌梅成功加入会员"));
        list.add(new MapiResourceResult(TYPE_DINGZHI,"恭喜123423424234成功加入会员"));
        list.add(new MapiResourceResult(TYPE_TUIHUO,"恭喜23423423423423成功加入会员"));
        list.add(new MapiResourceResult(TYPE_VIP,"恭喜23424234234成功加入会员"));
        list.add(new MapiResourceResult(TYPE_MORE,"恭喜东东成功加入会员"));
        return list;
    }

}
