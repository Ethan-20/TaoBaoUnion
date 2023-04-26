package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.view.iSearchPageCallback;

public interface iSearchPagePresenter extends iBasePresenter<iSearchPageCallback> {
    //获取搜索历史
    void getHistories();

    //删除搜索历史
    void delHistories();

    //搜索
    void doSearch(String keyWord);

    //重新搜索(重试)
    void reSearch();

    //获取更多搜索结果
    void loadMore();

    //热门搜索
    void getRecommendWords();

}
