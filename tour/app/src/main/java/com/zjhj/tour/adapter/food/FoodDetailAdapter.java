package com.zjhj.tour.adapter.food;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.FoodInfoLayout;
import com.zjhj.tour.view.FoodItemLayout;
import com.zjhj.tour.view.ShopInfoLayout;
import com.zjhj.tour.view.ShopItemLayout;
import com.zjhj.tour.view.ShopUserLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/10.
 */
public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int INFO = 0;
    private final static int ITEM = 1;
    private final static int USER = 2;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    String id = "";

    public void setId(String id){
        this.id = id;
    }

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FoodDetailAdapter(Context context, List<IndexData> list) {
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
            case INFO:
                return new InfoViewHolder(inflater.inflate(R.layout.lay_food_info, parent, false));
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.lay_food_item, parent, false));
            case USER:
                return new UserViewHolder(inflater.inflate(R.layout.lay_shop_user, parent, false));
            default:
                return new InfoViewHolder(inflater.inflate(R.layout.lay_food_info, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InfoViewHolder) {
            ((InfoViewHolder) holder).foodInfoLayout.load((MapiFoodResult) mList.get(position).getData());
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).foodItemLayout.load((List<MapiFoodResult>) mList.get(position).getData());
        } else if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).shopUserLayout.load((List<MapiDiscussResult>) mList.get(position).getData(),id,"1");
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "INFO":
                return INFO;
            case "ITEM":
                return ITEM;
            case "USER":
                return USER;
            default:
                return INFO;
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foodInfoLayout)
        FoodInfoLayout foodInfoLayout;
        public InfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foodItemLayout)
        FoodItemLayout foodItemLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.shopUserLayout)
        ShopUserLayout shopUserLayout;
        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
