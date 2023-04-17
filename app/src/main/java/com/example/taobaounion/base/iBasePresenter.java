package com.example.taobaounion.base;

import com.example.taobaounion.view.iHomeCallback;

public interface iBasePresenter<T> {
    //注册UI通知接口
    void registerViewCallback(T callback);

    //注销UI通知接口
    void unRegisterViewCallback(T callback);
}
