package com.zjhj.tour.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.HomeItemAdapter;
import com.zjhj.tour.adapter.PopItemAdapter;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.MaxHeightLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/9.
 */
public class ItemPopWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.maxLayout)
    MaxHeightLayout maxLayout;

    private LayoutInflater inflater = null;
    private View contentView = null;
    private int mWidth = 0;
    private Context mContext;
    List<MapiResourceResult> list;
    Activity activity;
    PopItemAdapter popItemAdapter;

    int oldPos = -1;

    public ItemPopWindow(Activity context, int width, List<MapiResourceResult> list, int style) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        activity = context;
        mWidth = width;
        this.list = list;
        initTopLayout(style);//R.style.PopupWindowAnimation
    }

    private void initTopLayout(int animaStyle) {
        contentView = inflater.inflate(R.layout.pop_item, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        if (mWidth == 0)
            this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        else
            // 设置弹出窗体的宽
            this.setWidth(mWidth);
        // 设置弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.maxLayout.setMaxHeight(1350);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 设置允许在外点击消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置popupwindow显示和隐藏时的动画
        this.setAnimationStyle(animaStyle);
        setOnDismissListener(this);
        initView();
        initListener();
    }

    private void initView() {

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(0.5f), mContext.getResources().getColor(R.color.divider_line)));
        recyclerView.setLayoutManager(linearLayoutManager);
        popItemAdapter = new PopItemAdapter(mContext, list);
        recyclerView.setAdapter(popItemAdapter);
    }

    public void refreshData(List<MapiResourceResult> data){
        this.list.addAll(data);
        popItemAdapter.notifyDataSetChanged();
}

    private void initListener(){
        popItemAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(position==oldPos) {

                }else{
                    if(oldPos>=0){
                        list.get(oldPos).setSel(false);
                    }
                    oldPos = position;
                    list.get(oldPos).setSel(true);
                    popItemAdapter.notifyDataSetChanged();
                    if(null!=mOnPopItemClickListener)
                        mOnPopItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    private RecyOnItemClickListener mOnPopItemClickListener;

    /**
     * 设置item的点击监听事件
     */
    public void setOnPopItemClickListener(RecyOnItemClickListener l) {
        mOnPopItemClickListener = l;
    }

    @Override
    public void onDismiss() {
        DebugLog.i("pop==onDismiss");
        if (null != mOnPopItemClickListener)
            mOnPopItemClickListener.onItemClick(null, 0);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
        } else {
            this.dismiss();
        }
    }

}
