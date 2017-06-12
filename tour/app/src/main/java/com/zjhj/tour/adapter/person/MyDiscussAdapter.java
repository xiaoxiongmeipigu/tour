package com.zjhj.tour.adapter.person;

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
import com.zjhj.commom.result.MapiDiscussResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.DiscussChildItemAdapter;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/12.
 */
public class MyDiscussAdapter extends RecyclerView.Adapter<MyDiscussAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiDiscussResult> mList;
    private RecyOnItemClickListener onItemClickListener;
    private Context mContext;
    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyDiscussAdapter(Context context, List<MapiDiscussResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_my_discuss, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiDiscussResult mapiDiscussResult = mList.get(position);
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

            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            holder.recyclerView.setLayoutManager(manager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(4), mContext.getResources().getColor(R.color.shop_white)));
            holder.recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.VERTICAL, DPUtil.dip2px(4), mContext.getResources().getColor(R.color.shop_white)));
            DiscussChildItemAdapter mAdapter = new DiscussChildItemAdapter(mContext, picList);
            holder.recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ControllerUtil.go2ShowBig(picList,position);
                }
            });
        }

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

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(mapiDiscussResult.getMerchant_cover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(50), DPUtil.dip2px(50)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.shopIv.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.shopIv.setController(controller);

        holder.shopName.setText(TextUtils.isEmpty(mapiDiscussResult.getMerchant_name())?"":mapiDiscussResult.getMerchant_name());
        holder.shopDesc.setText(TextUtils.isEmpty(mapiDiscussResult.getFeature())?"":mapiDiscussResult.getFeature());


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.addtime)
        TextView addtime;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        @Bind(R.id.shop_tv)
        TextView shopTv;
        @Bind(R.id.replay_ll)
        LinearLayout replayLl;
        @Bind(R.id.shop_iv)
        SimpleDraweeView shopIv;
        @Bind(R.id.shop_name)
        TextView shopName;
        @Bind(R.id.shop_desc)
        TextView shopDesc;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
