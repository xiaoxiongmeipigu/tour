package com.zjhj.tour.activity.partner;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.PartnerListAdapter;
import com.zjhj.tour.adapter.partner.PartnerAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PartnerActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IndexData> mList;
    PartnerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        ButterKnife.bind(this);
        initView();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("我的伙伴");
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PartnerAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void load(){
        showLoading();
        UserApi.userpartner(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if(null==success)
                    return;
                List<MapiResourceResult> closes = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("close_friend").toJSONString(),MapiResourceResult.class);
                List<MapiResourceResult> friends = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("friend").toJSONString(),MapiResourceResult.class);
                List<MapiItemResult> merchants = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("merchant").toJSONString(),MapiItemResult.class);

                if(null!=closes&&!closes.isEmpty()){
                    mList.add(new IndexData(0,"CLOSE",closes));
                }

                if(null!=friends&&!friends.isEmpty()){
                    mList.add(new IndexData(1,"FRIEND",friends));
                }

                if(null!=merchants&&!merchants.isEmpty()){
                    mList.add(new IndexData(2,"SHOP",merchants));
                }

                mAdapter.notifyDataSetChanged();

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
