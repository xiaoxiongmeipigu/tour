package com.zjhj.tour.activity.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.ShareModule;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
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

public class ShopDetailActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
//    @Bind(R.id.bg_view)
//    View bgView;
    @Bind(R.id.lay_header)
    RelativeLayout layHeader;

    float total = 0f;

    List<IndexData> mList;
    ShopDetailAdapter mAdapter;

    String id = "";
    String name = "";

    ShareDialog shareDialog;

    MapiItemResult mapiItemResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);

        if (null != getIntent()) {
            id = getIntent().getStringExtra("id");
        }

        if (!TextUtils.isEmpty(id)) {
            initView();
            initListener();
            load();
        }

    }

    private void initView() {

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(8), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ShopDetailAdapter(this, mList);
        mAdapter.setId(id);
        recyclerView.setAdapter(mAdapter);

//        bgView.setAlpha(0);

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);


    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
               /* LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }*/

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                DebugLog.i("position:" + firstVisibleItemPosition);
                total += dy;
                if (firstVisibleItemPosition == 0) {

                    DebugLog.i("total:" + total);
                    DebugLog.i("hight:" + (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop()));
                    float alphaValue = total / (recyclerView.getChildAt(0).getBottom() - recyclerView.getChildAt(0).getTop());
                    if (alphaValue > 1) {
                        alphaValue = 1f;
                    } else if (alphaValue < 0) {
                        alphaValue = 0;
                    }
                    bgView.setAlpha(alphaValue);
                }*/

            }
        });

        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                String SHARE_ORDER_DETAIL = mapiItemResult.getMerchant_share_page();
                String img_url = mapiItemResult.getCover_pic();
                switch (position) {
                    case 0://微信好友
                        ShareModule shareModule1 = new ShareModule(ShopDetailActivity.this, mapiItemResult.getName(), mapiItemResult.getFeature(), img_url, SHARE_ORDER_DETAIL);
                        shareModule1.startShare(1);
                        break;
                    case 1:
                        ShareModule shareModule2 = new ShareModule(ShopDetailActivity.this, mapiItemResult.getName(), mapiItemResult.getFeature(), img_url, SHARE_ORDER_DETAIL);
                        shareModule2.startShare(2);
                        break;
                    case 2:
                        ShareModule shareModule3 = new ShareModule(ShopDetailActivity.this, mapiItemResult.getName(), mapiItemResult.getFeature(), img_url, SHARE_ORDER_DETAIL);
                        shareModule3.startShare(3);
                        break;
                    case 3:
                        ShareModule shareModule4 = new ShareModule(ShopDetailActivity.this, mapiItemResult.getName(), mapiItemResult.getFeature(), img_url, SHARE_ORDER_DETAIL);
                        shareModule4.startShare(4);
                        break;
                }
            }
        });

    }

    private void load() {

        String longitude = "";
        String latitude = "";

        if (null != userSP.getUserAddr()) {
            longitude = TextUtils.isEmpty(userSP.getUserAddr().getLongitude()) ? "" : userSP.getUserAddr().getLongitude();
            latitude = TextUtils.isEmpty(userSP.getUserAddr().getLatitude()) ? "" : userSP.getUserAddr().getLatitude();
        }

        showLoading();
        ItemApi.defaultmerchant(this, id, longitude, latitude, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                Gson gson = new Gson();
                mapiItemResult = gson.fromJson(success.getJSONObject("data").toJSONString(), MapiItemResult.class); //MapiItemResult mapiItemResult = JSONObject.parseObject(success.getJSONObject("data").toJSONString(),MapiItemResult.class);
                name = mapiItemResult.getName();
                List<MapiFoodResult> set_meal = JSONArray.parseArray(success.getJSONObject("data").getJSONObject("set_meal").getJSONArray("list").toJSONString(), MapiFoodResult.class);

                List<MapiDiscussResult> comment_list = gson.fromJson(success.getJSONObject("data").getJSONArray("comment_list").toJSONString(), new TypeToken<List<MapiDiscussResult>>() {
                }.getType());

                mList.clear();
                mList.add(new IndexData(0, "INFO", mapiItemResult));
                if (null != set_meal && !set_meal.isEmpty())
                    mList.add(new IndexData(1, "ITEM", set_meal));
                if (null != comment_list && !comment_list.isEmpty())
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

    @OnClick({R.id.back, R.id.purcase, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.purcase:
                if (null != userSP.getUserBean() && !TextUtils.isEmpty(userSP.getUserBean().getToken())) {
                    if (!TextUtils.isEmpty(id)) {
                        ControllerUtil.go2Purcase(id, name);
                    }
                } else {
                    ControllerUtil.go2Login();
                }
                break;
            case R.id.share:
                if (null != mapiItemResult)
                    shareDialog.showDialog();
                else
                    MainToast.showShortToast("暂无信息");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != shareDialog && shareDialog.isShowing()) {
            shareDialog.dismiss();
            shareDialog = null;
        }
    }
}
