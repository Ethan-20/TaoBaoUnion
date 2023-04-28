package com.example.taobaounion.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.example.taobaounion.model.domain.iBaseInfo;
import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.adapter.LooperPagerAdapter;
import com.example.taobaounion.ui.custom.AutoLoopViewPager;
import com.example.taobaounion.utils.*;
import com.example.taobaounion.view.iCategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomePagerFragment extends BaseFragment implements iCategoryPagerCallback, LinearItemContentAdapter.OnListenItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private iCategoryPagerPresenter mPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    //轮播图
    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homePagerHeaderContainer;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedScroller;


    private LinearItemContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    @Override
    public void onResume() {
        super.onResume();
        //可见的时候调用开始轮播
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //不可见的时候结束轮播
        looperPager.stopLoop();
    }

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
    protected void initListener() {

        //HomePagerFragment实现ItemClick监听接口的原因是让Adapter中可以调用到HPF中的OnClick()方法来进行UI操作
        mContentAdapter.setOnListenItemClickListener(this);
        mLooperPagerAdapter.setOnLooperItemClickListener(this);
        //全局布局观察者,当布    局的时候就会调用这个方法
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight=0;
                if (homePagerHeaderContainer != null) {
                     headerHeight = homePagerHeaderContainer.getMeasuredHeight();
                }
//                LogUtils.d(this,"homePagerHeaderContainer.getMeasuredHeight()===="+headerHeight);
                if (homePagerNestedScroller != null) {
                    homePagerNestedScroller.setHeadHeight(headerHeight);
                }
                int measuredHeight = 0;
                if (homePagerParent != null) {
                     measuredHeight = homePagerParent.getMeasuredHeight();
                }
                //LogUtils.d(HomePagerFragment.this, "measuredHeight------>" + measuredHeight);
                LinearLayout.LayoutParams layoutParams;
                if (mContentList != null) {
                     layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
//                    LogUtils.d(HomePagerFragment.this,"layoutParam s.height--->"+layoutParams.height);
                    layoutParams.height = measuredHeight;
                    mContentList.setLayoutParams(layoutParams);
                }

                //有了height之后,就要移除监听,不然会一直监听,没意义
                if (measuredHeight!=0)
                {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

//        currentCategoryTitleTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int measuredHeight = mContentList.getMeasuredHeight();
//                LogUtils.d(HomePagerFragment.this,"measuredHeight------->"+measuredHeight);
//            }
//        });

        //轮播图的监听
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动页面时
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面被展示时
            @Override
            public void onPageSelected(int position) {
                //这里的position不是真正的position,需要取模,而真正有多少个数据
                //还需要通过adapter中的方法进行暴露,这样拿到的数据才是最准确的
                //mLooperPagerAdapter 中的mData是在onLooperListLoaded()中被设置的
                //而监听器一直在监听,当中的mData还没有被设置数据时,监听器已经在工作了
                //就会造成除零error 所以要加一个判断,防止除零错误的发生
                if (mLooperPagerAdapter.getDataSize() == 0) return;
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            //滑动状态发生改变
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置加载更多监听
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this, "onLoadMore()........");
                //去加载更多的内容
                if (mPagerPresenter != null) {
                    mPagerPresenter.loadMore(mMaterialId);
                }

            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }
        });
    }

    /**
     * 切换至指示器
     *
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initPresenter() {
        mPagerPresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO:加载数据
        LogUtils.d(this, "title---->>>>" + title);
        LogUtils.d(this, "materialId---->>>>" + mMaterialId);
        if (mPagerPresenter != null) {
            mPagerPresenter.getContentByCategoryId(mMaterialId);
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
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);;
            }
        });
        //创建适配器
        mContentAdapter = new LinearItemContentAdapter();
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置recyclerView的适配器
        mContentList.setAdapter(mContentAdapter);
        looperPager.setAdapter(mLooperPagerAdapter);
        //设置Refresh相关属性
//        twinklingRefreshLayout.setEnableRefresh(false); //上拉无效
        twinklingRefreshLayout.setEnableLoadmore(true); //下拉有效(默认为true)
//        twinklingRefreshLayout.setBottomView();

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

    @Override
    protected void onRetryClick() {
        if (mPagerPresenter != null) {
            mPagerPresenter.getContentByCategoryId(mMaterialId);
        }
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
        ToastUtils.showToast("网络异常请重试!!");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtils.showToast("已无更多商品!");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }

    }

    @Override
    public void onLoadMoreLoaded(List<HomePageContent.DataBean> contents) {
        //添加到适配器的底部把数据添加到适配器的底部
        mContentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("加载了" + contents.size() + "件商品");
    }

    @Override
    public void onLooperListLoaded(List<HomePageContent.DataBean> looperContents) {
        Context context = getContext();
        LogUtils.d(this, "looper_size---->" + looperContents.size());
        mLooperPagerAdapter.setData(looperContents);

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
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mPagerPresenter != null) {
            mPagerPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onItemClick(iBaseInfo item) {
        //列表内容被点击了
//        LogUtils.d(this,"item clicked--->"+item.getTitle());
        onHandleItemClick(item);
    }

    private void onHandleItemClick(iBaseInfo item) {

        TicketUtils.toTicketPage(getContext(),item);
    }

    @Override
    public void onLooperItemClick(iBaseInfo item) {
        //轮播图被点击
//        LogUtils.d(this, "Looper item clicked--->"+item.getTitle());
        onHandleItemClick(item);
    }
}