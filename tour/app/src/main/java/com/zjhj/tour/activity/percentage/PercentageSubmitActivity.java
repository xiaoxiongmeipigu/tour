package com.zjhj.tour.activity.percentage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiBankResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PercentageSubmitActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.bank_tv)
    TextView bankTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.money_et)
    EditText moneyEt;
    @Bind(R.id.name_tv)
    EditText nameTv;
    @Bind(R.id.bank_code_tv)
    EditText bankCodeTv;


    OptionsPickerView positionOptions;
    ArrayList<MapiBankResult> posOptions1Items = new ArrayList<>();
    String options1Str;
    String money;
    int moneyInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage_submit);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            money = getIntent().getStringExtra("money");
        }
        initView();
        load();
        initListener();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("申请提现");
        money = TextUtils.isEmpty(money)?"0":money;
        moneyTv.setText("剩余金额："+money);
        moneyInt = Integer.parseInt(money);
        //选项选择器
        positionOptions = new OptionsPickerView(this);

    }

    private void initListener() {
        positionOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                options1Str = posOptions1Items.get(options1).getPickerViewText();
                bankTv.setText(TextUtils.isEmpty(options1Str) ? "" : options1Str);
            }
        });
    }

    private void load() {
        MapiBankResult resourceResult1 = new MapiBankResult();
        resourceResult1.setName("中国银行");

        MapiBankResult resourceResult2 = new MapiBankResult();
        resourceResult2.setName("中国农业银行");

        MapiBankResult resourceResult3 = new MapiBankResult();
        resourceResult3.setName("中国工商银行");

        MapiBankResult resourceResult4 = new MapiBankResult();
        resourceResult4.setName("中国建设银行");

        MapiBankResult resourceResult5 = new MapiBankResult();
        resourceResult5.setName("中国交通银行");

        posOptions1Items.add(resourceResult1);
        posOptions1Items.add(resourceResult2);
        posOptions1Items.add(resourceResult3);
        posOptions1Items.add(resourceResult4);
        posOptions1Items.add(resourceResult5);

        //三级联动效果
        positionOptions.setPicker(posOptions1Items);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
        positionOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        positionOptions.setSelectOptions(0);

    }

    @OnClick({R.id.back, R.id.bank_ll, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bank_ll:
                positionOptions.show();
                break;
            case R.id.apply:
                if(TextUtils.isEmpty(moneyEt.getText())){
                    MainToast.showShortToast("请输入转出金额");
                    return;
                }
                String cashStr = TextUtils.isEmpty(moneyEt.getText())?"":moneyEt.getText().toString();
                int cashInt = Integer.parseInt(cashStr);

                if(moneyInt<cashInt){
                    MainToast.showShortToast("转出金额不能超出剩余金额");
                    return;
                }

                if(cashInt<100){
                    MainToast.showShortToast("转出金额必须大于100");
                    return;
                }

                if(TextUtils.isEmpty(nameTv.getText())){
                    MainToast.showShortToast("请输入持卡人姓名");
                    return;
                }

                if(TextUtils.isEmpty(bankCodeTv.getText())){
                    MainToast.showShortToast("请输入卡号");
                    return;
                }

                if(TextUtils.isEmpty(options1Str)){
                    MainToast.showShortToast("请选择银行");
                    return;
                }
                apply();
                break;
        }
    }

    private void apply() {
        showLoading();
        UserApi.userapply(this, moneyEt.getText().toString(), nameTv.getText().toString(), bankCodeTv.getText().toString(), options1Str
                , new RequestCallback() {
                    @Override
                    public void success(Object success) {
                        hideLoading();
                        MainToast.showLongToast("您的提现申请平台将在3-5个工作日内处理");
                        setResult(RESULT_OK);
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

}
