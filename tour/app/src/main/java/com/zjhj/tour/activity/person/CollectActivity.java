package com.zjhj.tour.activity.person;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.person.MyCollectAdapter;
import com.zjhj.tour.adapter.shop.ShopDetailAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    List<IndexData> mList;
    List<MapiItemResult> list;
    MyCollectAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("我的收藏");

        mList = new ArrayList<>();
        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyCollectAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {

        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
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

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiItemResult mapiItemResult = (MapiItemResult) mList.get(position).getData();
                ControllerUtil.go2ShopDetail(mapiItemResult.getMerchant_id());
            }
        });
    }

    private void load() {
        String longitude = "";
        String latitude = "";
        if(null!=userSP.getUserAddr()){
            longitude = TextUtils.isEmpty(userSP.getUserAddr().getLongitude()) ? "" : userSP.getUserAddr().getLongitude();
            latitude = TextUtils.isEmpty(userSP.getUserAddr().getLatitude()) ? "" : userSP.getUserAddr().getLatitude();
        }
        showLoading();
        ItemApi.userfavoritelist(this, pageIndex + "", pageNum + "", longitude, latitude, new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                counts = isNext;
                if (success.isEmpty())
                    return;
                list.clear();
                list.addAll(success);
                DebugLog.i("list.size=>"+list.size());
                initData(list);
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

    private void initData(List<MapiItemResult> data) {
        int count = mList.size();
        int pos = 0;
        for (MapiItemResult mapiItemResult : data) {
            pos++;
            mList.add(new IndexData(count++, "ITEM", mapiItemResult));
            if(pos<data.size())
                mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
        }

        mAdapter.notifyDataSetChanged();

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
            list.clear();
            mList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
