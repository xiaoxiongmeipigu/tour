package com.zjhj.tour.util;

import android.content.Intent;

import com.amap.api.navi.model.NaviLatLng;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.activity.CityActivity;
import com.zjhj.tour.activity.FoodDetailActivity;
import com.zjhj.tour.activity.ForgetPsdActivity;
import com.zjhj.tour.activity.GPSNaviActivity;
import com.zjhj.tour.activity.HisSearchActivity;
import com.zjhj.tour.activity.InfoWindowActivity;
import com.zjhj.tour.activity.LoginActivity;
import com.zjhj.tour.activity.MainActivity;
import com.zjhj.tour.activity.ModifyPhoneActivity;
import com.zjhj.tour.activity.ModifyPhoneNextActivity;
import com.zjhj.tour.activity.ModifyPsdActivity;
import com.zjhj.tour.activity.OrderListActivity;
import com.zjhj.tour.activity.PersonActivity;
import com.zjhj.tour.activity.PurcaseActivity;
import com.zjhj.tour.activity.RegisterActivity;
import com.zjhj.tour.activity.SetPsdActivity;
import com.zjhj.tour.activity.ShowBigPicActivity;
import com.zjhj.tour.activity.destine.DestineActivity;
import com.zjhj.tour.activity.destine.DestinePurcaseActivity;
import com.zjhj.tour.activity.destine.ModifyDestineActivity;
import com.zjhj.tour.activity.discuss.DiscussActivity;
import com.zjhj.tour.activity.discuss.DiscussListActivity;
import com.zjhj.tour.activity.person.CollectActivity;
import com.zjhj.tour.activity.person.MessageActivity;
import com.zjhj.tour.activity.person.MyDiscussActivity;
import com.zjhj.tour.activity.shop.ServiceDetailActivity;
import com.zjhj.tour.activity.shop.ShopDetailActivity;
import com.zjhj.tour.activity.shop.ShopImageActivity;
import com.zjhj.tour.activity.shop.ShopListActivity;
import com.zjhj.tour.activity.webview.WebviewControlActivity;

import java.util.ArrayList;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 我的下单列表
     */
   /* public static void go2IndentOrder() {
        Intent intent = new Intent(AppContext.getInstance(), IndentOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }*/

    /**
     * 首页
     */
    public static void go2Main(int type) {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type",type);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录
     */
    public static void go2Login() {
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 设置密码
     */
    public static void go2SetPsd(String code) {
        Intent intent = new Intent(AppContext.getInstance(), SetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code",code);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 忘记密码
     */
    public static void go2ForgetPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ForgetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 注册
     */
    public static void go2Register() {
        Intent intent = new Intent(AppContext.getInstance(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店列表
     */
    public static void go2ShopList(String keyword) {
        Intent intent = new Intent(AppContext.getInstance(), ShopListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("keyword",keyword);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店详情
     */
    public static void go2ShopDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), ShopDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 评论列表
     */
    public static void go2DiscussList(String id,String type) {
        Intent intent = new Intent(AppContext.getInstance(), DiscussListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        intent.putExtra("type",type);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 套餐详情
     */
    public static void go2FoodDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), FoodDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 个人中心
     */
    public static void go2Person() {
        Intent intent = new Intent(AppContext.getInstance(), PersonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 我的评价
     */
    public static void go2MyDiscuss() {
        Intent intent = new Intent(AppContext.getInstance(), MyDiscussActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 我的收藏
     */
    public static void go2Collect() {
        Intent intent = new Intent(AppContext.getInstance(), CollectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 我的消息
     */
    public static void go2Message() {
        Intent intent = new Intent(AppContext.getInstance(), MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改登录密码
     */
    public static void go2ModifyPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改手机号
     */
    public static void go2ModifyPhone() {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPhoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改手机号-下一步
     */
    public static void go2ModifyPhoneNext(String code) {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPhoneNextActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code",code);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 购物车
     */
    public static void go2Purcase(String merchant_id,String merchant_name) {
        Intent intent = new Intent(AppContext.getInstance(), PurcaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",merchant_id);
        intent.putExtra("merchant_name",merchant_name);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 我的订单
     */
    public static void go2OrderList() {
        Intent intent = new Intent(AppContext.getInstance(), OrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 立即预订
     */
    public static void go2Destine(String id) {
        Intent intent = new Intent(AppContext.getInstance(), DestineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 预订
     */
    public static void go2DestinePurcase(String id) {
        Intent intent = new Intent(AppContext.getInstance(), DestinePurcaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 评价
     */
    public static void go2Discuss(MapiItemResult itemResult) {
        Intent intent = new Intent(AppContext.getInstance(), DiscussActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item",itemResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 城市列表
     */
    public static void go2City() {
        Intent intent = new Intent(AppContext.getInstance(), CityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店服务详情
     */
    public static void go2ServiceDetail(MapiItemResult mapiItemResult) {
        Intent intent = new Intent(AppContext.getInstance(), ServiceDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item",mapiItemResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 酒店秀
     */
    public static void go2ShopImage(MapiItemResult mapiItemResult) {
        Intent intent = new Intent(AppContext.getInstance(), ShopImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item",mapiItemResult);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 图片展示
     */
    public static void go2ShowBig(ArrayList<MapiResourceResult> list,int position) {
        Intent intent = new Intent(AppContext.getInstance(), ShowBigPicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("list",list);
        intent.putExtra("position",position);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * h5页面
     */
    public static void go2WebView(String url, String title,String shareTitle,String shareContext,String shareLOGO, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
        intent.putExtra("shareTitle", shareTitle);

        intent.putExtra("shareContext", shareContext);
        intent.putExtra("shareLOGO", shareLOGO);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 实时导航
     */
    public static void go2GPSNavi(NaviLatLng startLatlng, NaviLatLng endLatlng) {
        Intent intent = new Intent(AppContext.getInstance(), GPSNaviActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("start",startLatlng);
        intent.putExtra("end",endLatlng);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 定位
     */
    public static void go2InfoWindow(MapiItemResult itemResult) {
        Intent intent = new Intent(AppContext.getInstance(), InfoWindowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item",itemResult);
        AppContext.getInstance().startActivity(intent);
    }

    public static void go2HisSearch(){
        Intent intent = new Intent(AppContext.getInstance(), HisSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改订单
     * @param id
     */
    public static void go2ModifyDestine(String id){
        Intent intent = new Intent(AppContext.getInstance(), ModifyDestineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

}
