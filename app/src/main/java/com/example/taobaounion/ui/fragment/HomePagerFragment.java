package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.view.iCategoryPagerCallback;

import java.util.List;


public class HomePagerFragment extends BaseFragment implements iCategoryPagerCallback {

    private iCategoryPagerPresenter mCategoryPagerPresenter;

    //在adapter中需要根据category来生成HomePagerFragment,所以在这里创建一个方法来返回HomePagerFragment
    public static HomePagerFragment newInstance(Categories.DataBean category){

        HomePagerFragment homePagerFragment = new HomePagerFragment();
        //通过bundle来保存要传输的数据
        Bundle bundle = new Bundle();
        //为bundle设置category的属性
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
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
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO:加载数据
        LogUtils.d(this,"title---->>>>"+title);
        LogUtils.d(this,"materialId---->>>>"+materialId);
        mCategoryPagerPresenter.getContentByCategoryId(materialId);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }

    @Override
    public void onContentLoad(List<HomePageContent.DataBean> contents) {

    }

    @Override
    public void onLoading(int categoryId) {

    }

    @Override
    public void onError(int categoryId) {

    }

    @Override
    public void onEmpty(int categoryId) {

    }

    @Override
    public void onLoadMoreError(int categoryId) {

    }

    @Override
    public void onLoadMoreEmpty(int categoryId) {

    }

    @Override
    public void onLoadMoreLoaded(List<HomePageContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePageContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unRegisterViewCallback(this);
        }
    }
}