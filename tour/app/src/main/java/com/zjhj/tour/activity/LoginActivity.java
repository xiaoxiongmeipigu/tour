package com.zjhj.tour.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.tour.R;
import com.zjhj.tour.adapter.TabFragmentAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.fragment.login.AccountLoginFrag;
import com.zjhj.tour.fragment.login.PhoneLoginFrag;
import com.zjhj.tour.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<String> list_title = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    TabFragmentAdapter mAdapter;

    AccountLoginFrag accountLoginFrag;
    PhoneLoginFrag phoneLoginFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        back.setImageResource(R.mipmap.close_blue);
        tvRight.setText("注册");
        center.setText("登录");

        accountLoginFrag = new AccountLoginFrag();
        phoneLoginFrag = new PhoneLoginFrag();
        list.add(accountLoginFrag);
        list.add(phoneLoginFrag);
        list_title.add("账号密码登录");
        list_title.add("手机号快捷登录");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    @OnClick({R.id.back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                ControllerUtil.go2Register();
                break;
        }
    }
}
