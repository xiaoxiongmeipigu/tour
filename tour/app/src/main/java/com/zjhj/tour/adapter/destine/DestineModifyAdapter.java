package com.zjhj.tour.adapter.destine;

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
import com.zjhj.tour.interfaces.PurcaseSheetListener;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.PurcaseSheetLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/6.
 */
public class DestineModifyAdapter extends RecyclerView.Adapter<DestineModifyAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiFoodResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private PurcaseSheetListener onPurcaseSheetListener;

    public void setOnPurcaseSheetListener(PurcaseSheetListener onPurcaseSheetListener) {
        this.onPurcaseSheetListener = onPurcaseSheetListener;
    }

    public DestineModifyAdapter(Context context, List<MapiFoodResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_destine_modify, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiFoodResult foodResult = mList.get(position);
        holder.setMealName.setText(TextUtils.isEmpty(foodResult.getSet_meal_name())?"":foodResult.getSet_meal_name());
        holder.originalSinglePrice.setText(TextUtils.isEmpty(foodResult.getOriginal_single_price())?"0元":foodResult.getOriginal_single_price()+"元");
        holder.purcaseSheetLayout.setCountEdit(false);
        String num = TextUtils.isEmpty(foodResult.getNum())?"1":foodResult.getNum();
        holder.purcaseSheetLayout.setNum(Integer.parseInt(num));
        holder.purcaseSheetLayout.setTag(position);
        holder.purcaseSheetLayout.setNumListener(new PurcaseSheetLayout.NumInterface() {
            @Override
            public void modify(View view, int num, String price) {
                if(null!=onPurcaseSheetListener)
                    onPurcaseSheetListener.notifyPurcaseSheet(view, (Integer) view.getTag(),num,price);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.set_meal_name)
        TextView setMealName;
        @Bind(R.id.original_single_price)
        TextView originalSinglePrice;
        @Bind(R.id.purcaseSheetLayout)
        PurcaseSheetLayout purcaseSheetLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
