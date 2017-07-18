package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiMsgResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/6/16.
 */
public class UserApi extends BasicApi{

    public static void login(Activity activity, String phone, String pwd, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("loginname",phone);
        params.put("password",pwd);
        MapiUtil.getInstance().call(activity,loginUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
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
     * 短信验证码登录
     * @param activity
     * @param mobile
     * @param sms_code
     * @param callback
     * @param exceptionCallback
     */
    public static void smsLogin(Activity activity,String mobile,String sms_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        MapiUtil.getInstance().call(activity,smsLoginUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
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
     * 获取验证码
     * @param activity
     * @param phone
     * @param callback
     * @param exceptionCallback
     */
    public static void getverify(Activity activity,String scene,String phone,String img_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        params.put("mobile",phone);
        if(!TextUtils.isEmpty(img_code))
            params.put("img_code",img_code);
        MapiUtil.getInstance().call(activity,getverify,params,new MapiUtil.MapiSuccessResponse(){
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
     * .修改手机号：1验证旧手机号
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void sendunbindcode(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,sendunbindcode,params,new MapiUtil.MapiSuccessResponse(){
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
     * 修改手机号：3给新手机发短信验证码
     * @param activity
     * @param mobile
     * @param code
     * @param callback
     * @param exceptionCallback
     */
    public static void sendbindcode(Activity activity,String mobile,String code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("code",code);
        MapiUtil.getInstance().call(activity,sendbindcode,params,new MapiUtil.MapiSuccessResponse(){
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
     * 注册
     * @param activity
     * @param name
     * @param mobile
     * @param sms_code
     * @param identity_no
     * @param tour_guide_no
     * @param identity_pic
     * @param tour_guide_pic
     * @param user_code
     * @param callback
     * @param exceptionCallback
     */
    public static void register(Activity activity,String name,String mobile,String sms_code,String identity_no,String tour_guide_no,String identity_pic,
                                String tour_guide_pic,String user_code,String province_id,String city_id,String area_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback ){
        Map<String,String> params = new HashMap<>();
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        params.put("identity_no",identity_no);
        params.put("tour_guide_no",tour_guide_no);
        params.put("identity_pic",identity_pic);
        params.put("tour_guide_pic",tour_guide_pic);
        if(!TextUtils.isEmpty(user_code))
            params.put("user_code",user_code);
        if(!TextUtils.isEmpty(province_id))
            params.put("province_id",province_id);
        if(!TextUtils.isEmpty(city_id))
            params.put("city_id",city_id);
        if(!TextUtils.isEmpty(area_id))
            params.put("area_id",area_id);
        MapiUtil.getInstance().call(activity,registerUrl,params,new MapiUtil.MapiSuccessResponse(){
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
     * 忘记密码
     * @param activity
     * @param scene
     * @param mobile
     * @param sms_code
     * @param code
     * @param new_password
     * @param callback
     * @param exceptionCallback
     */
    public static void findpassword(Activity activity,String scene,String mobile,String sms_code,String code,String new_password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        if(!TextUtils.isEmpty(mobile))
            params.put("mobile",mobile);
        if(!TextUtils.isEmpty(sms_code))
            params.put("sms_code",sms_code);
        if(!TextUtils.isEmpty(code))
            params.put("code",code);
        if(!TextUtils.isEmpty(new_password))
            params.put("new_password",new_password);
        MapiUtil.getInstance().call(activity,findpassword,params,new MapiUtil.MapiSuccessResponse(){
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
     * 个人中心
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void userindex(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,userindex,params,new MapiUtil.MapiSuccessResponse(){
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
     * 修改密码
     * @param activity
     * @param old_password
     * @param new_password
     * @param callback
     * @param exceptionCallback
     */
    public static void modifypassword(Activity activity,String old_password,String new_password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("old_password",old_password);
        params.put("new_password",new_password);
        MapiUtil.getInstance().call(activity,modifypassword,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                String token = json.getJSONObject("data").getString("token");
                callback.success(token);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 修改手机号：2验证旧手机短信验证码
     * @param activity
     * @param sms_code
     * @param callback
     * @param exceptionCallback
     */
    public static void validateunbindcode(Activity activity,String sms_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("sms_code",sms_code);
        MapiUtil.getInstance().call(activity,validateunbindcode,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                String code = json.getJSONObject("data").getString("code");
                callback.success(code);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 修改手机号：4验证新手机号
     * @param activity
     * @param mobile
     * @param sms_code
     * @param bindCode
     * @param callback
     * @param exceptionCallback
     */
    public static void validatebindcode(Activity activity,String mobile,String sms_code,String bindCode,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        params.put("code",bindCode);
        MapiUtil.getInstance().call(activity,validatebindcode,params,new MapiUtil.MapiSuccessResponse(){
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
     * 个人消息
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void usermessage(Activity activity, String page, String limit, final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,usermessage,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiMsgResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiMsgResult.class);
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
     * 提交评论
     * @param activity
     * @param order_id
     * @param cp_score
     * @param hj_score
     * @param sd_score
     * @param spc_score
     * @param jt_score
     * @param content
     * @param pic
     * @param callback
     * @param exceptionCallback
     */
    public static void orderaddcomment(Activity activity,String order_id,String cp_score,String hj_score,String sd_score,String spc_score,String jt_score,
                                       String content,String pic,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        params.put("cp_score",cp_score);
        params.put("hj_score",hj_score);
        params.put("sd_score",sd_score);
        params.put("spc_score",spc_score);
        params.put("jt_score",jt_score);
        params.put("content",content);
        params.put("pic",pic);
        MapiUtil.getInstance().call(activity,orderaddcomment,params,new MapiUtil.MapiSuccessResponse(){
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
     * 我的伙伴
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void userpartner(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,userpartner,params,new MapiUtil.MapiSuccessResponse(){
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
     * 我的伙伴
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void userclosefriend(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,userclosefriend,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiResourceResult.class);
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
     * 我的朋友
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void userfriend(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,userfriend,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiResourceResult.class);
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
     * 我的奖励金
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void usermoney(Activity activity,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,usermoney,params,new MapiUtil.MapiSuccessResponse(){
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
     * 我的推广酒店
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void usermymerchant(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,usermymerchant,params,new MapiUtil.MapiSuccessResponse(){
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
     * 奖励金明细
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void usercash(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        params.put("limit",limit);
        MapiUtil.getInstance().call(activity,usercash,params,new MapiUtil.MapiSuccessResponse(){
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
     * 提现申请
     * @param activity
     * @param money
     * @param realname
     * @param bank_code
     * @param bank_type
     * @param callback
     * @param exceptionCallback
     */
    public static void userapply(Activity activity,String money,String realname,String bank_code,String bank_type,final RequestCallback callback,
                                 final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("money",money);
        params.put("realname",realname);
        params.put("bank_code",bank_code);
        params.put("bank_type",bank_type);

        MapiUtil.getInstance().call(activity,userapply,params,new MapiUtil.MapiSuccessResponse(){
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
