package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.view.iOnSellPageCallback;

public interface iOnSellPagePresenter extends iBasePresenter<iOnSellPageCallback> {

    //加载特惠内容
    void getOnSellContent();

    //重新加载,网络出问题
    void reload();

    //加载更多
    void loadMore();
}
