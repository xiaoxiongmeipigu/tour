package com.zjhj.tour.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.DiscussItemAdapter;
import com.zjhj.tour.adapter.FoodItemAdapter;
import com.zjhj.tour.adapter.HomeItemAdapter;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/5/10.
 */
public class ShopItemLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.more_ll)
    LinearLayout moreLl;
    private Context mContext;
    private View view;

    List<MapiFoodResult> list;
    List<MapiFoodResult> data;
    List<IndexData> mList;

    FoodItemAdapter mAdapter;


    public ShopItemLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public ShopItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public ShopItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_shop_item, this);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();
        list = new ArrayList<>();
        data = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FoodItemAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiFoodResult mapiFoodResult = (MapiFoodResult) mList.get(position).getData();
                ControllerUtil.go2FoodDetail(mapiFoodResult.getId());
            }
        });
    }

    public void load( List<MapiFoodResult> data) {
        this.data.clear();
        this.data.addAll(data);
        list.clear();
        mList.clear();

        if(null!=data&&!data.isEmpty()){

            if(data.size()>3){
                moreLl.setVisibility(View.VISIBLE);
                for(int i=0;i<3;i++){
                    list.add(data.get(i));
                }
            }else{
                moreLl.setVisibility(View.GONE);
                for(int i=0;i<data.size();i++){
                    list.add(data.get(i));
                }
            }
            int count = 0;
            int pos = 0;
            for (MapiFoodResult mapiFoodResult : list) {
                pos++;
                mList.add(new IndexData(count++, "ITEM", mapiFoodResult));
                if(pos<list.size())
                    mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
            }

            mAdapter.notifyDataSetChanged();
        }else{
            moreLl.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.more_ll)
    public void onClick() {
        mList.clear();
        mAdapter.notifyDataSetChanged();

        for(int i=3;i<data.size();i++){
            list.add(data.get(i));
        }

        int count = 0;
        int pos = 0;
        for (MapiFoodResult mapiFoodResult : list) {
            pos++;
            mList.add(new IndexData(count++, "ITEM", mapiFoodResult));
            if(pos<list.size())
                mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
        }

        mAdapter.notifyDataSetChanged();
        moreLl.setVisibility(View.GONE);
    }
}
