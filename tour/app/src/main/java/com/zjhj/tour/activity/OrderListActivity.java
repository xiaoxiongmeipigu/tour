package com.zjhj.tour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.tour.R;
import com.zjhj.tour.adapter.TabFragmentAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.fragment.discuss.AllDiscussFragment;
import com.zjhj.tour.fragment.discuss.ImageDiscussFragment;
import com.zjhj.tour.fragment.order.AllOrderFragment;
import com.zjhj.tour.fragment.order.DiscussCompleteOrderFragment;
import com.zjhj.tour.fragment.order.DiscussOrderFragment;
import com.zjhj.tour.fragment.order.UseOrderFragment;
import com.zjhj.tour.fragment.order.WaitOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<String> list_title = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    TabFragmentAdapter mAdapter;

//    AllOrderFragment allOrderFragment;
    WaitOrderFragment waitOrderFragment;
    UseOrderFragment useOrderFragment;
    DiscussOrderFragment discussOrderFragment;
    DiscussCompleteOrderFragment discussCompleteOrderFragment;
    int fragIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            fragIndex = getIntent().getIntExtra("fragIndex",0);
        }
        initView();
        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragIndex = intent.getIntExtra("fragIndex",0);
        if(viewpager!=null){
            viewpager.setCurrentItem(fragIndex);
        }
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("我的订单");

//        allOrderFragment = new AllOrderFragment();
        waitOrderFragment = new WaitOrderFragment();
        useOrderFragment = new UseOrderFragment();
        discussOrderFragment = new DiscussOrderFragment();
        discussCompleteOrderFragment = new DiscussCompleteOrderFragment();

//        list.add(allOrderFragment);
        list.add(waitOrderFragment);
        list.add(useOrderFragment);
        list.add(discussOrderFragment);
        list.add(discussCompleteOrderFragment);

//        list_title.add("全部");
        list_title.add("待确认");
        list_title.add("待使用");
        list_title.add("待评价");
        list_title.add("已评价");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(2)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(3)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

        viewpager.setCurrentItem(fragIndex);

    }

    private void initListener() {

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
