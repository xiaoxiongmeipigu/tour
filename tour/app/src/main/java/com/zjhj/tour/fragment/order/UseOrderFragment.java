package com.zjhj.tour.fragment.order;


import android.app.Activity;
import android.content.Intent;
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
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.activity.destine.ModifyDestineActivity;
import com.zjhj.tour.adapter.order.OrderItemAdapter;
import com.zjhj.tour.adapter.order.OrderItemUseAdapter;
import com.zjhj.tour.base.BaseFrag;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;
import com.zjhj.tour.widget.CancelAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class UseOrderFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    List<MapiItemResult> mList;
    OrderItemUseAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;

    CancelAlertDialog cancelAlertDialog;

    private int pos = -1;

    public UseOrderFragment() {
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
        mAdapter = new OrderItemUseAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        cancelAlertDialog = new CancelAlertDialog(getActivity());
        cancelAlertDialog.setLeftBtnText("取消").setRightBtnText("提交").setTitle("取消预订");
        cancelAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancelAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        mAdapter.setOrderDeelListener(new OrderItemUseAdapter.OrderDeelListener() {
            @Override
            public void cancel(int position) {
                pos = position;
                cancelAlertDialog.show();
            }

            @Override
            public void care(int position) {
                MapiItemResult itemResult = mList.get(position);
                Intent intent = new Intent(getActivity(), ModifyDestineActivity.class);
                intent.putExtra("id",itemResult.getOrder_id());
                intent.putExtra("position",position);
                startActivityForResult(intent, RequestCode.ORDER_MODIFY);
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
        ItemApi.orderlist(getActivity(), "1", pageIndex + "", pageNum + "", new RequestPageCallback<List<MapiItemResult>>() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==RequestCode.ORDER_MODIFY){
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
