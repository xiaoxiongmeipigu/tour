package com.zjhj.commom.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2017/5/20.
 */
public class MapiMerchantResult extends MapiBaseResult{

    private ArrayList<MapiResourceResult> type_0;
    private ArrayList<MapiResourceResult> type_1;
    private ArrayList<MapiResourceResult> type_2;

    public ArrayList<MapiResourceResult> getType_0() {
        return type_0;
    }

    public void setType_0(ArrayList<MapiResourceResult> type_0) {
        this.type_0 = type_0;
    }

    public ArrayList<MapiResourceResult> getType_1() {
        return type_1;
    }

    public void setType_1(ArrayList<MapiResourceResult> type_1) {
        this.type_1 = type_1;
    }

    public ArrayList<MapiResourceResult> getType_2() {
        return type_2;
    }

    public void setType_2(ArrayList<MapiResourceResult> type_2) {
        this.type_2 = type_2;
    }
}
