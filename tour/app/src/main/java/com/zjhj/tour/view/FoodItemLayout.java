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
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.FoodItemAdapter;
import com.zjhj.tour.adapter.food.FoodDetailItemAdapter;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/11.
 */
public class FoodItemLayout extends RelativeLayout {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context mContext;
    private View view;

    List<MapiFoodResult> list;
    List<IndexData> mList;

    FoodDetailItemAdapter mAdapter;

    public FoodItemLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public FoodItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public FoodItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_food_item, this);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FoodDetailItemAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    public void load(List<MapiFoodResult> data) {
        list.clear();
        mList.clear();
        list.addAll(data);

        int count = 0;
        int pos = 0;
        for (MapiFoodResult mapiFoodResult : list) {
            pos++;
            mList.add(new IndexData(count++, "ITEM", mapiFoodResult));
            if(pos<list.size())
                mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
        }

        mAdapter.notifyDataSetChanged();
    }

}
