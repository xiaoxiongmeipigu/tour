package com.zjhj.tour.activity.discuss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.adapter.discuss.DiscussSubmitAdapter;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.util.ControllerUtil;
import com.zjhj.tour.widget.DividerListItemDecoration;
import com.zjhj.tour.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscussActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.ratingBarOne)
    RatingBar ratingBarOne;
    @Bind(R.id.ratingBarTwo)
    RatingBar ratingBarTwo;
    @Bind(R.id.ratingBarThree)
    RatingBar ratingBarThree;
    @Bind(R.id.ratingBarFour)
    RatingBar ratingBarFour;
    @Bind(R.id.ratingBarFive)
    RatingBar ratingBarFive;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.content_et)
    EditText contentEt;
    @Bind(R.id.name_tv)
    TextView nameTv;

    List<IndexData> mLsit;
    ArrayList<MapiResourceResult> imgs;

    DiscussSubmitAdapter mAdapter;
    PhotoDialog photoDialog;

    MapiItemResult itemResult;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            itemResult = (MapiItemResult) getIntent().getSerializableExtra("item");
            position = getIntent().getIntExtra("position",-1);
        }
        if(null!=itemResult){
            initView();
            initListener();
        }
    }

    private void initView() {

        mLsit = new ArrayList<>();
        imgs = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("评价");
        nameTv.setText(TextUtils.isEmpty(itemResult.getMerchant_name())?"":itemResult.getMerchant_name());

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(4), getResources().getColor(R.color.background_gray)));
        mAdapter = new DiscussSubmitAdapter(this, mLsit);
        recyclerView.setAdapter(mAdapter);

        mLsit.add(new IndexData(mLsit.size(), "CAMERA", new Object()));
        mAdapter.notifyDataSetChanged();

        photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
        photoDialog.setImagePath("tour_discuss_image.jpg");
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new DiscussSubmitAdapter.DiscussOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {//进入图片预览
                getImgs();
                int pos = 0;
                if(imgs.size()>=9)
                    pos = position;
                else
                  pos = position-1;
                ControllerUtil.go2ShowBig(imgs,pos);
            }

            @Override
            public void onDelClicl(View view, int position) {//删除
                getImgs();
                mLsit.remove(position);
                DebugLog.i(imgs.size() + ":imgs.size");
                if (imgs.size() >= 9) {

                    mLsit.add(0, new IndexData(0, "CAMERA", new Object()));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCamera(View view, int position) {//上传图片
                getImgs();
                if (imgs.size() >= 9) {
                    MainToast.showShortToast("最多上传9张图片");
                    return;
                }
                photoDialog.showDialog();
            }
        });
    }

    private ArrayList<MapiResourceResult> getImgs() {
        imgs.clear();
        for (IndexData indexData : mLsit) {
            DebugLog.i("type==>" + indexData.getType());
            if (indexData.getType().equals("ITEM")) {

                MapiResourceResult resourceResult = (MapiResourceResult) indexData.getData();
                imgs.add(resourceResult);
            }

        }
        return imgs;
    }

    @OnClick({R.id.back, R.id.upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.upload:
                if (TextUtils.isEmpty(contentEt.getText())) {
                    MainToast.showShortToast("请输入评价内容");
                    return;
                }

                if(null!=itemResult){
                    upload();
                }

                break;
        }
    }

    private void upload() {

        getImgs();
        String jsonString = JSON.toJSONString(imgs);
        DebugLog.i("jsonString=>"+jsonString);

        UserApi.orderaddcomment(this, itemResult.getOrder_id(), ratingBarOne.getRating() + "", ratingBarTwo.getRating() + "", ratingBarThree.getRating() + ""
                , ratingBarFive.getRating() + "", ratingBarFive.getRating() + "", contentEt.getText().toString(), jsonString, new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showShortToast("评论提交成功，审核通过后可见");
                        Intent intent = new Intent();
                        intent.putExtra("position",position);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.i("requestCode=" + requestCode + "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "tour_discuss_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "tour_discuss_image.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
            }
        }
    }

    private void uploadImage(File file) {
        showLoading();
        CommonApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {
                    MapiResourceResult resourceResult = new MapiResourceResult();
                    resourceResult.setPic_url(success.getUrl());
                    mLsit.add(new IndexData(mLsit.size(), "ITEM", resourceResult));
                    if (imgs.size() == 8) {
                        mLsit.remove(0);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "tour_discuss_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).withMaxSize(600, 600).start(this);
//        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
    }
}
