package com.zjhj.tour.adapter.order;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/15.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private LayoutInflater inflater;
    List<MapiItemResult> mList;
    private RecyOnItemClickListener onItemClickListener;

    private BaseActivity activity;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OrderItemAdapter(Context context, List<MapiItemResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        activity = (BaseActivity) context;
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        MapiItemResult itemResult = mList.get(position);

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(itemResult.getMerchant_cover_pic())?"":itemResult.getMerchant_cover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(90), DPUtil.dip2px(70)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        List<MapiFoodResult> foodList = itemResult.getOrder_detail_list();
        if(null!=foodList){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            OrderItemTextAdapter mAdapter = new OrderItemTextAdapter(activity, foodList);
            holder.recyclerView.setAdapter(mAdapter);
        }


        holder.cancel.setTag(position);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if(null!=orderDeelListener)
                    orderDeelListener.cancel(pos);
            }
        });

        holder.care.setTag(position);
        holder.care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if(null!=orderDeelListener)
                    orderDeelListener.care(pos);
            }
        });

        holder.nameTv.setText(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name());
        String dateStr = TextUtils.isEmpty(itemResult.getUse_date())?"":itemResult.getUse_date();
        String use_begin_time = TextUtils.isEmpty(itemResult.getUse_begin_time())?"":itemResult.getUse_begin_time();
        String use_end_time = TextUtils.isEmpty(itemResult.getUse_end_time())?"":itemResult.getUse_end_time();
        holder.dateTv.setText("就餐时间："+dateStr+"  "+use_begin_time+"-"+use_end_time);
        holder.priceTv.setText("总计： ¥"+(TextUtils.isEmpty(itemResult.getTotal_price())?"0":itemResult.getTotal_price()));

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cancel)
        TextView cancel;
        @Bind(R.id.care)
        TextView care;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.price_tv)
        TextView priceTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OrderDeelListener{
        void cancel(int position);
        void care(int position);
    }

    private OrderDeelListener orderDeelListener;

    public void setOrderDeelListener(OrderDeelListener orderDeelListener){
        this.orderDeelListener = orderDeelListener;
    }


}
