package com.zjhj.tour.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPsdActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.psd_three_et)
    EditText psdThreeEt;
    @Bind(R.id.clear_psd_three)
    ImageView clearPsdThree;
    @Bind(R.id.psd_et)
    EditText psdEt;
    @Bind(R.id.clear_psd)
    ImageView clearPsd;
    @Bind(R.id.psd_two_et)
    EditText psdTwoEt;
    @Bind(R.id.clear_psd_two)
    ImageView clearPsdTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psd);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        center.setText("修改登录密码");
        back.setImageResource(R.mipmap.back);

    }

    private void initListener() {

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

        psdTwoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsdTwo.setVisibility(View.VISIBLE);
                } else {
                    clearPsdTwo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        psdThreeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsdThree.setVisibility(View.VISIBLE);
                } else {
                    clearPsdThree.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void load() {

    }

    @OnClick({R.id.back, R.id.clear_psd_three, R.id.clear_psd, R.id.clear_psd_two, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_psd_three:
                psdThreeEt.setText("");
                break;
            case R.id.clear_psd:
                psdEt.setText("");
                break;
            case R.id.clear_psd_two:
                psdTwoEt.setText("");
                break;
            case R.id.confirm:
                String oldPsd = psdThreeEt.getText().toString();
                String newPsd = psdEt.getText().toString();
                String newTwoPsd = psdTwoEt.getText().toString();

                if(TextUtils.isEmpty(oldPsd)){
                    MainToast.showShortToast("请输入当前密码");
                    return;
                }

                if(TextUtils.isEmpty(newPsd)){
                    MainToast.showShortToast("请输入新密码");
                    return;
                }

                if(TextUtils.isEmpty(newTwoPsd)){
                    MainToast.showShortToast("请确认密码");
                    return;
                }

                if(!newPsd.equals(newTwoPsd)){
                    MainToast.showShortToast("两次密码输入不一致");
                    return;
                }

                confirm(oldPsd,newPsd);

                break;
        }
    }

    private void confirm(String old_password,String new_password){
        showLoading();
        UserApi.modifypassword(this, old_password, new_password, new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                MainToast.showShortToast("密码修改成功");
                finish();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

}
