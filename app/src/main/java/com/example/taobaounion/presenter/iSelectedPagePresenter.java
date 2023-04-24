package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.view.iSelectedPageCallback;

public interface iSelectedPagePresenter  extends iBasePresenter<iSelectedPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取分类内容
     * @param item
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();

}
