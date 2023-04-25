package com.example.taobaounion.view;

import com.example.taobaounion.base.iBaseCallback;
import com.example.taobaounion.model.domain.OnSellContent;

public interface iOnSellPageCallback extends iBaseCallback {

    //特惠内容加载完成
    void onContentLoadSuccess(OnSellContent result);

    //加载更多
    void onMoreLoaded(OnSellContent result);

    //加载更多失败
    void onMoreLoadedError();

    //已无更多内容
    void onMoreLoadedEmpty();
}
