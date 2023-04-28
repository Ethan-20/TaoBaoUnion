package com.example.taobaounion.base;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //设置为灰色主题
        //setGreyTheme();
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    private void setGreyTheme() {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        View contentContainer = getWindow().getDecorView();
        contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
    }

    protected abstract void initPresenter();

    protected void initEvent() {

    }

    protected abstract void initView() ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        this.release();
    }

    /**
     * 子类需要释放资源 重写该方法
     */
    protected void release() {

    }

    protected abstract int getLayoutResId() ;
}
