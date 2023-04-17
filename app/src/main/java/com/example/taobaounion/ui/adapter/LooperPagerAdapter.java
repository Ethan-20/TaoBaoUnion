package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.utils.UrlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
    private List<HomePageContent.DataBean> mData = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        Context context = container.getContext();
        HomePageContent.DataBean dataBean = mData.get(position);
        String CoverUrl = UrlUtils.getCoverPath(dataBean.getPict_url());
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
        return iv;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
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
}
