package com.zjhj.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by brain on 2017/6/24.
 */
public class MapiTasteResult extends MapiBaseResult implements IPickerViewData {
    @Override
    public String getPickerViewText() {
        return name;
    }
}
