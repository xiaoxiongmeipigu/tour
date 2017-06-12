package com.zjhj.tour.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.MainAlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.user_code)
    TextView userCode;
    @Bind(R.id.wait_count)
    TextView waitCount;
    @Bind(R.id.use_count)
    TextView useCount;
    @Bind(R.id.discuss_count)
    TextView discussCount;

    String phoneStr = "";

    String service_tel = "";
    String about_us = "";

    MainAlertDialog callDialog;
    MainAlertDialog exitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        initView();
        initListener();
//        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("个人中心");
        ivRight.setImageResource(R.mipmap.message);

        image.setImageURI(Uri.parse("res:///" + R.drawable.head_person_default));

        if (null != userSP.getUserBean()) {
            String name = userSP.getUserBean().getName();
            nameTv.setText(TextUtils.isEmpty(name) ? "" : name);
            phoneStr = userSP.getUserBean().getMobile();

            if (!TextUtils.isEmpty(phoneStr) && StringUtil.isMobile(phoneStr)) {
                phoneTv.setText(StringUtil.settingphone(phoneStr));
            }

            String user_code = userSP.getUserBean().getUser_code();
            userCode.setText(TextUtils.isEmpty(user_code) ? "" : user_code);

        }

        callDialog = new MainAlertDialog(this);
        exitDialog = new MainAlertDialog(this);
        exitDialog.setLeftBtnText("退出").setRightBtnText("取消").setTitle("确认退出？");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != userSP.getUserBean()) {
            phoneStr = userSP.getUserBean().getMobile();
            if (!TextUtils.isEmpty(phoneStr) && StringUtil.isMobile(phoneStr)) {
                phoneTv.setText(StringUtil.settingphone(phoneStr));
            }
        }
        load();
    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service_tel));
                startActivity(intent);
                callDialog.dismiss();
            }
        });

        exitDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSP.clearUserData();
                Intent i = new Intent(PersonActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("type", 3);
                startActivity(i);
                finish();
                exitDialog.dismiss();
            }
        });

        exitDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
            }
        });

    }

    private void load() {
        showLoading();
        UserApi.userindex(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                String order_count_0 = success.getJSONObject("data").getString("order_count_0");
                String order_count_1 = success.getJSONObject("data").getString("order_count_1");
                String order_count_2 = success.getJSONObject("data").getString("order_count_2");
                about_us = success.getJSONObject("data").getString("about_us");
                service_tel = success.getJSONObject("data").getString("service_tel");

                if(TextUtils.isEmpty(order_count_0)||"0".equals(order_count_0)){
                    waitCount.setText("");
                    waitCount.setVisibility(View.INVISIBLE);
                }else {
                    waitCount.setText(order_count_0);
                    waitCount.setVisibility(View.VISIBLE);
                }

                if(TextUtils.isEmpty(order_count_1)||"0".equals(order_count_1)){
                    useCount.setText("");
                    useCount.setVisibility(View.INVISIBLE);
                }else {
                    useCount.setText(order_count_1);
                    useCount.setVisibility(View.VISIBLE);
                }

                if(TextUtils.isEmpty(order_count_2)||"0".equals(order_count_2)){
                    discussCount.setText("");
                    discussCount.setVisibility(View.INVISIBLE);
                }else {
                    discussCount.setText(order_count_2);
                    discussCount.setVisibility(View.VISIBLE);
                }

                if(!TextUtils.isEmpty(service_tel)){
                    callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);
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

    @OnClick({R.id.back, R.id.iv_right, R.id.edit_tv, R.id.discuss, R.id.collect, R.id.order_rl, R.id.service_ll, R.id.exit
            , R.id.modify_psd, R.id.wait_ll, R.id.use_ll, R.id.discuss_ll,R.id.about_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_right:
                ControllerUtil.go2Message();
                break;
            case R.id.edit_tv:
                ControllerUtil.go2ModifyPhone();
                break;
            case R.id.discuss:
                ControllerUtil.go2MyDiscuss();
                break;
            case R.id.collect:
                ControllerUtil.go2Collect();
                break;
            case R.id.order_rl:
                ControllerUtil.go2OrderList();
                break;
            case R.id.service_ll:
                if(!TextUtils.isEmpty(service_tel)){
                    callDialog.show();
                }
                break;
            case R.id.about_rl:
                if(!TextUtils.isEmpty(about_us)){
                    ControllerUtil.go2WebView(about_us,"关于我们","","","",false);
                }
                break;
            case R.id.exit:
                exitDialog.show();
                break;
            case R.id.modify_psd:
                ControllerUtil.go2ModifyPsd();
                break;
            case R.id.wait_ll:
                Intent intent = new Intent(this, OrderListActivity.class);
                intent.putExtra("fragIndex", 0);
                startActivity(intent);
                break;
            case R.id.use_ll:
                Intent intent2 = new Intent(this, OrderListActivity.class);
                intent2.putExtra("fragIndex", 1);
                startActivity(intent2);
                break;
            case R.id.discuss_ll:
                Intent intent3 = new Intent(this, OrderListActivity.class);
                intent3.putExtra("fragIndex", 2);
                startActivity(intent3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(null!=callDialog&&callDialog.isShowing()){
            callDialog.dismiss();
            callDialog = null;
        }

        if (null != exitDialog && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
        super.onDestroy();
    }
}
