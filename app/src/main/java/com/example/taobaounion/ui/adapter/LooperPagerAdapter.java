package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.model.domain.iBaseInfo;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
    private List<HomePageContent.DataBean> mData = new ArrayList<>();
    private OnLooperPageItemClickListener mOnItemClickListener = null;

    public int getDataSize(){
//        LogUtils.d(this,"mDta.size()===="+mData.size());
        return mData.size();
    }
    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        //处理越界问题
        int realPosition = position % mData.size();
        Context context = container.getContext();
        HomePageContent.DataBean dataBean = mData.get(realPosition);
        //获取轮播图大小
        int width = container.getMeasuredWidth();
        int height = container.getMeasuredHeight();
        int picSize = (width>height?width:height)/2;
//        LogUtils.d(this,"width--->"+width);
//        LogUtils.d(this,"height--->"+height);
        String CoverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(),picSize);
//        LogUtils.d(this,"CoverUrl--->"+CoverUrl);
        //采用代码生成ImageView的方式
        ImageView iv = new ImageView(context);
        //创建布局参数
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置布局参数
        iv.setLayoutParams(layoutParams);
        //设置拉伸
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(CoverUrl).into(iv);
        container.addView(iv);
        //设置监听
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLooperItemClick(dataBean);
                }
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view==object;
    }

    public void setData(List<HomePageContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperItemClickListener(OnLooperPageItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener{
        void onLooperItemClick(iBaseInfo item);
    }
}
