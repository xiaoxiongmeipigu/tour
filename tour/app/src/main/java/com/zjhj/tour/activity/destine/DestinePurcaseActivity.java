package com.zjhj.tour.activity.destine;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DateUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.destine.DestinePurcaseAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DinnerTiemDialog;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DestinePurcaseActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.old_price)
    TextView oldPrice;
    @Bind(R.id.discount_rate)
    TextView discountRate;
    @Bind(R.id.new_price)
    TextView newPrice;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.mobile)
    TextView mobile;

    TimePickerView pvTime;
    DinnerTiemDialog dinnerTiemDialog;

    String id = "";
    String phoneStr = "";
    String dinner_id = "";
    String discountRateStr = "";
    List<MapiFoodResult> mList;
    List<MapiResourceResult> dateList;
    List<MapiResourceResult> timeList;

    DestinePurcaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destine_purcase);
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
        mList = new ArrayList<>();
        dateList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("预订");
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

        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        dinnerTiemDialog = new DinnerTiemDialog(this, R.style.image_dialog_theme);

        if (null != userSP.getUserBean()) {
            phoneStr = userSP.getUserBean().getMobile();

            if (!TextUtils.isEmpty(phoneStr) && StringUtil.isMobile(phoneStr)) {
                mobile.setText(StringUtil.settingphone(phoneStr));
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DestinePurcaseAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        dinnerTiemDialog.setDialogItemClickListner(new DinnerTiemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int type) {
                dinner_id = timeList.get(type).getId();
                MapiResourceResult mapiResourceResult = timeList.get(type);
                timeTv.setText(mapiResourceResult.getUse_begin_time() + "-" + mapiResourceResult.getUse_end_time());
            }
        });
    }

    private void load() {

        showLoading();
        ItemApi.orderpreview(this, "" ,id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                List<MapiFoodResult> list = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("set_meal").toJSONString(), MapiFoodResult.class);
                String discount_rate = success.getJSONObject("data").getString("discount_rate");
                List<MapiResourceResult> times = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("use_time").toJSONString(), MapiResourceResult.class);
                dateList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("no_open_date").toJSONString(), MapiResourceResult.class);
                if (TextUtils.isEmpty(discount_rate) || "10".equals(discount_rate)) {
                    discountRate.setVisibility(View.GONE);
                    newPrice.setVisibility(View.GONE);
                } else {
                    discountRateStr = discount_rate;
                    discountRate.setText(discountRateStr + "折");
                    discountRate.setVisibility(View.VISIBLE);
                    newPrice.setVisibility(View.VISIBLE);
                }

                if (null != list && !list.isEmpty()) {
                    mList.clear();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    notifyPrice();
                }

                if (null != times && !times.isEmpty()) {
                    timeList.addAll(times);
                    dinnerTiemDialog.setmList(timeList);
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

        double allOldPrice = 0;


        for(MapiFoodResult foodResult : mList){
            String price = TextUtils.isEmpty(foodResult.getOriginal_single_price()) ? "0" : foodResult.getOriginal_single_price();
            String numStr = TextUtils.isEmpty(foodResult.getNum())?"0":foodResult.getNum();
            double priceDouble = Double.parseDouble(price);
            allOldPrice += priceDouble * Integer.parseInt(numStr);
        }
        double discountRateDouble = Double.parseDouble(discountRateStr);
        double newPriceDouble = allOldPrice * discountRateDouble / 10;
        oldPrice.setText("原价：¥" + StringUtil.numberFormat(allOldPrice));
        newPrice.setText("¥" + StringUtil.numberFormat(newPriceDouble));

    }

    @OnClick({R.id.back, R.id.date, R.id.time, R.id.order})
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
                if(verifyDate())
                    order();
                break;
        }
    }

    private void order(){

        showLoading();
        ItemApi.cartsubmit(this, getSelIds(), dateTv.getText().toString(), dinner_id, phoneStr, new RequestCallback() {
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

    private String getSelIds(){
        String rec_ids = "";
        for(MapiFoodResult itemResult:mList){
            if(TextUtils.isEmpty(rec_ids))
                rec_ids += itemResult.getCart_id();
            else
                rec_ids +=","+ itemResult.getCart_id();
        }
        DebugLog.i("rec_ids==>"+rec_ids);
        return rec_ids;
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
