package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 功能:自动轮播
 */
public class AutoLoopViewPager extends ViewPager {
    private boolean isLoop = false;

    //切换间隔时长,单位毫秒
    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;
    public AutoLoopViewPager(@NonNull @NotNull Context context) {
        super(context,null);
    }

    public AutoLoopViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context, attrs);
    }

    private void init(@NotNull Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        //获取属性
        setDuration(t.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION));
        //回收
        t.recycle();
    }

    public void startLoop(){
        isLoop = true;
        //先拿到当前的位置
        post(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            setCurrentItem(++currentItem);
            if (isLoop) {
//                LogUtils.d(AutoLoopViewPager.this, "isLoop......");
                postDelayed(this, mDuration);
            }
        }
    };

    /**
     * 设置切换时长
     * @param duration 时长,单位:毫秒
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void stopLoop() {
        isLoop = false;
//        LogUtils.d(AutoLoopViewPager.this, "stopLoop......");
        removeCallbacks(mTask);
    }
}
