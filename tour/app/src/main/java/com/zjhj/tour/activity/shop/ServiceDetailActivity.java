package com.zjhj.tour.activity.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.ShopBasicResult;
import com.zjhj.commom.result.ShopPhoneResult;
import com.zjhj.commom.result.ShopPolicyResult;
import com.zjhj.commom.result.ShopServiceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.shop.ServiceDetailAdapter;
import com.zjhj.tour.adapter.shop.ShopDetailAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ServiceDetailAdapter mAdapter;
    List<IndexData> mList;

    MapiItemResult mapiItemResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);
        if(null!=getIntent())
            mapiItemResult = (MapiItemResult) getIntent().getSerializableExtra("item");
        if(null!=mapiItemResult){
            initView();
            load();
        }

    }

    private void initView() {

        back.setImageResource(R.mipmap.back_white);
        center.setText("酒店详情介绍");

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ServiceDetailAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void load(){
        mList.clear();
        ShopBasicResult shopBasicResult = mapiItemResult.getJdjbxx();
        if(null!=shopBasicResult){
            mList.add(new IndexData(0, "BASIC", shopBasicResult));
        }

        ShopPolicyResult shopPolicyResult = mapiItemResult.getJdzc();
        if(null!=shopPolicyResult){
            mList.add(new IndexData(1, "DIVIDER", new MapiResourceResult()));
            mList.add(new IndexData(2, "POLICY", shopPolicyResult));
        }

        ShopPhoneResult shopPhoneResult = mapiItemResult.getLxfs();
        if(null!=shopPhoneResult){
            mList.add(new IndexData(1, "DIVIDER", new MapiResourceResult()));
            mList.add(new IndexData(2, "PHONE", shopPhoneResult));
        }

        List<ShopServiceResult> serviceResults = mapiItemResult.getService();
        if(null!=serviceResults&&!serviceResults.isEmpty()){
            mList.add(new IndexData(5, "DIVIDER", new MapiResourceResult()));
            mList.add(new IndexData(6, "SERVICE", serviceResults));
        }

        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
