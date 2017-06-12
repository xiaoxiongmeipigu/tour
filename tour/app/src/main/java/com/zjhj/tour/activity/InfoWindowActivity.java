package com.zjhj.tour.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.LocationUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class InfoWindowActivity extends BaseActivity implements AMap.OnInfoWindowClickListener {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    private MarkerOptions markerOption;
    private AMap aMap;
    private MapView mapView;
    private Marker marker;
    private LatLng latlng;

    MapiItemResult itemResult;

    NaviLatLng mStartLatlng;
    NaviLatLng mEndLatlng;
    LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_activity);
        ButterKnife.bind(this);
        if(null!=getIntent())
            itemResult = (MapiItemResult) getIntent().getSerializableExtra("item");
        if(null!=itemResult){
            back.setImageResource(R.mipmap.back);
            tvRight.setText("导航");
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
            // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
            // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState); // 此方法必须重写
            init();
            initListener();
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        locationUtil = new LocationUtil(this);
        locationUtil.startLoc();

        String longitudeStr = TextUtils.isEmpty(itemResult.getLongitude())?"116.396481":itemResult.getLongitude();
        String latitudeStr = TextUtils.isEmpty(itemResult.getLatitude())?"39.91746":itemResult.getLatitude();
        latlng = new LatLng(Double.parseDouble(latitudeStr),Double.parseDouble(longitudeStr));
        mEndLatlng = new NaviLatLng(Double.parseDouble(latitudeStr),Double.parseDouble(longitudeStr));

        if (aMap == null) {
            aMap = mapView.getMap();
            if(null!=aMap){
                aMap.setOnInfoWindowClickListener(this);
                addMarkersToMap();// 往地图上添加marker
            }

        }
    }

    private void initListener(){
        locationUtil.setOnReceivedMessageListener(new LocationUtil.OnLocationListener() {
            @Override
            public void localInfo(AMapLocation location) {
                MainToast.showShortToast("定位成功");
                DebugLog.i("Longitude:"+location.getLongitude()+",Latitude:"+location.getLatitude());
                mStartLatlng = new NaviLatLng(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void localFail() {

            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(null!=locationUtil)
            locationUtil.stopLoc();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mapView)
            mapView.onDestroy();
        if(null!=locationUtil)
            locationUtil.destroyLocation();
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .title(TextUtils.isEmpty(itemResult.getName())?"天安门":itemResult.getName())
                .snippet(TextUtils.isEmpty(itemResult.getAddress())?"北京天安门":itemResult.getAddress())
                .draggable(true);

        marker = aMap.addMarker(markerOption);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        marker.showInfoWindow();
    }


   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            *//**
     * 清空地图上所有已经标注的marker
     *//*
            case R.id.clearMap:
                if (aMap != null) {
                    aMap.clear();
                }
                break;
            */

    /**
     * 重新标注所有的marker
     *//*
            case R.id.resetMap:
                if (aMap != null) {
                    aMap.clear();
                    addMarkersToMap();
                }
                break;
            default:
                break;
        }
    }*/
    @Override
    public void onInfoWindowClick(Marker marker) {
//        ControllerUtil.go2GPSNavi();
//        Toast.makeText(this, "您点击了Marker", Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.back, R.id.tv_right,R.id.loc_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                if(null!=mStartLatlng) {
                    ControllerUtil.go2GPSNavi(mStartLatlng, mEndLatlng);
                }else
                    MainToast.showShortToast("当前位置未获取到，请重新定位");
                break;
            case R.id.loc_btn:
                if(null!=locationUtil)
                    locationUtil.startLoc();
                break;
        }
    }

}
