package com.zjhj.tour.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/2/8.
 */
public class FilterPopWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    @Bind(R.id.recentflow)
    FlowLayout recentFlow;
    private LayoutInflater inflater = null;
    private View contentView = null;
    private int mWidth = 0;
    private Context mContext;
    List<MapiResourceResult> list = new ArrayList<>();
    Activity activity;
    int oldPos = -1;
    public FilterPopWindow(Activity context, int width, List<MapiResourceResult> list, int style) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        activity = context;
        mWidth = width;
        this.list = list;
        initTopLayout(style);//R.style.PopupWindowAnimation
    }

    private void initTopLayout(int animaStyle) {
        contentView = inflater.inflate(R.layout.pop_filter, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        if (mWidth == 0)
            this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        else
            // 设置弹出窗体的宽
            this.setWidth(mWidth);
        // 设置弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
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
    }

    private void initView() {
        for (int i=0;i<list.size();i++) {
            final MapiResourceResult resourceResult = list.get(i);
            final TextView textView = new TextView(mContext);
            textView.setTag(i);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(params);
            textView.setPadding(DPUtil.dip2px(8), DPUtil.dip2px(4), DPUtil.dip2px(8), DPUtil.dip2px(4));
            textView.setText(resourceResult.getName());
            textView.setGravity(Gravity.CENTER);
            if(oldPos==i) {
                textView.setTextColor(mContext.getResources().getColor(R.color.shop_white));
                textView.setBackgroundResource(R.drawable.rectangle_solid_orange_strike_divider_line_5);
            }else{
                textView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                textView.setBackgroundResource(R.drawable.rectangle_solid_white_strike_divider_line_5);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    if(pos==oldPos) {
                        oldPos = -1;
                        textView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                        textView.setBackgroundResource(R.drawable.rectangle_solid_white_strike_divider_line_5);
                    }else{

                        if(oldPos>=0){
                            TextView oldTextView = (TextView) recentFlow.getChildAt(oldPos);
                            oldTextView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                            oldTextView.setBackgroundResource(R.drawable.rectangle_solid_white_strike_divider_line_5);
                        }
                        oldPos = pos;
                        textView.setTextColor(mContext.getResources().getColor(R.color.shop_white));
                        textView.setBackgroundResource(R.drawable.rectangle_solid_orange_strike_divider_line_5);
                    }

                    if(null!=mOnPopItemClickListener)
                        mOnPopItemClickListener.onItemClick(view,oldPos);

                    dismiss();

                }
            });
            recentFlow.addView(textView);
        }
    }

    public void refreshData(List<MapiResourceResult> data){
        this.list = new ArrayList<>();
        this.list.addAll(data);
        recentFlow.removeAllViews();
        initView();
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
