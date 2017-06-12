package com.zjhj.tour.adapter.city;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.CityLocationLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/16.
 */
public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int LOCATION = 0;
    private final static int DIVIDER = 1;
    private final static int ITEM = 2;
    private final static int SEL = 3;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CityListAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.item_city, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
            case LOCATION:
                return new LocationViewHolder(inflater.inflate(R.layout.lay_city_location, parent, false));
            case SEL:
                return new ItemSelViewHolder(inflater.inflate(R.layout.item_city_sel, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        } else if (holder instanceof LocationViewHolder) {
            ((LocationViewHolder) holder).cityLocationLayout.load();
        }else if (holder instanceof ItemSelViewHolder) {
            setItemSel((ItemSelViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            case "LOCATION":
                return LOCATION;
            case "SEL":
                return SEL;
            default:
                return DIVIDER;
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.name)
        TextView name;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemSelViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        public ItemSelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cityLocationLayout)
        CityLocationLayout cityLocationLayout;
        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setItem(ItemViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiCityItemResult mapiCityItemResult = (MapiCityItemResult) mList.get(position).getData();
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.name.setText(TextUtils.isEmpty(mapiCityItemResult.getCity_name())?"":mapiCityItemResult.getCity_name());

    }

    private void setItemSel(ItemSelViewHolder holder, int position){
        DebugLog.i("position:" + position);
        String name = (String) mList.get(position).getData();
        holder.name.setText(TextUtils.isEmpty(name)?"":name);
    }

}
