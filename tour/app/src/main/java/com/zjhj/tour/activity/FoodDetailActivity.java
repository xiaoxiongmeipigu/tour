package com.zjhj.tour.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.ShareModule;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.food.FoodDetailAdapter;
import com.zjhj.tour.adapter.shop.ShopDetailAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;
import com.zjhj.tour.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.iv_right_two)
    ImageView ivRightTwo;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IndexData> mList;
    FoodDetailAdapter mAdapter;

    String id = "";
    String merchant_id = "";
    String merchant_name = "";

    ShareDialog shareDialog;
    MapiFoodResult mapiFoodResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        if(null!=getIntent())
            id = getIntent().getStringExtra("id");
        if(!TextUtils.isEmpty(id)){
            initView();
            initListener();
            load();
        }

    }

    private void initView() {
        back.setImageResource(R.mipmap.back_white);
        layHeader.setBackgroundColor(Color.parseColor("#1ea1f3"));
        ivRight.setImageResource(R.mipmap.purcase);
        ivRightTwo.setImageResource(R.mipmap.share);
        ivRightTwo.setVisibility(View.VISIBLE);
        center.setText("套餐详情");
        center.setTextColor(Color.parseColor("#ffffff"));

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(8), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FoodDetailAdapter(this, mList);
        mAdapter.setId(id);
        recyclerView.setAdapter(mAdapter);

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);

    }

    private void initListener() {

        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                String SHARE_ORDER_DETAIL = mapiFoodResult.getMerchant_share_page();
                String img_url = mapiFoodResult.getMerchant_cover_pic();
                switch (position) {
                    case 0://微信好友
                        ShareModule shareModule1 = new ShareModule(FoodDetailActivity.this,mapiFoodResult.getMerchant_name(), mapiFoodResult.getFeature(),img_url, SHARE_ORDER_DETAIL);
                        shareModule1.startShare(1);
                        break;
                    case 1:
                        ShareModule shareModule2 = new ShareModule(FoodDetailActivity.this, mapiFoodResult.getMerchant_name(), mapiFoodResult.getFeature(), img_url,SHARE_ORDER_DETAIL);
                        shareModule2.startShare(2);
                        break;
                    case 2:
                        ShareModule shareModule3 = new ShareModule(FoodDetailActivity.this, mapiFoodResult.getMerchant_name(), mapiFoodResult.getFeature(),img_url, SHARE_ORDER_DETAIL);
                        shareModule3.startShare(3);
                        break;
                    case 3:
                        ShareModule shareModule4 = new ShareModule(FoodDetailActivity.this, mapiFoodResult.getMerchant_name(), mapiFoodResult.getFeature(), img_url, SHARE_ORDER_DETAIL);
                        shareModule4.startShare(4);
                        break;
                }
            }
        });

    }

    private void load(){

        showLoading();
        ItemApi.setmealdetail(this,id,new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                Gson gson = new Gson();
                mapiFoodResult = gson.fromJson(success.getJSONObject("data").toJSONString(),MapiFoodResult.class); //MapiItemResult mapiItemResult = JSONObject.parseObject(success.getJSONObject("data").toJSONString(),MapiItemResult.class);
                merchant_id = mapiFoodResult.getMerchant_id();
                merchant_name = mapiFoodResult.getMerchant_name();
                List<MapiFoodResult> set_meal = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("dishes").toJSONString(),MapiFoodResult.class);

                List<MapiDiscussResult> comment_list = gson.fromJson(success.getJSONObject("data").getJSONArray("comment").toJSONString(), new TypeToken<List<MapiDiscussResult>>(){}.getType());

                mList.clear();
                mList.add(new IndexData(0, "INFO", mapiFoodResult));
                if(null!=set_meal&&!set_meal.isEmpty())
                    mList.add(new IndexData(1, "ITEM", set_meal));
                if(null!=comment_list&&!comment_list.isEmpty())
                    mList.add(new IndexData(2, "USER", comment_list));
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

    @OnClick({R.id.back, R.id.iv_right, R.id.iv_right_two,R.id.purcase,R.id.buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_right:
                if(!TextUtils.isEmpty(merchant_id)){
                    ControllerUtil.go2Purcase(merchant_id,merchant_name);
                }
                break;
            case R.id.iv_right_two:
                if(null!=mapiFoodResult)
                    shareDialog.showDialog();
                else
                    MainToast.showShortToast("暂无信息");
                break;
            case R.id.purcase:
                if(!TextUtils.isEmpty(id)){
                    addCart();
                }
                break;
            case R.id.buy:
                if(!TextUtils.isEmpty(merchant_id)){
                    ControllerUtil.go2Destine(id);
                }
                break;
        }
    }

    private void addCart(){
        showLoading();
        ItemApi.cartadd(this, id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                MainToast.showShortToast("成功加入购物车");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

}
