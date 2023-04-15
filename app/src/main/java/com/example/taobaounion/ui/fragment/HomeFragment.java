package com.example.taobaounion.ui.fragment;

import android.view.View;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.iHomePresenter;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private iHomePresenter mHomePresenter;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;
    private HomePagerAdapter mHomePagerAdapter;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    protected void initView(View rootView) {

        //创建适配器
        //Return a private FragmentManager for placing and managing Fragments inside of this Fragment.
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        //给viewPager设置适配器
        mHomePager.setAdapter(mHomePagerAdapter);
        //为TabLayout设置viewPager
        mTabLayout.setupWithViewPager(mHomePager);

    }
    //创建presenter
    protected void initPresenter() {
        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerCallback(this);
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
    public void onNetworkError() {
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


    public void release(){
        //释放资源
        if (mHomePresenter != null) {
            mHomePresenter.unRegisterCallback(this);
        }
    }
}
