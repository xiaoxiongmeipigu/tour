package com.zjhj.tour.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.PartnerListAdapter;
import com.zjhj.tour.adapter.percentage.PertnerShopAdapter;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/6/27.
 */
public class PartnerShopLayout extends LinearLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private View view;

    List<MapiItemResult> mList;
    PertnerShopAdapter mAdapter;

    public PartnerShopLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public PartnerShopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public PartnerShopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_partner_shop, this);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PertnerShopAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {

    }

    public void load( List<MapiItemResult> list){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ll_more)
    public void onClick() {
        ControllerUtil.go2PartnerShop();
    }
}
