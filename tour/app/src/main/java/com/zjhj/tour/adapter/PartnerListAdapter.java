package com.zjhj.tour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/26.
 */
public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    List<MapiResourceResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PartnerListAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_partner, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiResourceResult mapiResourceResult = mList.get(position);
        DebugLog.i("position:" + position);

        holder.nameTv.setText(TextUtils.isEmpty(mapiResourceResult.getGuide_name())?"":mapiResourceResult.getGuide_name());
        holder.numTv.setText("酒店数："+(TextUtils.isEmpty(mapiResourceResult.getMerchant_count())?"0":mapiResourceResult.getMerchant_count()));

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
