package com.zjhj.tour.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.DialogDinnerAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/23.
 */
public class DinnerTiemDialog extends Dialog {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseActivity mActivity;

    private List<MapiResourceResult> mList = new ArrayList<>();
    DialogDinnerAdapter mAdapter;
    Context mContext;

    public void setmList(List<MapiResourceResult> mList) {
        this.mList.clear();
        this.mList.addAll(mList);
        mAdapter.notifyDataSetChanged();
    }

    public DinnerTiemDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        mActivity = (BaseActivity) context;
        initView();
        initListtener();
    }

    private void initView() {
        setContentView(R.layout.dialog_dinner);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), Color.parseColor("#f7f7f7")));
        mAdapter = new DialogDinnerAdapter(mActivity, mList);
        recyclerView.setAdapter(mAdapter);

    }

    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    private void initListtener(){
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(null!=dialogItemClickListner)
                    dialogItemClickListner.onItemClick(view,position);
                dismiss();
            }
        });
    }

    public interface DialogItemClickListner {
        void onItemClick(View view, int type);
    }

    private DialogItemClickListner dialogItemClickListner;

    public void setDialogItemClickListner(DialogItemClickListner dialogItemClickListner){
        this.dialogItemClickListner = dialogItemClickListner;
    }

}
