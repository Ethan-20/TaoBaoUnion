package com.example.taobaounion.view;

import com.example.taobaounion.base.iBaseCallback;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;

public interface iSelectedPageCallback extends iBaseCallback {

    /**
     * 分类内容
     * @param result
     */
    void onCategoriesLoaded(SelectedPageCategory result);

    /**
     * 分类页面内容
     * @param content
     */
    void onContentLoaded(SelectedContent content);

}
