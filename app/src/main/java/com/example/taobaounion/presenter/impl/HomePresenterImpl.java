package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.iHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.IHomeCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;

public class HomePresenterImpl implements iHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        //加载数据时
        if (mCallback != null) {
            mCallback.onLoading();
        }
        //加载分类内容
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //成功结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "code=== " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();
                    if (mCallback != null) {

                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            mCallback.onCategoriesLoaded(categories);
                        }
                        //LogUtils.d(HomePresenterImpl.this,categories.toString());
                    }
                } else {
                    //请求失败
                    LogUtils.i(HomePresenterImpl.this, "请求失败.....");
                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //失败结果:
                LogUtils.e(this, "请求错误.....");
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
            }
        });
    }

    //如果有多个callback对象,需要用数组来统一管理
    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unRegisterCallback(IHomeCallback callback) {
        this.mCallback = null;
    }


}
