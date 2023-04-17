package com.example.taobaounion.view;

import com.example.taobaounion.base.iBaseCallback;
import com.example.taobaounion.model.domain.HomePageContent;

import java.util.List;

public interface iCategoryPagerCallback extends iBaseCallback {
    /**
     * 数据加载回调
     *
     * @param contents
     */
    void onContentLoad(List<HomePageContent.DataBean> contents);

    //获取当前id
    int getCategoryId();

    /**
     * 加载更多时网络错误
     */
    void onLoadMoreError();

    /**
     * 加载不出数据了
     */
    void onLoadMoreEmpty();

    /**
     * 加载更多成功
     *
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePageContent.DataBean> contents);

    /**
     * 轮播图
     * @param contents
     */
    void onLooperListLoaded(List<HomePageContent.DataBean> contents);
}
