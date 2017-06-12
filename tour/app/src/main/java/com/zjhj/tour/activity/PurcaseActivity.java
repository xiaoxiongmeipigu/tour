package com.zjhj.tour.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.PurcaseAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.AdapterSelListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurcaseActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.all)
    ImageView all;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.order)
    TextView order;
    @Bind(R.id.allPriceLL)
    LinearLayout allPriceLL;
    @Bind(R.id.merchant_name)
    TextView merchantName;

    List<IndexData> mList;
    ArrayList<MapiCarResult> list;

    PurcaseAdapter mAdapter;

    boolean isAll;

    double allPrice = 0;
    ArrayList<MapiCarResult> selList;

    String merchant_id = "";
    String merchant_name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purcase);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            merchant_id = getIntent().getStringExtra("id");
            merchant_name = getIntent().getStringExtra("merchant_name");
        }
        if (!TextUtils.isEmpty(merchant_id)) {
            initView();
            initListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("购物车");
        tvRight.setText("编辑");
        merchantName.setText(merchant_name);

        mList = new ArrayList<>();
        list = new ArrayList<>();
        selList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PurcaseAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        mAdapter.setOnAdapterSelListener(new AdapterSelListener() {
            @Override
            public void isAll() {
            }

            @Override
            public void notifyParentStatus(int position) {

            }

            @Override
            public void notifyChildStatus(int position) {
                MapiCarResult item = (MapiCarResult) mList.get(position).getData();
                item.setSel(!item.isSel());
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
            }

            @Override
            public void notifyChildNum(final int position, final int num) {
                DebugLog.i("notifyChildNum=>" + num);
                showLoading();
                MapiCarResult mapiCarResult = (MapiCarResult) mList.get(position).getData();
                ItemApi.cartedit(PurcaseActivity.this, mapiCarResult.getCart_id(), num + "", new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
//                        MainToast.showShortToast("修改成功");
                        String priceStr = "";
                        String numStr = "";
                        try{
                            priceStr = success.getJSONObject("data").getString("total_original_price");
                            numStr = success.getJSONObject("data").getString("num");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(TextUtils.isEmpty(priceStr))
                            priceStr = "0";
                        if(TextUtils.isEmpty(numStr))
                            numStr = "1";
                        MapiCarResult item = (MapiCarResult) mList.get(position).getData();
                        item.setNum(numStr);
                        item.setOriginal_total_price(priceStr);
                        mAdapter.notifyDataSetChanged();
                        notifyAllStatus();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
            }
        });

    }

    private void load() {

        showLoading();
        ItemApi.cartlist(this, merchant_id, new RequestCallback<List<MapiCarResult>>() {
            @Override
            public void success(List<MapiCarResult> success) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                if (success.isEmpty())
                    return;
                list.addAll(success);
                refreshList(list);
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                swipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });
    }

    private void refreshList(List<MapiCarResult> data) {

        mList.clear();
        mAdapter.notifyDataSetChanged();

        int heatCount = 0;
        int count = 0;
        if (data == null || data.size() == 0) {
            count = 0;
        } else {
            for (MapiCarResult ware : data) {
                heatCount++;
                mList.add(new IndexData(count++, "ITEM", ware));
                if (heatCount < data.size())
                    mList.add(new IndexData(count++, "DIVIDER", new Object()));
            }
        }
        mAdapter.notifyDataSetChanged();
        notifyAllStatus();
    }

    public void refreshData(){
        list.clear();
        mList.clear();
        isAll = false;
        selList.clear();
        mAdapter.notifyDataSetChanged();
        notifyAllStatus();
        load();
    }

    private void notifyAllStatus() {
        DebugLog.i("notifyAllStatus");
        selList.clear();
        isAll = true;
        allPrice = 0;
        if (!mList.isEmpty()) {
            for (IndexData indexData : mList) {
                if (indexData.getType().equals("ITEM")) {
                    MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                    if (!mapiCartResult.isSel()) {
                        DebugLog.i("notifyAllStatus==>ITEM");
                        isAll = false;
                    } else {
                        String price = TextUtils.isEmpty(mapiCartResult.getOriginal_total_price()) ? "0" : mapiCartResult.getOriginal_total_price();
                        double priceDouble = Double.parseDouble(price);
                        allPrice += priceDouble;
                        selList.add(mapiCartResult);
                    }
                }
            }
        } else
            isAll = false;

        price.setText(allPrice + "");
        if (isAll)
            all.setImageResource(R.mipmap.circle_sel_right);
        else
            all.setImageResource(R.mipmap.circle_sel);

    }

    @OnClick({R.id.back, R.id.tv_right, R.id.all, R.id.order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                if ("编辑".equals(tvRight.getText().toString())) {
                    tvRight.setText("完成");
                    order.setText("删除");
                    order.setBackgroundResource(R.drawable.selector_press_bg_color_red);
                    allPriceLL.setVisibility(View.INVISIBLE);
                    mAdapter.setDel(true);
                } else if ("完成".equals(tvRight.getText().toString())) {
                    tvRight.setText("编辑");
                    order.setText("结算");
                    order.setBackgroundResource(R.drawable.selector_press_bg_color_blue);
                    allPriceLL.setVisibility(View.VISIBLE);
                    mAdapter.setDel(false);
                }
                selList.clear();
                for (IndexData indexData : mList) {
                    if (indexData.getType().equals("ITEM")) {
                        MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                        mapiCartResult.setSel(false);

                    }
                }
                isAll = false;
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
                break;
            case R.id.all:
                for (IndexData indexData : mList) {
                    if (indexData.getType().equals("ITEM")) {
                        MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                        mapiCartResult.setSel(!isAll);

                    }
                }
                isAll = !isAll;
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
                break;
            case R.id.order:
                if(null==selList||selList.isEmpty())
                    MainToast.showShortToast("请选择商品");
                else{
                    if("编辑".equals(tvRight.getText().toString())){
//                        ControllerUtil.go2Balance(selList,list);



                        ControllerUtil.go2DestinePurcase(getSelIds());
                    } else if("完成".equals(tvRight.getText().toString())){
                        del();
                    }
                }
                break;
        }
    }

    private String getSelIds(){
        String rec_ids = "";
        for(MapiCarResult itemResult:selList){
            if(TextUtils.isEmpty(rec_ids))
                rec_ids += itemResult.getCart_id();
            else
                rec_ids +=","+ itemResult.getCart_id();
        }
        DebugLog.i("rec_ids==>"+rec_ids);
        return rec_ids;
    }

    private void del(){
        String rec_ids = "";
        for(MapiCarResult itemResult:selList){
            if(TextUtils.isEmpty(rec_ids))
                rec_ids += itemResult.getCart_id();
            else
                rec_ids +=","+ itemResult.getCart_id();
        }
        DebugLog.i("rec_ids==>"+rec_ids);
        showLoading();
        ItemApi.cartdelete(this, rec_ids, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                MainToast.showShortToast("成功删除");

                Iterator<MapiCarResult> it =list.iterator();
                while(it.hasNext()){
                    MapiCarResult x = it.next();
                    if(selList.contains(x)){
                        it.remove();
                    }
                }
                selList.clear();
                refreshList(list);

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
