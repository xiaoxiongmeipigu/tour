package com.zjhj.tour.activity.destine;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiBankResult;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiTasteResult;
import com.zjhj.commom.util.DateUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.view.PurcaseSheetLayout;
import com.zjhj.tour.widget.DinnerTiemDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DestineActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.purcaseSheetLayout)
    PurcaseSheetLayout purcaseSheetLayout;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    TimePickerView pvTime;
    @Bind(R.id.old_price)
    TextView oldPrice;
    @Bind(R.id.set_meal_name)
    TextView setMealName;
    @Bind(R.id.original_single_price)
    TextView originalSinglePrice;
    @Bind(R.id.discount_rate)
    TextView discountRate;
    @Bind(R.id.new_price)
    TextView newPrice;
    @Bind(R.id.mobile)
    TextView mobile;
    @Bind(R.id.taste_tv)
    TextView tasteTv;
    @Bind(R.id.remark_et)
    EditText remarkEt;

    String id = "";
    String price;
    String discountRateStr = "";

    DinnerTiemDialog dinnerTiemDialog;

    List<MapiResourceResult> timeList;

    private String dinner_id = "";

    List<MapiCarResult> list;

    String phoneStr = "";

    private List<MapiResourceResult> dateList;

    OptionsPickerView positionOptions;
    ArrayList<MapiTasteResult> posOptions1Items = new ArrayList<>();

    List<MapiTasteResult> tastes;
    String options1Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destine);
        ButterKnife.bind(this);
        if (null != getIntent())
            id = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(id)) {
            initView();
            initListener();
            load();
        }
    }

    private void initView() {
        list = new ArrayList<>();
        dateList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("立即预订");
        setMealName.setFocusable(true);
        setMealName.setFocusableInTouchMode(true);
        timeList = new ArrayList<>();
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);//要在setTime 之前才有效果哦
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                dateTv.setText(DateUtil.getInstance().date2YMD_H(date));
            }

        });

        dateTv.setText(DateUtil.getInstance().date2YMD_H(new Date()));

        purcaseSheetLayout.setCountEdit(false);

        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        dinnerTiemDialog = new DinnerTiemDialog(this, R.style.image_dialog_theme);

        if (null != userSP.getUserBean()) {
            phoneStr = userSP.getUserBean().getMobile();

            if (!TextUtils.isEmpty(phoneStr) && StringUtil.isMobile(phoneStr)) {
                mobile.setText(StringUtil.settingphone(phoneStr));
            }
        }

        //选项选择器
        positionOptions = new OptionsPickerView(this);

    }

    private void initListener() {
        purcaseSheetLayout.setNumListener(new PurcaseSheetLayout.NumInterface() {
            @Override
            public void modify(View view, int num, String price) {
                notifyPrice();
            }
        });
        dinnerTiemDialog.setDialogItemClickListner(new DinnerTiemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int type) {
                dinner_id = timeList.get(type).getId();
                MapiResourceResult mapiResourceResult = timeList.get(type);
                timeTv.setText(mapiResourceResult.getUse_begin_time() + "-" + mapiResourceResult.getUse_end_time());
            }
        });

        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                options1Str = posOptions1Items.get(options1).getPickerViewText();
                tasteTv.setText(TextUtils.isEmpty(options1Str)?"":options1Str);
            }
        });

    }

    private void load() {

        showLoading();
        ItemApi.orderpreview(this, id, "", new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                List<MapiFoodResult> list = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("set_meal").toJSONString(), MapiFoodResult.class);
                String discount_rate = success.getJSONObject("data").getString("discount_rate");
                List<MapiResourceResult> times = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("use_time").toJSONString(), MapiResourceResult.class);
                tastes = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("taste").toJSONString(),MapiTasteResult.class);
                dateList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("no_open_date").toJSONString(), MapiResourceResult.class);
                if (TextUtils.isEmpty(discount_rate) || "10".equals(discount_rate)) {
                    discountRateStr = "10";
                    discountRate.setVisibility(View.GONE);
                    oldPrice.setVisibility(View.GONE);
                } else {
                    discountRateStr = discount_rate;
                    discountRate.setText(discountRateStr + "折");
                    discountRate.setVisibility(View.VISIBLE);
                    oldPrice.setVisibility(View.VISIBLE);
                    newPrice.setVisibility(View.VISIBLE);
                }

                if (null != list && !list.isEmpty()) {
                    setMealName.setText(TextUtils.isEmpty(list.get(0).getSet_meal_name()) ? "" : list.get(0).getSet_meal_name());
                    price = TextUtils.isEmpty(list.get(0).getOriginal_single_price()) ? "0" : list.get(0).getOriginal_single_price();
                    originalSinglePrice.setText(price + "元");
                    notifyPrice();
                }

                if (null != times && !times.isEmpty()) {
                    timeList.addAll(times);
                    dinnerTiemDialog.setmList(timeList);
                }

                if(null!=tastes&&!tastes.isEmpty()){
                    for(MapiTasteResult tasteResult : tastes){
                        posOptions1Items.add(tasteResult);
                    }
                    //三级联动效果
                    positionOptions.setPicker(posOptions1Items);
                    //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
                    positionOptions.setCyclic(false);
                    //设置默认选中的三级项目
                    //监听确定选择按钮
                    positionOptions.setSelectOptions(0);
                }

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void notifyPrice() {

        if (!TextUtils.isEmpty(price)) {
            double priceDouble = Double.parseDouble(price);
            double oldPriceDouble = priceDouble * purcaseSheetLayout.getNum();
            double discountRateDouble = Double.parseDouble(discountRateStr);
            double newPriceDouble = oldPriceDouble * discountRateDouble / 10;

            oldPrice.setText("原价：¥" + StringUtil.numberFormat(oldPriceDouble));
            newPrice.setText("¥" + StringUtil.numberFormat(newPriceDouble));

        }

    }

    @OnClick({R.id.back, R.id.date, R.id.time, R.id.order,R.id.taste_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.date:
                pvTime.show();
                break;
            case R.id.time:
                if (null != timeList && !timeList.isEmpty()) {
                    dinnerTiemDialog.showDialog();
                }
                break;
            case R.id.order:
                if(TextUtils.isEmpty(dinner_id)){
                    MainToast.showShortToast("请选择用餐时间段");
                    return;
                }
                if(TextUtils.isEmpty(options1Str)){
                    MainToast.showShortToast("请选择口味");
                    return;
                }
                if(verifyDate())
                    order();
                break;
            case R.id.taste_ll:
                if(null!=tastes&&!tastes.isEmpty()){
                    positionOptions.show();
                }
                break;
        }
    }

    private void order(){

        list.clear();
        MapiCarResult carResult = new MapiCarResult();
        carResult.setSet_meal_id(id);
        carResult.setNum(purcaseSheetLayout.getNum()+"");
        list.add(carResult);
        String jsonString = JSON.toJSONString(list);
        DebugLog.i("jsonString=>"+jsonString);

        showLoading();
        ItemApi.ordersubmit(this, jsonString, dateTv.getText().toString(), dinner_id, phoneStr,options1Str,remarkEt.getText().toString(),new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                ControllerUtil.go2OrderList();
                finish();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private boolean verifyDate(){
        if(null!=dateList){
            for(MapiResourceResult resourceResult : dateList){
                if(resourceResult.getDate().equals(dateTv.getText().toString())) {
                    MainToast.showShortToast("当天酒店预订已满，请更换日期或联系酒店");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != dinnerTiemDialog && dinnerTiemDialog.isShowing()) {
            dinnerTiemDialog.dismiss();
            dinnerTiemDialog = null;
        }
    }

}
