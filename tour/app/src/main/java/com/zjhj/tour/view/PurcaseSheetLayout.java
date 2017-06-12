package com.zjhj.tour.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.tour.R;
import com.zjhj.tour.base.BaseActivity;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/9/8.
 */
public class PurcaseSheetLayout extends RelativeLayout {
    @Bind(R.id.cut)
    TextView cut;
    @Bind(R.id.count)
    EditText count;
    @Bind(R.id.add)
    TextView add;
    private Context mContext;
    private View view;
    private int num = 1;

    String rec_id;
    BaseActivity activity;

    private boolean canDo = true;
    private boolean isZero = false;
    private boolean isEdit = false;
    public void setZero(boolean zero) {
        isZero = zero;
    }

    public void setCanDo(boolean canDo) {
        this.canDo = canDo;
    }

    public void setCountEdit(boolean isEdit){
        this.isEdit = isEdit;
        if(isEdit){
            count.setFocusableInTouchMode(true);
            count.setFocusable(true);
        }else{
            count.setFocusableInTouchMode(false);
            count.setFocusable(false);
        }
    }

    public PurcaseSheetLayout(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();
    }

    public PurcaseSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public PurcaseSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_purcase_sheet, this);
        ButterKnife.bind(this, view);
        count.setText(num+"");
    }

    private void  initListener(){
        count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                DebugLog.i("afterTextChanged");
                if(TextUtils.isEmpty(editable.toString())){
                    if(isZero){
                        count.setText("0");
                        num = 0;
                    }else{
                        count.setText("1");
                        num = 1;
                    }

                }else{
                    int numInt = Integer.parseInt(editable.toString());
                    if(numInt<=0){
                        if(!isZero) {
                            count.setText("1");
                            num = 1;
                        }
                    }else
                        num = numInt;
                }
                if(isEdit&&null!=numInterface)
                    numInterface.modify(view,num,"");
            }
        });
    }

    public void load() {

    }

    private void cut() {
        if(num-1<1){
            if(isZero)
                num = 0;
            else
                num = 1;
            count.setText(num+"");
        }else{
            count.setText(--num+"");
            if(null!=numInterface)
                numInterface.modify(view,num,"");
        }
    }

    private void add() {
        count.setText(++num+"");
        if(null!=numInterface)
            numInterface.modify(view,num,"");
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        count.setText(num+"");
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    @OnClick({R.id.cut, R.id.count, R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cut:
                if(canDo){
                    cut();
                }else{
                    MainToast.showShortToast("编辑状态，不可修改");
                }
                break;
            case R.id.count:
                break;
            case R.id.add:
                if(canDo){
                    add();
                }else{
                    MainToast.showShortToast("编辑状态，不可修改");
                }
                break;
        }
    }

    NumInterface numInterface;

    public interface NumInterface{
        void modify(View view, int num, String price);
    }

    public void setNumListener(NumInterface numInterface){
        this.numInterface = numInterface;
    }

}
