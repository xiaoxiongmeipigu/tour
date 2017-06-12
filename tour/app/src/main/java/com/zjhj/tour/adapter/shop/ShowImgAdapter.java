package com.zjhj.tour.adapter.shop;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.util.ControllerUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/22.
 */
public class ShowImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPEONE = 0;
    private final static int TYPETWO = 1;
    private final static int TYPETHREE = 2;
    private final static int DIVIDER = 3;

    LayoutInflater inflater;

    private List<IndexData> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ShowImgAdapter(Context context, List<IndexData> list) {
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
            case TYPEONE:
                return new TypeOneViewHolder(inflater.inflate(R.layout.layout_shop_img_one, parent, false));
            case TYPETWO:
                return new TypeTwoViewHolder(inflater.inflate(R.layout.layout_shop_img_one, parent, false));
            case TYPETHREE:
                return new TypeThreeViewHolder(inflater.inflate(R.layout.layout_shop_img_one, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_white, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_white, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeOneViewHolder) {
            setTypeOne((TypeOneViewHolder) holder, position);
        } else if (holder instanceof TypeTwoViewHolder) {
            setTypeTwo((TypeTwoViewHolder) holder, position);
        } else if (holder instanceof TypeThreeViewHolder) {
            setTypeThree((TypeThreeViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "TYPEONE":
                return TYPEONE;
            case "TYPETWO":
                return TYPETWO;
            case "TYPETHREE":
                return TYPETHREE;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }

    class TypeOneViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.count_tv)
        TextView countTv;
        @Bind(R.id.type_rl)
        RelativeLayout typeRl;
        public TypeOneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TypeTwoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.count_tv)
        TextView countTv;
        @Bind(R.id.type_rl)
        RelativeLayout typeRl;
        public TypeTwoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TypeThreeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.count_tv)
        TextView countTv;
        @Bind(R.id.type_rl)
        RelativeLayout typeRl;
        public TypeThreeViewHolder(View itemView) {
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

    private void setTypeOne(TypeOneViewHolder holder, int position) {
        List<MapiResourceResult> list = (List<MapiResourceResult>) mList.get(position).getData();
        holder.title.setText("外观");
        holder.countTv.setText("("+list.size()+")");
        //创建将要下载的图片的URI
        Uri imageUri1 = Uri.parse(list.get(0).getPic_url());
        ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(imageUri1)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(175)))
                .build();
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request1)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller1);

        holder.typeRl.setTag(position);
        holder.typeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

    }

    private void setTypeTwo(TypeTwoViewHolder holder, int position) {
        List<MapiResourceResult> list = (List<MapiResourceResult>) mList.get(position).getData();
        holder.title.setText("内部");
        holder.countTv.setText("("+list.size()+")");
        //创建将要下载的图片的URI
        Uri imageUri1 = Uri.parse(list.get(0).getPic_url());
        ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(imageUri1)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(175)))
                .build();
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request1)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller1);

        holder.typeRl.setTag(position);
        holder.typeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

    }

    private void setTypeThree(TypeThreeViewHolder holder, int position) {
        List<MapiResourceResult> list = (List<MapiResourceResult>) mList.get(position).getData();
        holder.title.setText("其它");
        holder.countTv.setText("("+list.size()+")");
        //创建将要下载的图片的URI
        Uri imageUri1 = Uri.parse(list.get(0).getPic_url());
        ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(imageUri1)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(175)))
                .build();
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request1)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller1);

        holder.typeRl.setTag(position);
        holder.typeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

    }

}
