package com.zjhj.tour.fragment.login;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.SMSUtils;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseFrag;
import com.zjhj.tour.receiver.SMSBroadcastReceiver;
import com.zjhj.tour.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class PhoneLoginFrag extends BaseFrag {

    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.code_et)
    EditText codeEt;
    @Bind(R.id.clear_code)
    ImageView clearCode;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.request_code)
    TextView requestCode;

    /**
     * 短信验证倒计时--时长
     */
    private int i = 60;
    // 读取短信广播
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    SMSUtils.EventHandler eventHandler;

    public PhoneLoginFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    private void initView() {

    }

    private void initListener() {
        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearCode.setVisibility(View.VISIBLE);
                } else {
                    clearCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DebugLog.i("onDestroyView");
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.clear_name, R.id.clear_code, R.id.login,R.id.request_code,R.id.protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_code:
                codeEt.setText("");
                break;
            case R.id.login:
                if(TextUtils.isEmpty(nameEt.getText())){
                    MainToast.showShortToast("请输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(codeEt.getText())){
                    MainToast.showShortToast("请输入验证码");
                    return;
                }
                showLoading();
                UserApi.smsLogin(getActivity(), nameEt.getText().toString(), codeEt.getText().toString(), new RequestCallback<MapiUserResult>() {
                    @Override
                    public void success(MapiUserResult success) {
                        hideLoading();
                        MainToast.showShortToast("登录成功");
                        userSP.saveUserBean(success);
                        ControllerUtil.go2Main(4);
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
                break;
            case R.id.request_code:
                String phoneStr = nameEt.getText().toString();

                if(TextUtils.isEmpty(phoneStr)){
                    MainToast.showShortToast("请输入手机号");
                    return;
                }

                if(!StringUtil.isMobile(phoneStr)){
                    MainToast.showShortToast("手机号格式不正确！");
                    return;
                }

                requestCode();
                break;
            case R.id.protocol:
                ControllerUtil.go2WebView(BasicApi.PROTOCOL_GUIDE_URL,"用户协议","","","",false);
                break;
        }
    }

    /**
     * 向服务器请求验证码
     */
    private void requestCode() {
        SMSUtils.requestCode(getActivity(),"QUICK_LOGIN",nameEt.getText().toString(),"");
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        requestCode.setClickable(false);
        requestCode.setFocusableInTouchMode(false);
        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_color_divider));
        requestCode.setTextColor(getResources().getColor(R.color.shop_white));
        requestCode.setText("(" + i + "s)后重新获取");
        initHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        i = 30;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    private void initHandler(){
        eventHandler = new SMSUtils.EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = -7;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSUtils.registerEventHandler(eventHandler);
    }

    String mark = "";

    /**
     * 处理服务器返回的信息
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -9:
                    if(null!=requestCode)
                        requestCode.setText("(" + i + "s)后重新获取");
                    break;
                case -8:
                    if(null!=requestCode){
                        requestCode.setText("获取验证码");
                        requestCode.setClickable(true);
                        requestCode.setTextColor(getResources().getColor(R.color.shop_yellow));
                        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_white_stroke_yellow));
                        i = 60;
                    }

                    break;
                case -7:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    DebugLog.e("event=" + event);
                    if (result == SMSUtils.RESULT_COMPLETE) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE) {
                            mark = (String) data;
                            MainToast.showShortToast("验证码已经发送");
                        }
                    }else if(result == SMSUtils.RESULT_ERROR){
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE_ERROR) {
                            MainToast.showShortToast((String) data);

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=eventHandler)
            SMSUtils.unregisterEventHandler(eventHandler);
        DebugLog.i("onDestroy");
    }
}
