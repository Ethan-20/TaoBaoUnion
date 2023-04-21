package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.iHomePresenter;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.iHomeCallback;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends BaseFragment implements iHomeCallback {

    private iHomePresenter mHomePresenter;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;
    private HomePagerAdapter mHomePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(this,"onCreateView.....");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(this,"onDestroyView....");
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    protected void initView(View rootView) {

        //创建适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        //给viewPager设置适配器
        mHomePager.setAdapter(mHomePagerAdapter);
        //为TabLayout设置viewPager
        mTabLayout.setupWithViewPager(mHomePager);

    }
    //创建presenter
    protected void initPresenter() {
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container,false);
    }

    //加载数据
    protected void loadData() {
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        LogUtils.d(this,"onCategoriesLoaded....");

        setupState(State.SUCCESS);

        //加载的数据就会从这里回来
        if (mHomePagerAdapter != null) {
        mHomePagerAdapter.setCategory(categories);
        }
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }


    @Override
    protected void onRetryClick() {
        //网络错误,点击了重试
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }

    @Override
    protected void release() {
        //释放资源
        if (mHomePresenter != null) {
            mHomePresenter.unRegisterViewCallback(this);
        }
    }
}
