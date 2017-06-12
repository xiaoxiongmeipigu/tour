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
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.DiscussItemAdapter;
import com.zjhj.tour.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/5/10.
 */
public class ShopUserLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.more_ll)
    LinearLayout moreLl;
    @Bind(R.id.count_tv)
    TextView countTv;
    private Context mContext;
    private View view;

    List<MapiDiscussResult> list;
    List<IndexData> mList;

    DiscussItemAdapter mAdapter;
    String id = "";
    String type = "0";

    public ShopUserLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public ShopUserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public ShopUserLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_shop_user, this);
        ButterKnife.bind(this, view);

        mList = new ArrayList<>();
        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DiscussItemAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {

    }

    public void load(List<MapiDiscussResult> data,String id,String type) {
        this.id = id;
        this.type = type;
        list.clear();
        mList.clear();
        list.addAll(data);

        int count = 0;
        int pos = 0;
        for (MapiDiscussResult mapiItemResult : list) {
            pos++;
            mList.add(new IndexData(count++, "ITEM", mapiItemResult));
            if(pos<list.size())
                mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
        }

        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.more_ll)
    public void onClick() {
        ControllerUtil.go2DiscussList(id,type);
    }
}
