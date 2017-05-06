package com.zjhj.tour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.view.HomeItemHotLayout;
import com.zjhj.tour.view.HomeItemShopLayout;
import com.zjhj.tour.view.HomeSliderLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/6.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int SLIDER_IMAGE = 0;
    private final static int ITEM_HOT = 1;
    private final static int ITEM_SHOP = 2;

    LayoutInflater inflater;

    private List<IndexData> mList;

    public MainAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SLIDER_IMAGE:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_home_slider, parent, false));
            case ITEM_HOT:
                return new ItemHotViewHolder(inflater.inflate(R.layout.lay_home_item_hot, parent, false));
            case ITEM_SHOP:
                return new ItemShopViewHolder(inflater.inflate(R.layout.lay_home_item_shop, parent, false));
            default:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_home_slider, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderViewHolder) {
            ((SliderViewHolder)holder).homeSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }else if(holder instanceof ItemHotViewHolder){
            ((ItemHotViewHolder)holder).homeItemHotLayout.load((List<MapiItemResult>) mList.get(position).getData());
        }else if(holder instanceof ItemShopViewHolder){
            ((ItemShopViewHolder)holder).homeItemShopLayout.load((List<MapiItemResult>) mList.get(position).getData());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "SCROLL":
                return SLIDER_IMAGE;
            case "ITEM_HOT":
                return ITEM_HOT;
            case "ITEM_SHOP":
                return ITEM_SHOP;
            default:
                return SLIDER_IMAGE;
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeSliderLayout)
        HomeSliderLayout homeSliderLayout;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemHotViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.homeItemHotLayout)
        HomeItemHotLayout homeItemHotLayout;
        public ItemHotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemShopViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.homeItemShopLayout)
        HomeItemShopLayout homeItemShopLayout;
        public ItemShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
