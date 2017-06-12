package com.zjhj.tour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.result.SearchHistory;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;

import org.apmem.tools.layouts.FlowLayout;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HisSearchActivity extends BaseActivity {

    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.clear_iv)
    ImageView clearIv;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.clear_history)
    ImageView clearHistory;
    @Bind(R.id.recent_search)
    FlowLayout recentSearch;

    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_search);
        ButterKnife.bind(this);
        initView();
        initListener();
        getRecentSearchData();
    }

    private void initView(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("tour").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);
//        searchEt.requestFocus();

    }

    private void initListener(){
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性

                    String keyWord = searchEt.getText().toString().trim();
                    if (TextUtils.isEmpty(keyWord)) {
//                        MainToast.showShortToast("请输入需要搜索的商家名称");
//                        return true;
                    }else
                        insertKeyword(keyWord);
                    //TODO回车键按下时要执行的操作

                    Intent intent = new Intent();
                    intent.putExtra("keyword",keyWord);
                    setResult(RESULT_OK,intent);
                    finish();
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

    }

    private void getRecentSearchData() {
        try {
            List<SearchHistory> historyList = db.selector(SearchHistory.class).orderBy("createDate", true).limit(10).findAll();
            recentSearch.removeAllViews();
            if (historyList == null||historyList.size()==0) {
                clearHistory.setVisibility(View.GONE);
                return;
            }else{
                clearHistory.setVisibility(View.VISIBLE);
            }
            showRecentSearch(historyList);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 最近搜索
     *
     * @param list
     */
    private void showRecentSearch(List<SearchHistory> list) {
        for (final SearchHistory resourceResult : list) {
            final TextView textView = new TextView(this);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(params);
            textView.setPadding(DPUtil.dip2px(8), DPUtil.dip2px(4), DPUtil.dip2px(8), DPUtil.dip2px(4));
            textView.setText(resourceResult.getKeyword());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.shop_black));
            textView.setBackgroundResource(R.drawable.rectangle_solid_white_strike_divider_line_5);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setTextColor(getResources().getColor(R.color.gray_middle));
                    String keyWord = textView.getText().toString();
                    insertKeyword(keyWord);
                    Intent intent = new Intent();
                    intent.putExtra("keyword",keyWord);
                    setResult(RESULT_OK,intent);
                    finish();//进入列表页
                }
            });
            recentSearch.addView(textView);
        }
    }

    /**
     * 将搜索内容保存到本地
     *
     * @param keyWord
     */
    private void insertKeyword(String keyWord) {
        try {
            SearchHistory history = db.selector(SearchHistory.class).where("keyword", "==", keyWord).findFirst();
            if (history != null) {
                history.setCreateDate(new Date());
                db.update(history);
            } else {
                history = new SearchHistory();
                history.setKeyword(keyWord);
                history.setCreateDate(new Date());
                db.save(history);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.clear_iv, R.id.cancel, R.id.clear_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_iv:
                searchEt.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.clear_history:
                try {
                    db.delete(SearchHistory.class);
                    getRecentSearchData();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
