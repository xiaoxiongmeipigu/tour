package com.zjhj.tour.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.AdapterSelListener;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.view.PurcaseSheetLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/15.
 */
public class PurcaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM = 0;
    private final static int DIVIDER = 1;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    AdapterSelListener listener;

    public void setOnAdapterSelListener(AdapterSelListener listener) {
        this.listener = listener;
    }

    private boolean isDel = false;

    public void setDel(boolean del) {
        isDel = del;
    }

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PurcaseAdapter(Context context, List<IndexData> list) {
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
                return new ItemViewHolder(inflater.inflate(R.layout.item_purcase, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_sel)
        ImageView itemSel;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.purcaseSheetLayout)
        PurcaseSheetLayout purcaseSheetLayout;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.price_tv)
        TextView priceTv;
        @Bind(R.id.name)
        TextView name;

        public ItemViewHolder(View itemView) {
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
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.itemSel.setTag(position);
        holder.itemSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (int) view.getTag();
                if (null != listener) {
                    listener.notifyChildStatus(position);
                }

            }
        });

//        holder.image.setImageURI(Uri.parse("res:///" + R.drawable.item_food_default));

        holder.purcaseSheetLayout.setCountEdit(false);
        if (isDel) {
            holder.purcaseSheetLayout.setCanDo(false);
        } else {
            holder.purcaseSheetLayout.setCanDo(true);
        }


        MapiCarResult mapiCarResult = (MapiCarResult) mList.get(position).getData();

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(mapiCarResult.getCover_pic())?"":mapiCarResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(90), DPUtil.dip2px(70)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        if (mapiCarResult.isSel())
            holder.itemSel.setImageResource(R.mipmap.circle_sel_right);
        else
            holder.itemSel.setImageResource(R.mipmap.circle_sel);

        holder.purcaseSheetLayout.setTag(position);
        holder.purcaseSheetLayout.setNumListener(new PurcaseSheetLayout.NumInterface() {
            @Override
            public void modify(View view, int num, String price) {
                int position = (int) view.getTag();
                if (null != listener) {
                    listener.notifyChildNum(position, num);
                }

            }
        });

        holder.priceTv.setText("¥" + (TextUtils.isEmpty(mapiCarResult.getOriginal_total_price()) ? "0" : mapiCarResult.getOriginal_total_price()));
        holder.name.setText(TextUtils.isEmpty(mapiCarResult.getName())?"":mapiCarResult.getName());
        String numStr = TextUtils.isEmpty(mapiCarResult.getNum())?"1":mapiCarResult.getNum();
        holder.purcaseSheetLayout.setNum(Integer.parseInt(numStr));

    }

}
