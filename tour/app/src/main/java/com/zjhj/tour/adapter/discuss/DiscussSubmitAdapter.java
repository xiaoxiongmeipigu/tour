package com.zjhj.tour.adapter.discuss;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/7.
 */
public class DiscussSubmitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int CAMERA = 0;
    private final static int ITEM = 1;

    LayoutInflater inflater;

    private List<IndexData> mList;
    private Context mContext;

    public DiscussSubmitAdapter(Context context, List<IndexData> list) {
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
            case CAMERA:
                return new CameraViewHolder(inflater.inflate(R.layout.item_discuss_submit_camera, parent, false));
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.item_discuss_submit, parent, false));
            default:
                return new CameraViewHolder(inflater.inflate(R.layout.item_discuss_submit_camera, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            setCamera((CameraViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "CAMERA":
                return CAMERA;
            case "ITEM":
                return ITEM;
            default:
                return CAMERA;
        }
    }

    class CameraViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public CameraViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.del_iv)
        ImageView delIv;
        @Bind(R.id.root_view)
        RelativeLayout rootView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setCamera(CameraViewHolder holder, int position) {
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onDiscussItemClickListener)
                    onDiscussItemClickListener.onCamera(view, (Integer) view.getTag());
            }
        });
    }

    private void setItem(ItemViewHolder holder, int position) {

        MapiResourceResult resourceResult = (MapiResourceResult) mList.get(position).getData();

        //创建将要下载的图片的URI
        Uri imageUri2 = Uri.parse(TextUtils.isEmpty(resourceResult.getPic_url())?"":resourceResult.getPic_url());
        ImageRequest request2 = ImageRequestBuilder.newBuilderWithSource(imageUri2)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(105), DPUtil.dip2px(80)))
                .build();
        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request2)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller2);

        holder.delIv.setTag(position);
        holder.delIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onDiscussItemClickListener)
                    onDiscussItemClickListener.onDelClicl(view, (Integer) view.getTag());
            }
        });

        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onDiscussItemClickListener)
                    onDiscussItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

    }

    private DiscussOnItemClickListener onDiscussItemClickListener;

    public void setOnItemClickListener(DiscussOnItemClickListener onDiscussItemClickListener) {
        this.onDiscussItemClickListener = onDiscussItemClickListener;
    }

    public interface DiscussOnItemClickListener {
        void onItemClick(View view, int position);

        void onDelClicl(View view, int position);

        void onCamera(View view, int position);
    }

}
