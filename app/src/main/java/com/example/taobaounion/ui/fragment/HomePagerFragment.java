package com.example.taobaounion.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.example.taobaounion.ui.adapter.LooperPagerAdapter;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.view.iCategoryPagerCallback;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomePagerFragment extends BaseFragment implements iCategoryPagerCallback {

    private iCategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    //轮播图
    @BindView(R.id.looper_pager)
    public ViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;


    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    //在adapter中需要根据category来生成HomePagerFragment,所以在这里创建一个方法来返回HomePagerFragment
    public static HomePagerFragment newInstance(Categories.DataBean category) {

        HomePagerFragment homePagerFragment = new HomePagerFragment();
        //通过bundle来保存要传输的数据
        Bundle bundle = new Bundle();
        //为bundle设置category的属性
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        //为homePagerFragment绑定bundle
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagePresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO:加载数据
        LogUtils.d(this, "title---->>>>" + title);
        LogUtils.d(this, "materialId---->>>>" + mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText(title);
        }

    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置recyclerView的适配器
        mContentList.setAdapter(mContentAdapter);
        looperPager.setAdapter(mLooperPagerAdapter);

    }

    @Override
    public void onContentLoad(List<HomePageContent.DataBean> contents) {
        //数据加载到了
        //TODO :更新UI
        mContentAdapter.setData(contents);
        setupState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }


    /**
     * 网络错误
     */
    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLoadMoreLoaded(List<HomePageContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePageContent.DataBean> looperContents) {
        Context context = getContext();
        LogUtils.d(this, "looper_size---->" + looperContents.size());
        mLooperPagerAdapter.setData(looperContents);
        GradientDrawable pointDrawable = (GradientDrawable) context.getDrawable(R.drawable.shape_indicator_point);
        GradientDrawable focusedPointDrawable = (GradientDrawable) context.getDrawable(R.drawable.shape_indicator_point);
        pointDrawable.setColor(context.getColor(R.color.colorWhite));
        //解决第一张图不是真正的第一张图的问题: 用中间值减去取模之后的值,再取模就是0了
        int dx = (Integer.MAX_VALUE / 2) % looperContents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        //设置到中间点(解决从第一张轮播图向左滑动时不能滑动的问题)
        looperPager.setCurrentItem(targetCenterPosition);
        looperPointContainer.removeAllViews();
        //添加点
        for (int i = 0; i < looperContents.size(); i++) {
            View point = new View(context);
            //LayoutParams接收的是px,一半给的设计图都是dp,要用工具类转一下
            int size = SizeUtils.dip2px(context, 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(context, 5);
            layoutParams.rightMargin = SizeUtils.dip2px(context, 5);
            point.setLayoutParams(layoutParams);
            if (i == 1) {
                point.setBackground(focusedPointDrawable);
            } else {
                point.setBackground(pointDrawable);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unRegisterViewCallback(this);
        }
    }
}