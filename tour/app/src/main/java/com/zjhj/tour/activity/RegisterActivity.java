package com.zjhj.tour.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.soundcloud.android.crop.Crop;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.FileUtil;
import com.zjhj.commom.util.JGJBitmapUtils;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.SMSUtils;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;
import com.zjhj.tour.base.RequestCode;
import com.zjhj.tour.receiver.SMSBroadcastReceiver;
import com.zjhj.tour.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;
    @Bind(R.id.phone_et)
    EditText phoneEt;
    @Bind(R.id.clear_phone)
    ImageView clearPhone;
    @Bind(R.id.code_et)
    EditText codeEt;
    @Bind(R.id.clear_code)
    ImageView clearCode;
    @Bind(R.id.request_code)
    TextView requestCode;
    @Bind(R.id.identity_et)
    EditText identityEt;
    @Bind(R.id.clear_identity)
    ImageView clearIdentity;
    @Bind(R.id.guide_et)
    EditText guideEt;
    @Bind(R.id.clear_guide)
    ImageView clearGuide;
    @Bind(R.id.identit_ll)
    LinearLayout identitLl;
    @Bind(R.id.guide_ll)
    LinearLayout guideLl;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.identit_image)
    SimpleDraweeView identitImage;
    @Bind(R.id.guide_image)
    SimpleDraweeView guideImage;
    @Bind(R.id.recommend_et)
    EditText recommendEt;
    @Bind(R.id.clear_recommend)
    ImageView clearRecommend;

    /**
     * 短信验证倒计时--时长
     */
    private int i = 60;
    // 读取短信广播
    private SMSBroadcastReceiver smsBroadcastReceiver;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    SMSUtils.EventHandler eventHandler;

    PhotoDialog photoDialog;
    ArrayList<MapiImageResult> identityImgs;
    ArrayList<MapiImageResult> guideImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

        identityImgs = new ArrayList<>();
        guideImgs = new ArrayList<>();

        center.setText("注册");
        back.setImageResource(R.mipmap.back);

    }

    private void initListener() {
        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPhone.setVisibility(View.VISIBLE);
                } else {
                    clearPhone.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        codeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearCode.setVisibility(View.VISIBLE);
                } else {
                    clearCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        identityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearIdentity.setVisibility(View.VISIBLE);
                } else {
                    clearIdentity.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        guideEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearGuide.setVisibility(View.VISIBLE);
                } else {
                    clearGuide.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recommendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearRecommend.setVisibility(View.VISIBLE);
                } else {
                    clearRecommend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.back, R.id.clear_name, R.id.clear_phone, R.id.clear_code, R.id.request_code, R.id.clear_identity, R.id.clear_guide, R.id.identit_ll, R.id.guide_ll, R.id.submit
    ,R.id.clear_recommend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_phone:
                phoneEt.setText("");
                break;
            case R.id.clear_code:
                codeEt.setText("");
                break;
            case R.id.request_code:
                String phoneStr = phoneEt.getText().toString();

                if (TextUtils.isEmpty(phoneStr)) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }

                if (!StringUtil.isMobile(phoneStr)) {
                    MainToast.showShortToast("手机号格式不正确！");
                    return;
                }

                requestCode();
                break;
            case R.id.clear_identity:
                identityEt.setText("");
                break;
            case R.id.clear_guide:
                guideEt.setText("");
                break;
            case R.id.clear_recommend:
                recommendEt.setText("");
                break;
            case R.id.identit_ll:
                type = "0";
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("tour_identity_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.guide_ll:
                type = "1";
                if (photoDialog == null)
                    photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                photoDialog.setImagePath("tour_identity_image.jpg");
                photoDialog.showDialog();
                break;
            case R.id.submit:
                if (TextUtils.isEmpty(nameEt.getText())) {
                    MainToast.showShortToast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(phoneEt.getText())) {
                    MainToast.showShortToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(codeEt.getText())) {
                    MainToast.showShortToast("请输入验证码");
                    return;
                }

                if (TextUtils.isEmpty(identityEt.getText())) {
                    MainToast.showShortToast("请输入身份证号");
                    return;
                }

                if (TextUtils.isEmpty(guideEt.getText())) {
                    MainToast.showShortToast("请输入导游证号");
                    return;
                }

                if (TextUtils.isEmpty(guideEt.getText())) {
                    MainToast.showShortToast("请输入导游证号");
                    return;
                }

                if (null == identityImgs || identityImgs.isEmpty()) {
                    MainToast.showShortToast("请上传身份证正面");
                    return;
                }

                if (null == guideImgs || guideImgs.isEmpty()) {
                    MainToast.showShortToast("请上传导游证");
                    return;
                }

                showLoading();
                UserApi.register(this, nameEt.getText().toString(), phoneEt.getText().toString(), codeEt.getText().toString(), identityEt.getText().toString(),
                        guideEt.getText().toString(), identityImgs.get(0).getUrl(), guideImgs.get(0).getUrl(),recommendEt.getText().toString(), new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showLongToast("为了保障导游利益，平台将在24小时内确认申请，审核结果将以短信方式告知。");
                                finish();
                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(Integer code, String message) {
                                hideLoading();
                                MainToast.showShortToast(message);
                            }
                        });

                break;
        }
    }

    String type = "0";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.i("requestCode=" + requestCode + "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "tour_identity_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "tour_identity_image.jpg", FileUtil.TYPE_IMAGE);
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

                    if ("0".equals(type)) {
                        identityImgs.add(success);
                        identitImage.setVisibility(View.VISIBLE);
                        identitLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(identityImgs.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(identitImage.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        identitImage.setController(controller);
                    } else if ("1".equals(type)) {
                        guideImgs.add(success);
                        guideImage.setVisibility(View.VISIBLE);
                        guideLl.setVisibility(View.GONE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                        //创建将要下载的图片的URI
                        Uri imageUri = Uri.parse(guideImgs.get(0).getUrl());
                        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(165), DPUtil.dip2px(100)))
                                .build();
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .setOldController(guideImage.getController())
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        guideImage.setController(controller);
                    }


                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                DebugLog.i(message);
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
                .fromFile(FileUtil.createFile(this, "tour_identity_image.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    /**
     * 向服务器请求验证码
     */
    private void requestCode() {
        SMSUtils.requestCode(this, "REGISTER", phoneEt.getText().toString(), "");
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        requestCode.setClickable(false);
        requestCode.setFocusableInTouchMode(false);
        requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_color_divider));
        requestCode.setTextColor(getResources().getColor(R.color.shop_white));
        requestCode.setText("(" + i + "s)后重新获取");
        initHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        i = 30;
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        }).start();
    }

    private void initHandler() {
        eventHandler = new SMSUtils.EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = -7;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSUtils.registerEventHandler(eventHandler);
    }

    String mark = "";

    /**
     * 处理服务器返回的信息
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -9:
                    requestCode.setText("(" + i + "s)后重新获取");
                    break;
                case -8:
                    requestCode.setText("获取验证码");
                    requestCode.setClickable(true);
                    requestCode.setTextColor(getResources().getColor(R.color.shop_yellow));
                    requestCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rect_solid_white_stroke_yellow));
                    i = 60;
                    break;
                case -7:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    DebugLog.e("event=" + event);
                    if (result == SMSUtils.RESULT_COMPLETE) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE) {
                            mark = (String) data;
                            MainToast.showShortToast("验证码已经发送");
                        }
                    } else if (result == SMSUtils.RESULT_ERROR) {
                        if (event == SMSUtils.EVENT_GET_VERIFICATION_CODE_ERROR) {
                            MainToast.showShortToast((String) data);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        if (null != eventHandler)
            SMSUtils.unregisterEventHandler(eventHandler);
        if(null!=photoDialog) {
            photoDialog.dismiss();
            photoDialog = null;
        }
        super.onDestroy();
    }

}
