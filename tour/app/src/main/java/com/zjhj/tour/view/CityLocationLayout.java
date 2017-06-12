package com.zjhj.tour.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.util.LocationUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.AnimationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/5/16.
 */
public class CityLocationLayout extends RelativeLayout {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.location_iv)
    ImageView locationIv;
    @Bind(R.id.location_ll)
    LinearLayout locationLl;


    private Context mContext;
    private View view;

    AnimationUtil animationUtil;

    LocationUtil locationUtil;

    BaseActivity activity;

    AMapLocation aMapLocation;

    public CityLocationLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    public CityLocationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    public CityLocationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_city_location, this);
        ButterKnife.bind(this, view);

        animationUtil = new AnimationUtil(mContext, R.anim.anim_rotate_refresh);
        locationUtil = new LocationUtil(mContext);

        locationUtil.startLoc();
        animationUtil.startAnimation(locationIv);
        locationLl.setClickable(false);

    }

    private void initListener() {
        locationUtil.setOnReceivedMessageListener(new LocationUtil.OnLocationListener() {
            @Override
            public void localInfo(AMapLocation location) {
                MainToast.showShortToast("定位成功");
                aMapLocation = location;
                name.setText(aMapLocation.getCity());
                locationLl.setClickable(true);
                animationUtil.stopAnimation(locationIv);
            }

            @Override
            public void localFail() {
                locationLl.setClickable(true);
                animationUtil.stopAnimation(locationIv);
            }
        });
    }

    public void load() {

    }

    @OnClick({R.id.name, R.id.location_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name:

                if(null!=aMapLocation){
                    if(null!=activity.userSP.getUserAddr()){
                        MapiCityItemResult itemResult = activity.userSP.getUserAddr();
                        itemResult.setLongitude(aMapLocation.getLongitude()+"");
                        itemResult.setLatitude(aMapLocation.getLatitude()+"");
                        itemResult.setCity_name(aMapLocation.getCity());
                        activity.userSP.saveUserAddr(itemResult);
                    }else{
                        MapiCityItemResult cityItemResult = new MapiCityItemResult();
                        cityItemResult.setLongitude(aMapLocation.getLongitude()+"");
                        cityItemResult.setLatitude(aMapLocation.getLatitude()+"");
                        cityItemResult.setCity_name(aMapLocation.getCity());
                        activity.userSP.saveUserAddr(cityItemResult);
                    }

                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }else
                    MainToast.showShortToast("请重新定位");

                break;
            case R.id.location_ll:
                locationUtil.startLoc();
                animationUtil.startAnimation(locationIv);
                locationLl.setClickable(false);
                break;
        }
    }
}
