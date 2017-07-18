package com.zjhj.tour.activity.percentage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.percentage.PercentageListAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PercentageActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.total_tv)
    TextView totalTv;
    @Bind(R.id.cash_tv)
    TextView cashTv;
    @Bind(R.id.iv_right)
    ImageView ivRight;

    PercentageListAdapter mAdapter;
    List<MapiItemResult> mList;
    String money;
    String exitStr = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage);
        ButterKnife.bind(this);
        initView();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("奖励金");
        ivRight.setImageResource(R.mipmap.explain_logo);
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), getResources().getColor(R.color.background_gray)));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PercentageListAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void load() {

        showLoading();
        UserApi.usermoney(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if (null == success)
                    return;
                List<MapiItemResult> list = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("bounty").toJSONString(), MapiItemResult.class);

                money = success.getJSONObject("data").getString("money");

                moneyTv.setText(TextUtils.isEmpty(money) ? "0" : money);

                String total = success.getJSONObject("data").getString("total");

                totalTv.setText("历史总额：" + (TextUtils.isEmpty(total) ? "0" : total));


                String cash = success.getJSONObject("data").getString("cash");

                cashTv.setText("已提现金额：" + (TextUtils.isEmpty(cash) ? "0" : cash));

                exitStr = success.getJSONObject("data").getString("exit");
                exitStr = TextUtils.isEmpty(exitStr) ? "0" : exitStr;

                if (null != list && !list.isEmpty()) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    @OnClick({R.id.back, R.id.apply, R.id.more_ll,R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.apply:
                if ("0".equals(exitStr)) {

                    Intent intent = new Intent(AppContext.getInstance(), PercentageSubmitActivity.class);
                    intent.putExtra("money", money);
                    startActivityForResult(intent, RequestCode.PERCENTAGE_APPLY);

                } else {
                    MainToast.showLongToast("您有一个提现申请正在处理中，期间不可再次申请");
                }

                break;
            case R.id.more_ll:
                ControllerUtil.go2PercentageList();
                break;
            case R.id.iv_right:
                ControllerUtil.go2WebView(BasicApi.Percentage_EXPLAIN_URL,"规则说明","","","",false);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (requestCode == RequestCode.PERCENTAGE_APPLY) {
                exitStr = "1";
            }
        }
    }
}
