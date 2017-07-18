package com.zjhj.tour.adapter.percentage;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/26.
 */
public class PertnerShopAdapter extends RecyclerView.Adapter<PertnerShopAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiItemResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PertnerShopAdapter(Context context, List<MapiItemResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_partner_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        MapiItemResult itemResult = mList.get(position);

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(itemResult.getCover_pic()) ? "" : itemResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(90), DPUtil.dip2px(70)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.nameTv.setText(TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName());
        holder.addrTv.setText("地址："+(TextUtils.isEmpty(itemResult.getAddress())?"":itemResult.getAddress()));

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.addr_tv)
        TextView addrTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
