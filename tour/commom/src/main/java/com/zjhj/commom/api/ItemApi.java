package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/5/18.
 */
public class ItemApi extends BasicApi{

    /**
     * 首页接口
     * @param activity
     * @param token
     * @param city
     * @param longitude
     * @param latitude
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultindex(Activity activity,String token, String city, String longitude, String latitude, final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);
        if(!TextUtils.isEmpty(city))
            params.put("city",city);
        if(!TextUtils.isEmpty(longitude))
            params.put("longitude",longitude);
        if(!TextUtils.isEmpty(latitude))
            params.put("latitude",latitude);

        MapiUtil.getInstance().call(activity,defaultindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 酒店列表
     * @param activity
     * @param keyword
     * @param scenic_spot_id
     * @param restaurant_cat_id
     * @param sort
     * @param province_id
     * @param city_id
     * @param area_id
     * @param longitude
     * @param latitude
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultlist(Activity activity, String keyword,String scenic_spot_id,String restaurant_cat_id,String sort,String province_id,String city_id,
                                   String area_id,String longitude,String latitude,String page,String limit,
                            final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(keyword))
            params.put("keyword",keyword);
        if(!TextUtils.isEmpty(scenic_spot_id))
            params.put("scenic_spot_id",scenic_spot_id);
        if(!TextUtils.isEmpty(restaurant_cat_id))
            params.put("restaurant_cat_id",restaurant_cat_id);
        if(!TextUtils.isEmpty(restaurant_cat_id))
            params.put("province_id",province_id);
        if(!TextUtils.isEmpty(city_id))
            params.put("city_id",city_id);
        if(!TextUtils.isEmpty(area_id))
            params.put("area_id",area_id);
        if(!TextUtils.isEmpty(longitude))
            params.put("longitude",longitude);
        if(!TextUtils.isEmpty(latitude))
            params.put("latitude",latitude);
        if(!TextUtils.isEmpty(sort))
            params.put("sort",sort);
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,defaultlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 酒店详情
     * @param activity
     * @param id
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultmerchant(Activity activity,String id,String longitude,String latitude,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        if(!TextUtils.isEmpty(longitude))
            params.put("longitude",longitude);
        if(!TextUtils.isEmpty(latitude))
            params.put("latitude",latitude);
        MapiUtil.getInstance().call(activity,defaultmerchant,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 评论列表
     * @param activity
     * @param token
     * @param merchant_id
     * @param set_meal_id
     * @param type
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void commentlist(Activity activity,String token,String merchant_id,String set_meal_id,String type,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);
        if(!TextUtils.isEmpty(merchant_id))
            params.put("merchant_id",merchant_id);
        if(!TextUtils.isEmpty(set_meal_id))
            params.put("set_meal_id",set_meal_id);
        params.put("type",type);
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,commentlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiDiscussResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiDiscussResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 套餐详情
     * @param activity
     * @param set_meal_id
     * @param callback
     * @param exceptionCallback
     */
    public static void setmealdetail(Activity activity,String set_meal_id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("set_meal_id",set_meal_id);
        MapiUtil.getInstance().call(activity,setmealdetail,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 我的评论列表
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void rodercomment(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,rodercomment,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiDiscussResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiDiscussResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 添加购物车
     * @param activity
     * @param set_meal_id
     * @param callback
     * @param exceptionCallback
     */
    public static void cartadd(Activity activity,String set_meal_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("set_meal_id",set_meal_id);
        MapiUtil.getInstance().call(activity,cartadd,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 购物车列表
     * @param activity
     * @param merchant_id
     * @param callback
     * @param exceptionCallback
     */
    public static void cartlist(Activity activity,String merchant_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("merchant_id",merchant_id);
        MapiUtil.getInstance().call(activity,cartlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiCarResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiCarResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 编辑购物车
     * @param activity
     * @param cart_id
     * @param num
     * @param callback
     * @param exceptionCallback
     */
    public static void cartedit(Activity activity,String cart_id,String num,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("cart_id",cart_id);
        params.put("num",num);
        MapiUtil.getInstance().call(activity,cartedit,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 删除购物车
     * @param activity
     * @param cart_id
     * @param callback
     * @param exceptionCallback
     */
    public static void cartdelete(Activity activity,String cart_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("cart_id",cart_id);
        MapiUtil.getInstance().call(activity,cartdelete,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 提交订单页面
     * @param activity
     * @param set_meal_id
     * @param cart_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderpreview(Activity activity,String set_meal_id,String cart_id,final RequestCallback callback,
                                    final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(set_meal_id))
            params.put("set_meal_id",set_meal_id);
        if(!TextUtils.isEmpty(cart_id))
            params.put("cart_id",cart_id);
        MapiUtil.getInstance().call(activity,orderpreview,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });


    }

    /**
     * 提交订单（直接下单）
     * @param activity
     * @param set_meal_list
     * @param use_date
     * @param mobile
     * @param callback
     * @param exceptionCallback
     */
    public static void ordersubmit(Activity activity,String set_meal_list,String use_date,String use_time_id,String mobile,
                                   final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("set_meal_list",set_meal_list);
        params.put("use_date",use_date);
        params.put("use_time_id",use_time_id);
        params.put("mobile",mobile);
        MapiUtil.getInstance().call(activity,ordersubmit,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 提交订单（购物车提交）
     * @param activity
     * @param cart_id
     * @param use_date
     * @param mobile
     * @param callback
     * @param exceptionCallback
     */
    public static void cartsubmit(Activity activity,String cart_id,String use_date,String use_time_id,String mobile,
                                   final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("cart_id",cart_id);
        params.put("use_date",use_date);
        params.put("use_time_id",use_time_id);
        params.put("mobile",mobile);
        MapiUtil.getInstance().call(activity,cartsubmit,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 订单列表
     * @param activity
     * @param status
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void orderlist(Activity activity,String status,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("status",status);
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,orderlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 订单取消
     * @param activity
     * @param order_id
     * @param revoke_reason
     * @param callback
     * @param exceptionCallback
     */
    public static void orderrevoke(Activity activity,String order_id,String revoke_reason,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        params.put("revoke_reason",revoke_reason);
        MapiUtil.getInstance().call(activity,orderrevoke,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 订单提醒
     * @param activity
     * @param order_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderremind(Activity activity,String order_id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        MapiUtil.getInstance().call(activity,orderremind,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }


    /**
     * 订单修改
     * @param activity
     * @param order_id
     * @param order_detail_list
     * @param use_date
     * @param use_time_id
     * @param mobile
     * @param callback
     * @param exceptionCallback
     */
    public static void ordermodify(Activity activity,String order_id,String order_detail_list,String use_date,String use_time_id,String mobile,
                                          final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        params.put("order_detail_list",order_detail_list);
        params.put("use_date",use_date);
        params.put("use_time_id",use_time_id);
        params.put("mobile",mobile);
        MapiUtil.getInstance().call(activity,ordermodify,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });

    }

    /**
     * 订单修改页面
     * @param activity
     * @param order_id
     * @param callback
     * @param exceptionCallback
     */
    public static void ordermodifypreview(Activity activity,String order_id,final RequestCallback callback,
                                          final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        MapiUtil.getInstance().call(activity,ordermodifypreview,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 我的收藏列表
     * @param activity
     * @param page
     * @param limit
     * @param longitude
     * @param latitude
     * @param callback
     * @param exceptionCallback
     */
    public static void userfavoritelist(Activity activity,String page,String limit,String longitude,String latitude,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        if(!TextUtils.isEmpty(longitude))
            params.put("longitude",longitude);
        if(!TextUtils.isEmpty(latitude))
            params.put("latitude",latitude);
        MapiUtil.getInstance().call(activity,userfavoritelist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 添加、删除收藏
     * @param activity
     * @param merchant_id
     * @param action
     * @param callback
     * @param exceptionCallback
     */
    public static void usersetfavorite(Activity activity,String merchant_id,String action,final RequestCallback callback,
                                       final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("merchant_id",merchant_id);
        params.put("action",action);
        MapiUtil.getInstance().call(activity,usersetfavorite,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

}
