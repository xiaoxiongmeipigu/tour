package com.zjhj.tour.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.HomeItemAdapter;
import com.zjhj.tour.adapter.ItemAdapter;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/5/6.
 */
public class HomeItemShopLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private View view;
    HomeItemAdapter mAdapter;
    List<IndexData> mList = new ArrayList<>();

    public HomeItemShopLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public HomeItemShopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public HomeItemShopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_item_shop, this);
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(10), getResources().getColor(R.color.shop_white)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HomeItemAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiItemResult mapiItemResult = (MapiItemResult) mList.get(position).getData();
                ControllerUtil.go2ShopDetail(mapiItemResult.getMerchant_id());
            }
        });

    }

    public void load(List<MapiItemResult> list) {
        refreshData();
        initData(list);
    }

    private void initData(List<MapiItemResult> list) {


        if(null==list||list.isEmpty())
            return;
        int count = 0;
        int pos = 0;
        for(MapiItemResult mapiItemResult : list){
            pos++;
            mList.add(new IndexData(count++, "ITEM", mapiItemResult));
            if (pos <list.size())
                mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
        }

        mAdapter.notifyDataSetChanged();

    }


    public void refreshData() {
        if (null != mList) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.ll_more)
    public void onClick() {
        ControllerUtil.go2ShopList("");
    }

}
