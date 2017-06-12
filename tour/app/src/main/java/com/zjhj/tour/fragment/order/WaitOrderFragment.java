package com.zjhj.tour.fragment.order;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.order.OrderItemAdapter;
import com.zjhj.tour.base.BaseFrag;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;
import com.zjhj.tour.widget.CancelAlertDialog;
import com.zjhj.tour.widget.MainAlertDialog;
import com.zjhj.tour.widget.RemindAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class WaitOrderFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    List<MapiItemResult> mList;
    OrderItemAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    CancelAlertDialog cancelAlertDialog;
    RemindAlertDialog remindAlertDialog;
    private int pos = -1;

    public WaitOrderFragment() {
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
        mAdapter = new OrderItemAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        cancelAlertDialog = new CancelAlertDialog(getActivity());
        cancelAlertDialog.setLeftBtnText("取消").setRightBtnText("提交").setTitle("取消预订");
        cancelAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancelAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        remindAlertDialog = new RemindAlertDialog(getActivity());


    }

    private void initListener(){

        cancelAlertDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlertDialog.dismiss();
            }
        });

        cancelAlertDialog.setRightClickListener(new View.OnClickListener() {//提交
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(cancelAlertDialog.getInfo())){
                    MainToast.showShortToast("请输入取消理由");
                    return;
                }
                if(pos>=0)
                    cancel(cancelAlertDialog.getInfo());
                cancelAlertDialog.dismiss();
            }
        });

        remindAlertDialog.setCloseClickListener(new View.OnClickListener() {//提醒
            @Override
            public void onClick(View view) {
                remindAlertDialog.dismiss();
            }
        });

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

        mAdapter.setOrderDeelListener(new OrderItemAdapter.OrderDeelListener() {
            @Override
            public void cancel(int position) {
                pos = position;
                cancelAlertDialog.show();

            }

            @Override
            public void care(int position) {
                pos = position;
                MapiItemResult itemResult = mList.get(position);
                if(itemResult.getRemind_available().equals("0"))
                    MainToast.showShortToast("10分钟内只可发送一次提醒");
                else if(itemResult.getRemind_available().equals("1"))
                    remind();
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
        ItemApi.orderlist(getActivity(), "0", pageIndex + "", pageNum + "", new RequestPageCallback<List<MapiItemResult>>() {
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

    private void cancel(String info){

        MapiItemResult itemResult = mList.get(pos);
        showLoading();
        ItemApi.orderrevoke(getActivity(), itemResult.getOrder_id(), info, new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(pos);
                mAdapter.notifyDataSetChanged();
                pos = -1;
                MainToast.showShortToast("取消成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                pos = -1;
                MainToast.showShortToast(message);
            }
        });
    }

    private void remind(){
        MapiItemResult itemResult = mList.get(pos);
        showLoading();
        ItemApi.orderremind(getActivity(), itemResult.getOrder_id(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.get(pos).setRemind_available("0");
                mAdapter.notifyDataSetChanged();
                pos = -1;
                remindAlertDialog.show();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                pos = -1;
                MainToast.showShortToast(message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
