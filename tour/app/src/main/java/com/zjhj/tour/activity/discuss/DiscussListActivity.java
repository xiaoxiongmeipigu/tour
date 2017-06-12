package com.zjhj.tour.activity.discuss;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhj.tour.R;
import com.zjhj.tour.adapter.TabFragmentAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.fragment.discuss.AllDiscussFragment;
import com.zjhj.tour.fragment.discuss.ImageDiscussFragment;
import com.zjhj.tour.fragment.discuss.NewDiscussFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscussListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    private List<String> list_title = new ArrayList<>();
    private List<Fragment> list = new ArrayList<>();
    TabFragmentAdapter mAdapter;

    AllDiscussFragment allDiscussFragment;
    ImageDiscussFragment imageDiscussFragment;
//    NewDiscussFragment newDiscussFragment;

    String id = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_list);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
        }
        if(!TextUtils.isEmpty(id)){
            initView();
            initListener();
            load();
        }
    }

    private void initView() {
        back.setImageResource(R.mipmap.back_white);
        layHeader.setBackgroundColor(Color.parseColor("#1ea1f3"));
        center.setText("全部用户评价");
        center.setTextColor(Color.parseColor("#ffffff"));

        allDiscussFragment = new AllDiscussFragment();
        allDiscussFragment.setId(id);
        allDiscussFragment.setType(type);
        imageDiscussFragment = new ImageDiscussFragment();
        imageDiscussFragment.setId(id);
        imageDiscussFragment.setType(type);
//        newDiscussFragment = new NewDiscussFragment();

        list.add(allDiscussFragment);
        list.add(imageDiscussFragment);
//        list.add(newDiscussFragment);

        list_title.add("全部");
        list_title.add("晒图");
//        list_title.add("最新");

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
//        tablayout.addTab(tablayout.newTab().setText(list_title.get(2)));
        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    private void initListener() {

    }

    private void load() {

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

}
