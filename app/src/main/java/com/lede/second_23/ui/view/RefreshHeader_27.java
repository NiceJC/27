package com.lede.second_23.ui.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lede.second_23.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;

/**
 *
 * 27的定制刷新头
 * Created by wjc on 17/10/20.
 */

public class RefreshHeader_27 extends LinearLayout implements RefreshHeader{

    private View view;

    private ProgressBar progressbar_27;
    public RefreshHeader_27(Context context) {
        super(context,null);
    }

    public RefreshHeader_27(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public RefreshHeader_27(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context){

        setGravity(Gravity.CENTER);

        ProgressDrawable progressDrawable=new ProgressDrawable();
//        progressDrawable.
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.header_27,this);
        progressbar_27= (ProgressBar) view.findViewById(R.id.progressbar_27);

        addView(view);
}




    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {





    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {


    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 200;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
