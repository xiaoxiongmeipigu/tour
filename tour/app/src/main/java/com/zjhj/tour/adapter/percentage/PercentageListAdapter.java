package com.zjhj.tour.adapter.percentage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/24.
 */
public class PercentageListAdapter extends RecyclerView.Adapter<PercentageListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiItemResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PercentageListAdapter(Context context, List<MapiItemResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_percentage_add, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);


        MapiItemResult itemResult = mList.get(position);
        holder.dateTv.setText(TextUtils.isEmpty(itemResult.getAddtime())?"":itemResult.getAddtime());
        if("0".equals(itemResult.getType())){
            holder.titleTv.setText("推广"+(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name()));
        }else if("1".equals(itemResult.getType())){
            holder.titleTv.setText("密友"+(TextUtils.isEmpty(itemResult.getChild_guide_name())?"":itemResult.getChild_guide_name())+"推广"+(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name()));
        }else if("2".equals(itemResult.getType())){
            holder.titleTv.setText("朋友"+(TextUtils.isEmpty(itemResult.getChild_guide_name())?"":itemResult.getChild_guide_name())+"推广"+(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name()));
        }else if("3".equals(itemResult.getType())){
            holder.titleTv.setText("推广"+(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name())+"订单奖金");
        }else if("4".equals(itemResult.getType())){
            holder.titleTv.setText("提现");
        }

        if(itemResult.getType().equals("4")){
            holder.priceTv.setTextColor(Color.parseColor("#5cc06b"));
            holder.priceTv.setText("-"+(TextUtils.isEmpty(itemResult.getMoney())?"0":itemResult.getMoney()));
        }else{
            holder.priceTv.setTextColor(Color.parseColor("#ff5f5e"));
            holder.priceTv.setText("+"+(TextUtils.isEmpty(itemResult.getMoney())?"0":itemResult.getMoney()));
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.price_tv)
        TextView priceTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
