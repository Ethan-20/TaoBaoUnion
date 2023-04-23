package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.utils.LogUtils;
import org.jetbrains.annotations.NotNull;

public class LoadingView extends AppCompatImageView {
    private float mDegree = 30;
    private boolean mNeedRotate = true;
    public LoadingView(@NonNull @NotNull Context context) {
        this(context,null);
    }

    public LoadingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    private void startRotate() {
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegree+=10;
                mDegree = mDegree%360;
                invalidate();
                //判断是否需要继续旋转
                //如果不可见,或者已经DetachFromWindow就不再旋转了
                LogUtils.d(LoadingView.this,"loading.........");

                if (BaseFragment.currentState== BaseFragment.State.LOADING) {
                    postDelayed(this, 100);
                }else{
                    stopRotate();
                    removeCallbacks(this);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }

}
