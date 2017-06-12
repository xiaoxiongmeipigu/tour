package com.zjhj.tour.activity.shop;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiMerchantResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.shop.ShopDetailAdapter;
import com.zjhj.tour.adapter.shop.ShowImgAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopImageActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    MapiItemResult mapiItemResult = null;

    ArrayList<MapiResourceResult> typeOne;
    ArrayList<MapiResourceResult> typeTwo;
    ArrayList<MapiResourceResult> typeThree;

    List<IndexData> mList;
    ShowImgAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_image);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            mapiItemResult = (MapiItemResult) getIntent().getSerializableExtra("item");
        }
        if (null != mapiItemResult) {
            initView();
            load();
            initListener();
        }

    }

    private void initView() {

        center.setText("酒店秀");
        back.setImageResource(R.mipmap.back_white);

        typeOne = new ArrayList<>();
        typeTwo = new ArrayList<>();
        typeThree = new ArrayList<>();

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ShowImgAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);


    }

    private void  initListener(){
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<MapiResourceResult> list = (ArrayList<MapiResourceResult>) mList.get(position).getData();
                ControllerUtil.go2ShowBig(list,0);
            }
        });
    }

    private void load() {
        mList.clear();
        MapiMerchantResult mapiMerchantResult = mapiItemResult.getMerchant_pic();
        if (null != mapiMerchantResult) {
            typeOne = mapiMerchantResult.getType_0();
            typeTwo = mapiMerchantResult.getType_1();
            typeThree = mapiMerchantResult.getType_2();
        }

        if (null != typeOne && !typeOne.isEmpty()) {
            mList.add(new IndexData(0, "DIVIDER", new MapiResourceResult()));
            mList.add(new IndexData(0, "TYPEONE", typeOne));
            mList.add(new IndexData(0, "DIVIDER", new MapiResourceResult()));
        }

        if (null != typeTwo && !typeTwo.isEmpty()) {
            mList.add(new IndexData(0, "TYPETWO", typeTwo));
            mList.add(new IndexData(0, "DIVIDER", new MapiResourceResult()));
        }

        if (null != typeThree && !typeThree.isEmpty()) {
            mList.add(new IndexData(0, "DIVIDER", new MapiResourceResult()));
            mList.add(new IndexData(0, "TYPETHREE", typeThree));
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
