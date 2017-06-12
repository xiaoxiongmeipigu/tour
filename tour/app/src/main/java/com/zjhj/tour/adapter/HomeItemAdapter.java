package com.zjhj.tour.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/5.
 */
public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM = 0;
    private final static int DIVIDER = 1;
    private final static int ITEM_SMALL = 2;
    private final static int DIVIDER_LINE = 3;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeItemAdapter(Context context, List<IndexData> list) {
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
                return new ItemViewHolder(inflater.inflate(R.layout.item_home_item, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_white, parent, false));
            case ITEM_SMALL:
                return new ItemSmallViewHolder(inflater.inflate(R.layout.item_home_small_item, parent, false));
            case DIVIDER_LINE:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_white, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        } else if (holder instanceof ItemSmallViewHolder) {
            setSmallItem((ItemSmallViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            case "ITEM_SMALL":
                return ITEM_SMALL;
            case "DIVIDER_LINE":
                return DIVIDER_LINE;
            default:
                return DIVIDER;
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.info)
        TextView info;
        @Bind(R.id.customer_consumption)
        TextView customerConsumption;
        @Bind(R.id.distance)
        TextView distance;
        @Bind(R.id.root_view)
        RelativeLayout rootView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemSmallViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.info)
        TextView info;
        @Bind(R.id.customer_consumption)
        TextView customerConsumption;
        @Bind(R.id.distance)
        TextView distance;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public ItemSmallViewHolder(View itemView) {
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
        MapiItemResult mapiItemResult = (MapiItemResult) mList.get(position).getData();
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        if(!TextUtils.isEmpty(mapiItemResult.getCover_pic())){
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(TextUtils.isEmpty(mapiItemResult.getCover_pic())?"":mapiItemResult.getCover_pic());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(180)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(holder.image.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            holder.image.setController(controller);
        }

        holder.title.setText(TextUtils.isEmpty(mapiItemResult.getName())?"":mapiItemResult.getName());
        holder.price.setText(TextUtils.isEmpty(mapiItemResult.getDiscount_rate())?"":mapiItemResult.getDiscount_rate()+"折");
        holder.customerConsumption.setText(TextUtils.isEmpty(mapiItemResult.getCustomer_consumption())?"":mapiItemResult.getCustomer_consumption()+"元/每人起做");
        holder.info.setText(TextUtils.isEmpty(mapiItemResult.getFeature())?"":mapiItemResult.getFeature());
        holder.distance.setText(TextUtils.isEmpty(mapiItemResult.getDistance())?"":mapiItemResult.getDistance());

    }

    private void setSmallItem(ItemSmallViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiItemResult mapiItemResult = (MapiItemResult) mList.get(position).getData();
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        if(!TextUtils.isEmpty(mapiItemResult.getCover_pic())){
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(mapiItemResult.getCover_pic());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(360), DPUtil.dip2px(180)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(holder.image.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            holder.image.setController(controller);
        }

        holder.title.setText(TextUtils.isEmpty(mapiItemResult.getName())?"":mapiItemResult.getName());
        holder.price.setText(TextUtils.isEmpty(mapiItemResult.getDiscount_rate())?"":mapiItemResult.getDiscount_rate()+"折");
        holder.customerConsumption.setText(TextUtils.isEmpty(mapiItemResult.getCustomer_consumption())?"":mapiItemResult.getCustomer_consumption()+"元/每人起做");
        holder.info.setText(TextUtils.isEmpty(mapiItemResult.getFeature())?"":mapiItemResult.getFeature());
        holder.distance.setText(TextUtils.isEmpty(mapiItemResult.getDistance())?"":mapiItemResult.getDistance());
    }


}
