package com.zjhj.tour.activity.percentage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.percentage.PercentageListAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PercentageListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    PercentageListAdapter mAdapter;
    List<MapiItemResult> mList;

    private Integer pageIndex = 1;
    private Integer pageCount = 20;
    private Integer counts=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("奖励金明细");
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PercentageListAdapter(this, mList);
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
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void loadNext() {
        if (counts == null || counts <= pageIndex) {
            MainToast.showShortToast("没有更多数据了");
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    private void load() {

        showLoading();
        UserApi.usercash(this, pageIndex + "", pageCount + "", new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                if(null==success||success.isEmpty())
                    return;
                counts = isNext;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
