package com.zjhj.tour.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.DialogItemAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zaoren on 2015/6/26.
 */
public class ItemDialog extends Dialog {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private String imagePath;
    private BaseActivity mActivity;

    private List<MapiResourceResult> mList = new ArrayList<>();
    DialogItemAdapter mAdapter;
    Context mContext;

    public void setmList(List<MapiResourceResult> mList) {
        this.mList.clear();
        this.mList.addAll(mList);
        mAdapter.notifyDataSetChanged();
    }

    public ItemDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        mActivity = (BaseActivity) context;
        initView();
        initListtener();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private void initView() {
        setContentView(R.layout.dialog_item);
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
        mAdapter = new DialogItemAdapter(mActivity, mList);
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
