package com.zjhj.tour.adapter.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/15.
 */
public class OrderItemTextAdapter extends RecyclerView.Adapter<OrderItemTextAdapter.ViewHolder> {
    private LayoutInflater inflater;
    List<MapiFoodResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    private BaseActivity activity;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OrderItemTextAdapter(Context context, List<MapiFoodResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        activity = (BaseActivity) context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order_text, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiFoodResult foodResult = mList.get(position);
        holder.nameTv.setText(TextUtils.isEmpty(foodResult.getSet_meal_name())?"":foodResult.getSet_meal_name());
        holder.numTv.setText(TextUtils.isEmpty(foodResult.getSet_meal_num())?"X 1":"X "+foodResult.getSet_meal_num());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.num_tv)
        TextView numTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
