package com.zjhj.tour.view;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.ShopPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/8/30.
 */
public class HomeSliderLayout extends RelativeLayout {
    @Bind(R.id.index_viewpager)
    ViewPager indexViewpager;
    @Bind(R.id.guide_dot)
    LinearLayout guideDot;
    @Bind(R.id.search_et)
    EditText searchEt;
    @Bind(R.id.clear_iv)
    ImageView clearIv;

    private Context mContext;
    private View view;
    List<View> sliderViewList;

    public HomeSliderLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_slider, this);
        ButterKnife.bind(this, view);
    }

    private void initListener(){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                } else {
                    clearIv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void load(List<MapiResourceResult> list) {
        if (null != list) {//&&list.size()>0
            sliderViewList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.layout_draweeview, null);
                if (i == 2) {
                    view.setImageURI(Uri.parse("res:///" + R.drawable.slider_three));
                } else if (i == 1) {
                    view.setImageURI(Uri.parse("res:///" + R.drawable.slider_two));
                } else if (i == 0) {
                    view.setImageURI(Uri.parse("res:///" + R.drawable.slider_one));
                }
                //创建将要下载的图片的URI
                /*Uri imageUri = Uri.parse(list.get(i).getImg());
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                        .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(250)))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(view.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>())
                        .build();
                view.setController(controller);*/


                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                sliderViewList.add(view);
            }
            ShopPagerAdapter sliderAdapter = new ShopPagerAdapter(sliderViewList);
            indexViewpager.setAdapter(sliderAdapter);
            guideDot.removeAllViews();
            for (int i = 0; i < sliderViewList.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPUtil.dip2px(8), DPUtil.dip2px(8));
                params.setMargins(DPUtil.dip2px(9), 0, DPUtil.dip2px(9), DPUtil.dip2px(10));
                imageView.setLayoutParams(params);
                imageView.setBackgroundResource(R.drawable.selector_item_dot);
                guideDot.addView(imageView);
            }
            guideDot.getChildAt(0).setSelected(true);
            indexViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < sliderViewList.size(); i++) {
                        if (position == i)
                            guideDot.getChildAt(i).setSelected(true);
                        else
                            guideDot.getChildAt(i).setSelected(false);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }

            });
        }

    }

    @OnClick(R.id.clear_iv)
    public void onClick() {
        searchEt.setText("");
    }

}
