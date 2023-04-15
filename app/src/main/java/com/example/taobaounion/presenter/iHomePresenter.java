package com.example.taobaounion.presenter;

import com.example.taobaounion.view.IHomeCallback;

public interface iHomePresenter {
    //获取商品分类
    void getCategories();

    //注册UI通知接口
    void registerCallback(IHomeCallback callback);

    //注销UI通知接口
    void unRegisterCallback(IHomeCallback callback);
}
