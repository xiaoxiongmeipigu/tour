package com.zjhj.tour.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DateUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerGridItemDecoration;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/11.
 */
public class DiscussItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM = 0;
    private final static int DIVIDER = 1;

    LayoutInflater inflater;
    Context mContext;


    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DiscussItemAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.item_discuss, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider, parent, false));
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
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.shop_tv)
        TextView shopTv;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.addtime)
        TextView addtime;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        @Bind(R.id.replay_ll)
        LinearLayout replayLl;


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
        MapiDiscussResult mapiDiscussResult = (MapiDiscussResult) mList.get(position).getData();
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        final ArrayList<MapiResourceResult> picList = mapiDiscussResult.getPic();
        if (null != picList && !picList.isEmpty()) {

            GridLayoutManager manager = new GridLayoutManager(mContext,4);
            holder.recyclerView.setLayoutManager(manager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            holder.recyclerView.setHasFixedSize(true);
            DiscussChildItemAdapter mAdapter = new DiscussChildItemAdapter(mContext, picList);
            holder.recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ControllerUtil.go2ShowBig(picList,position);
                }
            });
        }

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(mapiDiscussResult.getAvatar());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(40), DPUtil.dip2px(40)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.name.setText(TextUtils.isEmpty(mapiDiscussResult.getName()) ? "" : mapiDiscussResult.getName());
        String score = TextUtils.isEmpty(mapiDiscussResult.getScore())?"0":mapiDiscussResult.getScore();
        holder.ratingBar.setRating(Float.parseFloat(score));
        holder.content.setText(TextUtils.isEmpty(mapiDiscussResult.getContent())?"":mapiDiscussResult.getContent());
        String dateStr = TextUtils.isEmpty(mapiDiscussResult.getAddtime())?"":mapiDiscussResult.getAddtime();
        holder.addtime.setText(dateStr);

        if(!TextUtils.isEmpty(mapiDiscussResult.getReply())){
            holder.replayLl.setVisibility(View.VISIBLE);
            String str = "<font color='#1ea1f3'>商家回复: </font>"+mapiDiscussResult.getReply();
            holder.shopTv.setText(Html.fromHtml(str));
        }else{
            holder.replayLl.setVisibility(View.GONE);
        }


    }

}
