package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class TbNestedScrollView extends NestedScrollView {
    private static final String TAG = "TbNestedScrollView";
    private int mHeaderHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;

    public TbNestedScrollView(@NonNull @NotNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeadHeight(int headerHeight) {
        //这里实现计算header的大小
        mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedScroll(@NonNull @NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull @NotNull int[] consumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
//        LogUtils.d(this, "onNestedScroll...");
    }

    @Override
    public void onNestedPreScroll(@NonNull @NotNull View target, int dx, int dy, @NonNull @NotNull int[] consumed, int type) {
//        LogUtils.d(this, "dy====" + dy);
        //如果滑动到的地方比头部小,那么就nestedScrollView就滑动,否则交给孩子去处理
        if (target instanceof RecyclerView){
            this.mRecyclerView = (RecyclerView)target;
        }
        if (originScroll<mHeaderHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        LogUtils.d(this, "vertical====" + t);
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子项是否已经滑动到了底部
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            //这个方法检查RV能否继续垂直滑动 负数检查能否向上滑,正数表示能否向下滑
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
//            Log.d(TAG,"isBottom--->"+isBottom);
            return isBottom;
        }
        return false;
    }
}
