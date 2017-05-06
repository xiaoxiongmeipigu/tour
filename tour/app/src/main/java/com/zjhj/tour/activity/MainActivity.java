package com.zjhj.tour.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.MainAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.bg_view)
    View bgView;
    @Bind(R.id.addr_tv)
    TextView addrTv;
    @Bind(R.id.person_iv)
    ImageView personIv;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    float total = 0f;
    List<IndexData> mList;
    MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView(){
        bgView.setAlpha(1);
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
               /* LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }*/

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                DebugLog.i("position:" + firstVisibleItemPosition);
                total += dy;
                if (firstVisibleItemPosition == 0) {

                    DebugLog.i("total:" + total);
                    DebugLog.i("hight:" + (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop()));
                    float alphaValue = total / (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop());
                    if (alphaValue > 1) {
                        alphaValue = 1f;
                    } else if (alphaValue < 0) {
                        alphaValue = 0;
                    }
                    bgView.setAlpha(alphaValue);
                }

            }
        });

    }

    private void load(){
        mList.add(new IndexData(0, "SCROLL",new ArrayList<MapiResourceResult>()));
        mList.add(new IndexData(1, "ITEM_HOT",new ArrayList<MapiResourceResult>()));
        mList.add(new IndexData(2, "ITEM_SHOP",new ArrayList<MapiResourceResult>()));
        Collections.sort(mList);
        mAdapter.notifyDataSetChanged();
    }

    public void refreshData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        load();
    }

    @OnClick({R.id.addr_tv, R.id.person_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addr_tv:
                break;
            case R.id.person_iv:
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
