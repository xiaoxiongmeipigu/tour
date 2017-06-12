package com.zjhj.tour.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.navi.model.NaviLatLng;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.LocationUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.MainAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.broadcast.LoginBroadcast;
import com.zjhj.tour.broadcast.ReceiverAction;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.util.JpushUtil;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bg_view)
    View bgView;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.person_iv)
    ImageView personIv;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    float total = 0f;
    List<IndexData> mList;
    MainAdapter mAdapter;

    LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.i("onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
        registerMessageReceiver();
    }

    private void initView(){

        if(null!=userSP.getUserBean()){

            if (userSP.getAlias()) {
                userSP.setAlias(false);
                JpushUtil.getInstance().setAlias("");
                JpushUtil.getInstance().stopPush(AppContext.getInstance());
            }
            JpushUtil.getInstance().verifyInit(this);
            DebugLog.i(JPushInterface.isPushStopped(AppContext.getInstance())+":服务状态");
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }

            DebugLog.i(JPushInterface.getConnectionState(AppContext.getInstance())+":连接状态");

            if (!userSP.getAlias()) {
                DebugLog.i("getAlias===>false");
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getGuide_id());
            }
        }

        bgView.setAlpha(1);
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        showLoading();
        locationUtil = new LocationUtil(this);
        locationUtil.startLoc();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DebugLog.i("onNewIntent");
        int type = intent.getIntExtra("type",0);
        if(type==3){//退出登录

            JpushUtil.getInstance().setAlias("");
            JpushUtil.getInstance().stopPush(AppContext.getInstance());

//            ControllerUtil.go2Login();
//            finish();
        }else if(type == 4){//登录
            JpushUtil.getInstance().verifyInit(this);
            if (JPushInterface.isPushStopped(AppContext.getInstance())) {
                JPushInterface.resumePush(AppContext.getInstance());
            }
            if (!userSP.getAlias()) {
                DebugLog.i("getAlias===>false");
                JpushUtil.getInstance().setAlias(userSP.getUserBean().getGuide_id());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null!=locationUtil)
            locationUtil.stopLoc();
    }

    private void initListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
               /* LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }*/

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                DebugLog.i("position:" + firstVisibleItemPosition);
                total += dy;
                if (firstVisibleItemPosition == 0) {

                    DebugLog.i("total:" + total);
                    DebugLog.i("hight:" + (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop()));
                    float alphaValue = total / (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop());
                    if (alphaValue > 1) {
                        alphaValue = 1f;
                    } else if (alphaValue < 0) {
                        alphaValue = 0;
                    }
                    bgView.setAlpha(alphaValue);
                }

            }
        });

        locationUtil.setOnReceivedMessageListener(new LocationUtil.OnLocationListener() {
            @Override
            public void localInfo(AMapLocation location) {

                if(null!=userSP.getUserAddr()){
                    MapiCityItemResult itemResult = userSP.getUserAddr();
                    itemResult.setLongitude(location.getLongitude()+"");
                    itemResult.setLatitude(location.getLatitude()+"");
                    itemResult.setCity_name(location.getCity());
                    userSP.saveUserAddr(itemResult);
                }else{
                    MapiCityItemResult cityItemResult = new MapiCityItemResult();
                    cityItemResult.setLongitude(location.getLongitude()+"");
                    cityItemResult.setLatitude(location.getLatitude()+"");
                    cityItemResult.setCity_name(location.getCity());
                    userSP.saveUserAddr(cityItemResult);
                }
                load();
            }

            @Override
            public void localFail() {
                load();
            }
        });

    }

    private void load(){

        String token = "";
        String city = "";
        String longitude = "";
        String latitude = "";

        if(null!=userSP.getUserBean()) {
            token = TextUtils.isEmpty(userSP.getUserBean().getToken()) ? "" : userSP.getUserBean().getToken();
        }

        if(null!=userSP.getUserAddr()){
            city = TextUtils.isEmpty(userSP.getUserAddr().getCity_name()) ? "" : userSP.getUserAddr().getCity_name();
            longitude = TextUtils.isEmpty(userSP.getUserAddr().getLongitude()) ? "" : userSP.getUserAddr().getLongitude();
            latitude = TextUtils.isEmpty(userSP.getUserAddr().getLatitude()) ? "" : userSP.getUserAddr().getLatitude();
        }

//        showLoading();
        ItemApi.defaultindex(this, token, city, longitude, latitude, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                List<MapiResourceResult> bannerList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("banner").toJSONString(),MapiResourceResult.class);
                List<MapiItemResult> renqiList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("renqi").toJSONString(),MapiItemResult.class);
                List<MapiItemResult> remenList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("remen").toJSONString(),MapiItemResult.class);
                MapiCityItemResult mapiCityItemResult = JSONObject.parseObject(success.getJSONObject("data").getJSONObject("location").toJSONString(),MapiCityItemResult.class);
                if(null!=mapiCityItemResult) {
                    addrTv.setText(TextUtils.isEmpty(mapiCityItemResult.getCity_name())?"":mapiCityItemResult.getCity_name());
                    userSP.saveUserAddr(mapiCityItemResult);
                }else{
                    MapiCityItemResult cityItemResult = new MapiCityItemResult();
                    cityItemResult.setCity_id("15");
                    cityItemResult.setCity_name("北京市");
                    cityItemResult.setProvince_id("14");
                    cityItemResult.setProvince_name("北京");
                    cityItemResult.setLongitude("116.3975071907");
                    cityItemResult.setLatitude("39.9087322136");
                    userSP.saveUserAddr(mapiCityItemResult);
                }
                mList.clear();
                if(null!=bannerList&&!bannerList.isEmpty())
                    mList.add(new IndexData(0, "SCROLL",bannerList));
                if(null!=renqiList&&!renqiList.isEmpty())
                    mList.add(new IndexData(1, "ITEM_HOT",renqiList));
                if(null!=remenList&&!remenList.isEmpty())
                    mList.add(new IndexData(2, "ITEM_SHOP",remenList));
                Collections.sort(mList);
                mAdapter.notifyDataSetChanged();

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    public void refreshData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        load();
    }

    @OnClick({R.id.addr_tv, R.id.person_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addr_tv:
                Intent intent = new Intent(MainActivity.this,CityActivity.class);
                startActivityForResult(intent, RequestCode.CITY);
                break;
            case R.id.person_iv:
                if(null!=userSP.getUserBean()&&!TextUtils.isEmpty(userSP.getUserBean().getToken())){
                    ControllerUtil.go2Person();
                }else
                    ControllerUtil.go2Login();
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_OK==resultCode){
            switch (requestCode){
                case RequestCode.CITY:
                    if(null!=userSP.getUserAddr()){
                        addrTv.setText(userSP.getUserAddr().getCity_name());
                        refreshData();
                    }
                    break;
            }
        }
    }

    LoginBroadcast loginBroadcast;
    public void registerMessageReceiver() {

        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ReceiverAction.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);

        loginBroadcast = new LoginBroadcast();
        IntentFilter filter2 = new IntentFilter();
        filter2.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter2.addAction(ReceiverAction.LOGIN_ACTION);
        registerReceiver(loginBroadcast, filter2);
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ReceiverAction.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(JpushUtil.KEY_MESSAGE);
                /*String extras = intent.getStringExtra(JpushUtil.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(JpushUtil.KEY_MESSAGE + " : " + messge + "\n");
                if (!JpushUtil.getInstance().isEmpty(extras)) {
                    showMsg.append(JpushUtil.KEY_EXTRAS + " : " + extras + "\n");
                }*/
                DebugLog.i("onReceive"+messge);
               /* if(!TextUtils.isEmpty(messge)){
                    JSONObject jsonObject = JSONObject.parseObject(messge);

                    String type = jsonObject.getString("type");
                    String result = jsonObject.getString("result");

                }*/

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=loginBroadcast)
            unregisterReceiver(loginBroadcast);
        if(null!=mMessageReceiver)
            unregisterReceiver(mMessageReceiver);
        if(null!=locationUtil)
            locationUtil.destroyLocation();
    }

}
