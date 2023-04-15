package com.example.taobaounion.view;

import com.example.taobaounion.model.domain.Categories;

public interface IHomeCallback {

    //加载了categories后调用的方法
    void onCategoriesLoaded(Categories categories);

    void onNetworkError();

    void onLoading();

    void onEmpty();

}
