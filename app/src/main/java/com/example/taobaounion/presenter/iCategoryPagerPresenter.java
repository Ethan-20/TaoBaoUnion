package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.view.iCategoryPagerCallback;

public interface iCategoryPagerPresenter extends iBasePresenter<iCategoryPagerCallback> {
    /**
     * 根据分类ID获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loadMore();

    void reload();


}
