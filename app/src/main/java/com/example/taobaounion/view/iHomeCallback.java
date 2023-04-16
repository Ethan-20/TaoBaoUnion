package com.example.taobaounion.view;

import com.example.taobaounion.base.iBaseCallback;
import com.example.taobaounion.model.domain.Categories;

public interface iHomeCallback extends iBaseCallback {

    //加载了categories后调用的方法
    void onCategoriesLoaded(Categories categories);



}
