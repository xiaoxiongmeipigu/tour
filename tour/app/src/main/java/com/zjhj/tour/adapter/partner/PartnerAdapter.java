package com.zjhj.tour.adapter.partner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.PartnerCloseLayout;
import com.zjhj.tour.view.PartnerFriendLayout;
import com.zjhj.tour.view.PartnerShopLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/27.
 */
public class PartnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int CLOSE = 0;
    private final static int FRIEND = 1;
    private final static int SHOP = 2;
    private final static int DIVIDER = 3;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PartnerAdapter(Context context, List<IndexData> list) {
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
            case CLOSE:
                return new CloseViewHolder(inflater.inflate(R.layout.lay_partner_close, parent, false));
            case FRIEND:
                return new FriendViewHolder(inflater.inflate(R.layout.lay_partner_friend, parent, false));
            case SHOP:
                return new ShopViewHolder(inflater.inflate(R.layout.lay_partner_shop, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_white, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CloseViewHolder) {
            ((CloseViewHolder) holder).partnerCloseLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        } else if (holder instanceof FriendViewHolder) {
            ((FriendViewHolder) holder).partnerFriendLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }else if (holder instanceof ShopViewHolder) {
            ((ShopViewHolder) holder).partnerShopLayout.load((List<MapiItemResult>) mList.get(position).getData());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "CLOSE":
                return CLOSE;
            case "FRIEND":
                return FRIEND;
            case "SHOP":
                return SHOP;
            default:
                return DIVIDER;
        }
    }

    class CloseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.partnerCloseLayout)
        PartnerCloseLayout partnerCloseLayout;

        public CloseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.partnerFriendLayout)
        PartnerFriendLayout partnerFriendLayout;

        public FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.partnerShopLayout)
        PartnerShopLayout partnerShopLayout;
        public ShopViewHolder(View itemView) {
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

}
