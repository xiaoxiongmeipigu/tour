package com.zjhj.commom.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.widget.MainToast;

/**
 * Created by brain on 2017/6/1.
 */
public class LocationUtil {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private MyLocationListener mMyLocationListener;

    public LocationUtil(Context context) {
        super();
        //初始化client
        locationClient = new AMapLocationClient(AppContext.getInstance());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        mMyLocationListener = new MyLocationListener();
        // 设置定位监听
        locationClient.setLocationListener(mMyLocationListener);
    }

    /**
     * 开始定位
     */
    public void startLoc(){
        Log.i("LocationUtil","开始定位");
        locationClient.startLocation();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    public void stopLoc(){
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    public void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(aMapLocation.getErrorCode() == 0){
                    DebugLog.i("定位成功");
//                    MainToast.showShortToast("定位成功");
                    if(null!=locationListener)
                        locationListener.localInfo(aMapLocation);
                }else {
                    if(null!=locationListener)
                        locationListener.localFail();
                    MainToast.showShortToast("定位失败,请重新定位");
                }
            }else {
                if(null!=locationListener)
                    locationListener.localFail();
                MainToast.showShortToast("定位失败,请重新定位");
            }
        }
    }

    private OnLocationListener locationListener;

    public interface OnLocationListener{
         void localInfo(AMapLocation location);
         void localFail();
    }

    public void setOnReceivedMessageListener(OnLocationListener locationListener){
        this.locationListener = locationListener;
    }

}
