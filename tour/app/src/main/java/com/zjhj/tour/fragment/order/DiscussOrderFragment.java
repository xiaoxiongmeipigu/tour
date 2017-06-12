package com.zjhj.tour.fragment.order;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.activity.discuss.DiscussActivity;
import com.zjhj.tour.adapter.order.OrderItemDiscussAdapter;
import com.zjhj.tour.adapter.order.OrderItemUseAdapter;
import com.zjhj.tour.base.BaseFrag;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class DiscussOrderFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    List<MapiItemResult> mList;
    OrderItemDiscussAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    public DiscussOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_all_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
//        load();
        return view;
    }

    private void initView(){
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderItemDiscussAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
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

        mAdapter.setOrderDeelListener(new OrderItemDiscussAdapter.OrderDeelListener() {
            @Override
            public void cancel(int position) {

            }

            @Override
            public void care(int position) {

                Intent intent = new Intent(getActivity(), DiscussActivity.class);
                intent.putExtra("item",mList.get(position));
                intent.putExtra("position",position);
                startActivityForResult(intent, RequestCode.ORDER_DISCUSS);
//                ControllerUtil.go2Discuss(mList.get(position));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    public void load() {
        showLoading();
        ItemApi.orderlist(getActivity(), "2", pageIndex + "", pageNum + "", new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {
                hideLoading();
                if(null!=swipeRefreshLayout)
                    swipeRefreshLayout.setRefreshing(false);
                counts = isNext;
                if (success.isEmpty())
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                if(null!=swipeRefreshLayout)
                    swipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==RequestCode.ORDER_DISCUSS){
                if(null!=data){
                    int pos = data.getIntExtra("position",-1);
                    if(pos>=0){
                        mList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
