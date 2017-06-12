package com.zjhj.tour.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiCityResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.LocationUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.city.CityListAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityActivity extends BaseActivity {

    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.clear_iv)
    ImageView clearIv;

    List<IndexData> mList;
    CityListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);

        initView();
        initListener();
        load();
    }

    private void initView() {
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CityListAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        mList.clear();

        String cityname = "";
        if(null!=userSP.getUserAddr())
            cityname = userSP.getUserAddr().getCity_name();
        mList.add(new IndexData(0, "SEL",cityname));
        mList.add(new IndexData(1, "LOCATION", new ArrayList<MapiResourceResult>()));
        mAdapter.notifyDataSetChanged();


    }

    private void initListener() {

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    refreshData();
                }
                return true;
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                } else {
                    clearIv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiCityItemResult mapiCityItemResult = (MapiCityItemResult) mList.get(position).getData();
                if(null!=userSP.getUserAddr()){
                    MapiCityItemResult mapiUserResult = userSP.getUserAddr();
                    mapiUserResult.setCity_id(mapiCityItemResult.getCity_id());
                    mapiUserResult.setCity_name(mapiCityItemResult.getCity_name());
                    mapiUserResult.setProvince_id(mapiCityItemResult.getProvince_id());
                    mapiUserResult.setProvince_name(mapiCityItemResult.getProvince_name());
                    userSP.saveUserAddr(mapiUserResult);
                }else{
                    MapiCityItemResult mapiUserResult = new MapiCityItemResult();
                    mapiUserResult.setCity_id(mapiCityItemResult.getCity_id());
                    mapiUserResult.setCity_name(mapiCityItemResult.getCity_name());
                    mapiUserResult.setProvince_id(mapiCityItemResult.getProvince_id());
                    mapiUserResult.setProvince_name(mapiCityItemResult.getProvince_name());
                    userSP.saveUserAddr(mapiUserResult);
                }
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void refreshData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        String cityname = "";
        if(null!=userSP.getUserAddr())
            cityname = userSP.getUserAddr().getCity_name();
        mList.add(new IndexData(0, "SEL",cityname));
        mList.add(new IndexData(1, "LOCATION", new ArrayList<MapiResourceResult>()));
        load();
    }

    private void load() {

        showLoading();
        CommonApi.city(this, new RequestCallback<List<MapiCityResult>>() {
            @Override
            public void success(List<MapiCityResult> success) {
                hideLoading();
                if(success.isEmpty())
                    return;
                int count = 2;
                int pos = 0;
               for(MapiCityResult cityResult : success){
                   pos++;
                   List<MapiCityItemResult> citys = cityResult.getList();
                   for(int i=0;i<citys.size();i++){
                       mList.add(new IndexData(count++, "ITEM", citys.get(i)));
                       if (pos < success.size()||i<citys.size()-1)
                           mList.add(new IndexData(count++, "DIVIDER", new ArrayList<MapiResourceResult>()));
                   }
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

    @OnClick({R.id.clear_iv, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_iv:
                searchEt.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
