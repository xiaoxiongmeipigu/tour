package com.zjhj.tour.fragment.login;


import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseFrag;
import com.zjhj.tour.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class AccountLoginFrag extends BaseFrag {


    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.psd_et)
    EditText psdEt;
    @Bind(R.id.clear_psd)
    ImageView clearPsd;
    @Bind(R.id.show_psd)
    ImageView showPsd;
    @Bind(R.id.login)
    TextView login;

    public AccountLoginFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_account_login, container, false);
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

        psdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsd.setVisibility(View.VISIBLE);
                } else {
                    clearPsd.setVisibility(View.INVISIBLE);
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
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.clear_name, R.id.clear_psd, R.id.show_psd, R.id.login,R.id.protocol,R.id.forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_psd:
                psdEt.setText("");
                break;
            case R.id.show_psd:
                if(psdEt.getTransformationMethod()==PasswordTransformationMethod.getInstance()){
                    //显示密码
                    psdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPsd.setImageResource(R.mipmap.show_psd);
                }else{
                    //否则隐藏密码
                    psdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPsd.setImageResource(R.mipmap.hid_psd);
                }
                break;
            case R.id.login:
                if(TextUtils.isEmpty(nameEt.getText())){
                    MainToast.showShortToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(psdEt.getText())){
                    MainToast.showShortToast("请输入密码");
                    return;
                }
                showLoading();
                UserApi.login(getActivity(), nameEt.getText().toString(), psdEt.getText().toString(), new RequestCallback<MapiUserResult>() {
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
            case R.id.protocol:

                break;
            case R.id.forget:
                ControllerUtil.go2ForgetPsd();
                break;
        }
    }
}
