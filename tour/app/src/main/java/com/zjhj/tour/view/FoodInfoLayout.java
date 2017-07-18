package com.zjhj.tour.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zjhj.commom.result.MapiFoodResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/11.
 */
public class FoodInfoLayout extends RelativeLayout {

    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.merchant_name)
    TextView merchantName;
    @Bind(R.id.set_meal_name)
    TextView setMealName;
    @Bind(R.id.sales_volume)
    TextView salesVolume;
    @Bind(R.id.original_price)
    TextView originalPrice;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    private Context mContext;
    private View view;

    MapiFoodResult mapiFoodResult;

    public FoodInfoLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public FoodInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public FoodInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_food_info, this);
        ButterKnife.bind(this, view);
    }

    private void initListener() {

    }

    public void load(MapiFoodResult mapiFoodResult) {


        if (null != mapiFoodResult) {
            this.mapiFoodResult = mapiFoodResult;
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(TextUtils.isEmpty(mapiFoodResult.getCover_pic()) ? "" : mapiFoodResult.getCover_pic());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(170)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(image.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            image.setController(controller);

            merchantName.setText(TextUtils.isEmpty(mapiFoodResult.getMerchant_name()) ? "" : mapiFoodResult.getMerchant_name());
            setMealName.setText(TextUtils.isEmpty(mapiFoodResult.getSet_meal_name()) ? "" : mapiFoodResult.getSet_meal_name());
            salesVolume.setText("已售:  " + (TextUtils.isEmpty(mapiFoodResult.getSales_volume()) ? "" : mapiFoodResult.getSales_volume()));
            originalPrice.setText(TextUtils.isEmpty(mapiFoodResult.getOriginal_price()) ? "" : mapiFoodResult.getOriginal_price());
            if (TextUtils.isEmpty(mapiFoodResult.getContent()))
                contentLl.setVisibility(View.GONE);
            else {
                contentLl.setVisibility(View.VISIBLE);
                contentTv.setText(mapiFoodResult.getContent());
            }
        }

    }

}
