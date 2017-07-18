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

    public static List<MapiResourceResult> getArea(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("0","全部"));
        list.add(new MapiResourceResult("0","丽江"));
        list.add(new MapiResourceResult("0","马尔代夫"));
        list.add(new MapiResourceResult("0","九寨沟"));
        list.add(new MapiResourceResult("0","三亚"));
        list.add(new MapiResourceResult("0","普陀山"));
        list.add(new MapiResourceResult("0","乌镇"));
        list.add(new MapiResourceResult("0","九华山"));
        list.add(new MapiResourceResult("0","成都"));
        list.add(new MapiResourceResult("0","五台山"));
        list.add(new MapiResourceResult("0","凤凰古城"));

        return list;
    }

    public static List<MapiResourceResult> getType() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("0", "全部"));
        list.add(new MapiResourceResult("0", "少数名族"));
        list.add(new MapiResourceResult("0", "社会餐厅"));
        list.add(new MapiResourceResult("0", "温泉"));
        list.add(new MapiResourceResult("0", "连锁餐厅"));
        list.add(new MapiResourceResult("0", "农家乐"));
        list.add(new MapiResourceResult("0", "茶餐厅"));
        list.add(new MapiResourceResult("0", "异域风情"));
        return list;
    }

    public static List<MapiResourceResult> getSort() {
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1", "智能排序"));
        list.add(new MapiResourceResult("2", "离我最近"));
        list.add(new MapiResourceResult("3", "人气最高"));
        list.add(new MapiResourceResult("4", "好评优先"));
        list.add(new MapiResourceResult("5", "用餐最低标准"));
        return list;
    }

}
