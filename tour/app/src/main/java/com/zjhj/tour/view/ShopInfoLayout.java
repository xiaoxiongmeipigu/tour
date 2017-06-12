package com.zjhj.tour.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.ShopServiceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.MainAlertDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/5/10.
 */
public class ShopInfoLayout extends RelativeLayout {

    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.photo_num)
    TextView photoNum;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.discount_rate)
    TextView discountRate;
    @Bind(R.id.customer_consumption)
    TextView customerConsumption;
    @Bind(R.id.score_tv)
    TextView scoreTv;
    @Bind(R.id.sales_volume)
    TextView salesVolume;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.distance_tv)
    TextView distanceTv;
    @Bind(R.id.service_ll)
    LinearLayout serviceLl;
    @Bind(R.id.scenic_spot)
    TextView scenicSpot;
    @Bind(R.id.collect_iv)
    ImageView collectIv;

    private Context mContext;
    private View view;
    private BaseActivity activity;

    MapiItemResult mapiItemResult;

    MainAlertDialog callDialog;
    String service_tel = "";

    public ShopInfoLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    public ShopInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    public ShopInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_shop_info, this);
        ButterKnife.bind(this, view);
        callDialog = new MainAlertDialog(mContext);
    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(service_tel)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service_tel));
                    mContext.startActivity(intent);
                    callDialog.dismiss();
                }

            }
        });
    }

    public void load(MapiItemResult mapiItemResult) {
        if (null != mapiItemResult) {
            this.mapiItemResult = mapiItemResult;
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(TextUtils.isEmpty(mapiItemResult.getCover_pic()) ? "" : mapiItemResult.getCover_pic());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(170)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(image.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            image.setController(controller);

            photoNum.setText(TextUtils.isEmpty(mapiItemResult.getMerchant_pic_count()) ? "0张" : mapiItemResult.getMerchant_pic_count() + "张");

            name.setText(TextUtils.isEmpty(mapiItemResult.getName()) ? "" : mapiItemResult.getName());

            discountRate.setText(TextUtils.isEmpty(mapiItemResult.getDiscount_rate()) ? "" : mapiItemResult.getDiscount_rate() + "折");
            customerConsumption.setText(TextUtils.isEmpty(mapiItemResult.getCustomer_consumption()) ? "" : mapiItemResult.getCustomer_consumption() + "元/每人起做");

            String score = TextUtils.isEmpty(mapiItemResult.getScore()) ? "0" : mapiItemResult.getScore();
            float scoreFloat = Float.parseFloat(score);
            ratingBar.setRating(scoreFloat);

            if(mapiItemResult.getIs_favorite().equals("0"))
                collectIv.setImageResource(R.mipmap.uncollect);
            else if(mapiItemResult.getIs_favorite().equals("1"))
                collectIv.setImageResource(R.mipmap.collect);

            if(!TextUtils.isEmpty(mapiItemResult.getTel())){
                service_tel = mapiItemResult.getTel();
            }else{
                service_tel = mapiItemResult.getMobile();
            }
            callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);

            scoreTv.setText(TextUtils.isEmpty(score) ? "0.0" : score);
            salesVolume.setText("已售" + (TextUtils.isEmpty(mapiItemResult.getSales_volume()) ? "0" : mapiItemResult.getSales_volume()) + "单");
            addressTv.setText(TextUtils.isEmpty(mapiItemResult.getAddress()) ? "" : mapiItemResult.getAddress());
            distanceTv.setText("距我" + (TextUtils.isEmpty(mapiItemResult.getDistance()) ? "" : mapiItemResult.getDistance()));
            scenicSpot.setText("附近景点：" + (TextUtils.isEmpty(mapiItemResult.getScenic_spot()) ? "暂无" : mapiItemResult.getScenic_spot()));

            List<ShopServiceResult> serviceResultList = mapiItemResult.getService();
            if (null != serviceResultList && !serviceResultList.isEmpty()) {
                serviceLl.setWeightSum(3);
                for (int i = 0; i < serviceResultList.size(); i++) {
                    if (i < 3) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shop_detail, null);


                        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.image);
                        TextView textView = (TextView) view.findViewById(R.id.title);
                        textView.setText(TextUtils.isEmpty(serviceResultList.get(i).getName()) ? "" : serviceResultList.get(i).getName());

                        DebugLog.i(serviceResultList.get(i).getImg_url() + "");

                        //创建将要下载的图片的URI
                        Uri imageUri2 = Uri.parse(serviceResultList.get(i).getImg_url());
                        ImageRequest request2 = ImageRequestBuilder.newBuilderWithSource(imageUri2)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(23), DPUtil.dip2px(23)))
                                .build();
                        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request2)
                                .setOldController(imageView.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        imageView.setController(controller2);
                        serviceLl.addView(view);
                        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) view.getLayoutParams();
                        param.weight = 1.0f;
                        view.setLayoutParams(param);

                    }
                }
            }

        }
    }

    @OnClick({R.id.service_parent,R.id.image,R.id.collect_iv,R.id.phone_ll,R.id.addr_ll})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image:
                ControllerUtil.go2ShopImage(mapiItemResult);
                break;
            case R.id.service_parent:
                if(null!=mapiItemResult){
                    ControllerUtil.go2ServiceDetail(mapiItemResult);
                }
                break;
            case R.id.collect_iv:
                String action = "";
                if(mapiItemResult.getIs_favorite().equals("0"))
                    action = "ADD_FAVORITE";
                else if(mapiItemResult.getIs_favorite().equals("1"))
                    action = "REMOVE_FAVORITE";
                collectAction(action);
                break;
            case R.id.phone_ll:
                if(null!=callDialog)
                    callDialog.show();
                break;
            case R.id.addr_ll:
                ControllerUtil.go2InfoWindow(mapiItemResult);
                break;
        }
    }

    private void collectAction(String action){
        activity.showLoading();
        ItemApi.usersetfavorite(activity, mapiItemResult.getId(), action, new RequestCallback() {
            @Override
            public void success(Object success) {
                activity.hideLoading();
                if(mapiItemResult.getIs_favorite().equals("0")) {
                   collectIv.setImageResource(R.mipmap.collect);
                    mapiItemResult.setIs_favorite("1");
                }else if(mapiItemResult.getIs_favorite().equals("1")) {
                    collectIv.setImageResource(R.mipmap.uncollect);
                    mapiItemResult.setIs_favorite("0");
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                activity.hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

}
