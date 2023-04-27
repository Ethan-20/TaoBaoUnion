package com.example.taobaounion.view;

import com.example.taobaounion.base.iBaseCallback;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface iSearchPageCallback extends iBaseCallback {

    //搜索历史结果
    void onHistoriesLoaded(Histories histories);


    //历史记录删除完成
    void onHistoriesDeleted();

    //搜索结果:成功
    void onSearchSuccess(SearchResult result);

    //加载更多内容成功
    void onMoreLoaded(SearchResult result);

    //加载更多内容失败
    void onMoreLoadedError();

    //加载更多内容为空
    void onMoreLoadedEmpty();

    //显示推荐词列表
    void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWords);
}
