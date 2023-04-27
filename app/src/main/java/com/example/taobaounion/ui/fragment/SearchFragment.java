package com.example.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.iSearchPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.iSearchPageCallback;

import java.util.List;

public class SearchFragment extends BaseFragment implements iSearchPageCallback {

    private iSearchPagePresenter mSearchPagePresenter;

    @Override
    protected void initPresenter() {
        mSearchPagePresenter = PresenterManager.getInstance().getSearchPagePresenter();
        mSearchPagePresenter.registerViewCallback(this);
        //获取推荐词
        mSearchPagePresenter.getRecommendWords();
        mSearchPagePresenter.doSearch("鼠标");
        mSearchPagePresenter.getHistories();

    }

    @Override
    protected void release() {
        if (mSearchPagePresenter != null) {
            mSearchPagePresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onHistoriesLoaded(List<String> histories) {
    //显示历史记录
        LogUtils.d(this,"onHistoriesLoaded--->"+histories);
    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        LogUtils.d(this,"onSearchSuccess--->"+result);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordList(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(this,"onRecommendWordList--->"+recommendWords.size());
    }
}
