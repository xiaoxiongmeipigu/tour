package com.zjhj.tour.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.activity.HisSearchActivity;
import com.zjhj.tour.adapter.HomeItemAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.util.JGJDataSource;
import com.zjhj.tour.view.MaxHeightLayout;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;
import com.zjhj.tour.widget.ItemPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.search_tv)
    TextView searchTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.smallrecyclerView)
    RecyclerView smallrecyclerView;
    @Bind(R.id.smallswipeRefreshLayout)
    BestSwipeRefreshLayout smallswipeRefreshLayout;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.bg_color)
    View bgColor;
    @Bind(R.id.area_cb)
    CheckBox areaCb;
    @Bind(R.id.type_cb)
    CheckBox typeCb;
    @Bind(R.id.sort_cb)
    CheckBox sortCb;

    HomeItemAdapter mAdapter;
    HomeItemAdapter smallAdapter;
    List<IndexData> bigList;
    List<IndexData> smallList;

    List<MapiItemResult> list;


    private boolean isSmall = false;

    ItemPopWindow areaPop;
    ItemPopWindow typePop;
    ItemPopWindow sortPop;

    List<MapiResourceResult> areaList;
    List<MapiResourceResult> typeList;
    List<MapiResourceResult> sortList;

    String[] filterStrs = new String[3];

    private Integer pageIndex = 1;
    private Integer pageNum = 8;
    private Integer counts;
    String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        if(null!=getIntent())
            keyword = getIntent().getStringExtra("keyword");
        if(TextUtils.isEmpty(keyword))
            keyword = "";
        initView();
        initBigView();
        initSmallView();
        initListener();
        loadData();
        load();
    }

    private void initView() {

        searchTv.setText(keyword);

        bigList = new ArrayList<>();
        smallList = new ArrayList<>();
        list = new ArrayList<>();
        areaList = new ArrayList<>();
        typeList = new ArrayList<>();
        sortList = new ArrayList<>();

        back.setImageResource(R.mipmap.back_gray);

        areaPop = new ItemPopWindow(this, 0, areaList, R.style.PopupWindowAnimation);
        typePop = new ItemPopWindow(this, 0, typeList, R.style.PopupWindowAnimation);
        sortPop = new ItemPopWindow(this, 0, sortList, R.style.PopupWindowAnimation);

        filterStrs[0] = "";
        filterStrs[1] = "";
        filterStrs[2] = "";

    }

    private void initBigView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HomeItemAdapter(this, bigList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initSmallView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        smallrecyclerView.setHasFixedSize(true);
        smallrecyclerView.setLayoutManager(linearLayoutManager);
        smallAdapter = new HomeItemAdapter(this, smallList);
        smallrecyclerView.setAdapter(smallAdapter);

    }

    private void initListener() {

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiItemResult mapiItemResult = (MapiItemResult) bigList.get(position).getData();
                ControllerUtil.go2ShopDetail(mapiItemResult.getId());
            }
        });

        smallAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiItemResult mapiItemResult = (MapiItemResult) smallList.get(position).getData();
                ControllerUtil.go2ShopDetail(mapiItemResult.getId());
            }
        });


        areaPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                areaCb.setChecked(false);
                bgColor.setVisibility(View.GONE);
            }
        });

        typePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                typeCb.setChecked(false);
                bgColor.setVisibility(View.GONE);
            }
        });

        sortPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                sortCb.setChecked(false);
                bgColor.setVisibility(View.GONE);
            }
        });

        areaPop.setOnPopItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (null != view) {
                    if (postion > 0) {
                        filterStrs[0] = areaList.get(postion).getId();
                        areaCb.setText(areaList.get(postion).getName());
                    } else {
                        filterStrs[0] = "";
                        areaCb.setText("景点选择");
                    }
                    refreshData();
                }
                areaPop.dismiss();
            }
        });

        typePop.setOnPopItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (null != view) {
                    if (postion > 0) {
                        filterStrs[1] = typeList.get(postion).getId();
                        typeCb.setText(typeList.get(postion).getName());
                    } else {
                        filterStrs[1] = "";
                        typeCb.setText("商家类型");
                    }
                    refreshData();
                }
                typePop.dismiss();
            }
        });

        sortPop.setOnPopItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (null != view) {
                    if (postion > 0) {
                        filterStrs[2] = sortList.get(postion).getId();
                        sortCb.setText(sortList.get(postion).getName());
                    } else {
                        filterStrs[2] = "";
                        sortCb.setText("智能排序");
                    }
                    refreshData();
                }
                sortPop.dismiss();
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSmall = b;
                if (null != recyclerView && null != smallrecyclerView) {
                    if (isSmall) {

                        swipeRefreshLayout.setVisibility(View.GONE);
                        smallswipeRefreshLayout.setVisibility(View.VISIBLE);
                    } else {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        smallswipeRefreshLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        smallswipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        smallrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    private void loadNext() {
        if (counts == null || counts <= pageIndex) {
            MainToast.showShortToast("没有更多数据了");
            return;
        }
        pageIndex++;
        load();
    }

    private void loadData() {
        showLoading();
        String city_id = "";
        if(null!=userSP.getUserAddr())
            city_id = TextUtils.isEmpty(userSP.getUserAddr().getCity_id())?"":userSP.getUserAddr().getCity_id();
        showLoading();
        CommonApi.defaultunion(this, city_id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();

                List<MapiResourceResult> areas = JSONArray.parseArray(success.getJSONObject("data").getJSONObject("scenic_spot").getJSONArray("list").toJSONString(),MapiResourceResult.class);
                List<MapiResourceResult> types = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("restaurant_cat").toJSONString(),MapiResourceResult.class);

                if(null!=areas&&!areas.isEmpty()){
                    areaList.clear();
                    areaList.addAll(areas);
                    MapiResourceResult resourceResult = new MapiResourceResult();
                    resourceResult.setId("");
                    resourceResult.setName("全部");
                    areaList.add(0,resourceResult);
//                    areaList.addAll(JGJDataSource.getArea());
                    areaPop.refreshData(areaList);
                }

                if(null!=types&&!types.isEmpty()){
                    typeList.clear();
                    typeList.addAll(types);
//                    typeList.addAll(JGJDataSource.getType());
                    MapiResourceResult resourceResult = new MapiResourceResult();
                    resourceResult.setId("");
                    resourceResult.setName("全部");
                    typeList.add(0,resourceResult);
                    typePop.refreshData(typeList);
                }

                sortList.clear();
                sortList.addAll(JGJDataSource.getSort());
                sortPop.refreshData(sortList);

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void load() {

        String city_id = "";
        String longitude = "";
        String latitude = "";

        if(null!=userSP.getUserAddr()){
            city_id = TextUtils.isEmpty(userSP.getUserAddr().getCity_id()) ? "" : userSP.getUserAddr().getCity_id();
            longitude = TextUtils.isEmpty(userSP.getUserAddr().getLongitude()) ? "" : userSP.getUserAddr().getLongitude();
            latitude = TextUtils.isEmpty(userSP.getUserAddr().getLatitude()) ? "" : userSP.getUserAddr().getLatitude();
        }

        showLoading();
        ItemApi.defaultlist(this, keyword, filterStrs[0], filterStrs[1], filterStrs[2], "", city_id, "", longitude, latitude, pageIndex + "",pageNum+"", new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                smallswipeRefreshLayout.setRefreshing(false);
                counts = isNext;
                if (success.isEmpty())
                    return;
                list.clear();
                list.addAll(success);
                DebugLog.i("list.size=>"+list.size());
                initBigData(list);
                initSmallData(list);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                smallswipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });


    }

    private void initBigData(List<MapiItemResult> data) {
        int count = bigList.size();
        int pos = 0;
        for (MapiItemResult mapiItemResult : data) {
            pos++;
            bigList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
            bigList.add(new IndexData(count++, "ITEM", mapiItemResult));
        }

        mAdapter.notifyDataSetChanged();

    }

    private void initSmallData(List<MapiItemResult> data) {
        int count = smallList.size();
        int pos = 0;
        for (MapiItemResult mapiItemResult : data) {
            pos++;
            smallList.add(new IndexData(count++, "ITEM_SMALL", mapiItemResult));
            if (pos < data.size())
                smallList.add(new IndexData(count++, "DIVIDER_LINE", new ArrayList<MapiResourceResult>()));
        }

        DebugLog.i(smallList.size() + "smallList");

        smallAdapter.notifyDataSetChanged();
    }

    public void refreshData() {
        if (null != bigList) {
            list.clear();
            bigList.clear();
            smallList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            smallAdapter.notifyDataSetChanged();
            load();
        }
    }

    @OnClick({R.id.back, R.id.area_cb, R.id.type_cb, R.id.sort_cb,R.id.search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.area_cb:


                    if (null != areaPop) {
                        areaPop.showPopupWindow(view);
                        bgColor.setVisibility(View.VISIBLE);
                    }


                break;
            case R.id.type_cb:

                    if (null != typePop) {
                        typePop.showPopupWindow(view);
                        bgColor.setVisibility(View.VISIBLE);
                    }

                break;
            case R.id.sort_cb:


                    if (null != sortPop) {
                        sortPop.showPopupWindow(view);
                        bgColor.setVisibility(View.VISIBLE);
                    }

                break;
            case R.id.search_tv:

                Intent intent = new Intent(ShopListActivity.this, HisSearchActivity.class);
                startActivityForResult(intent,RequestCode.SEARCH_HIS);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode== RequestCode.SEARCH_HIS){
                if(null!=data){
                    String key = data.getStringExtra("keyword");
                    if(TextUtils.isEmpty(key))
                        keyword = "";
                    else
                        keyword = key;
                    searchTv.setText(keyword);
                    refreshData();
                }
            }
        }
    }
}
