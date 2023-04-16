package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.view.iHomeCallback;

public interface iHomePresenter extends iBasePresenter<iHomeCallback> {
    //获取商品分类
    void getCategories();


}
