package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.iSelectedPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.iSelectedPageCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;

public class SelectedPagePresenterImpl implements iSelectedPagePresenter {

    private final Api mApi;


    public SelectedPagePresenterImpl (){
        //拿到retrofit
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    private iSelectedPageCallback mViewCallback = null;

    @Override
    public void getCategories() {

        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int resultCode = response.code();
                LogUtils.d(this, "resultCode===" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    //TODO 通知UI层更新
                    handleLoadSuccess(result);
                }
                else{
                    //其他处理
                    handleLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                //错误处理
                handleLoadError();
            }
        });
    }

    private void handleLoadSuccess(SelectedPageCategory result) {
        if (mViewCallback != null) {
            mViewCallback.onCategoriesLoaded(result);
        }
    }

    private void handleLoadError() {
        handleContentLoadedError();
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        //这里是把分类保存起来例如-程序员必备-这个条目

        Integer categoryId = item.getFavorites_id();
        String categoryUrl = UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedPageContent(categoryUrl);
            task.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int resultCode = response.code();
                    LogUtils.d(this, "resultCode====" + resultCode);
                    if (resultCode== HttpURLConnection.HTTP_OK) {
                        SelectedContent result = response.body();
                        if (mViewCallback != null) {
                            mViewCallback.onContentLoaded(result);
                        }
                    }
                    else{
                        handleContentLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                        handleContentLoadedError();
                }
            });

    }

    private void handleContentLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void reloadContent() {
            this.getCategories();
    }

    @Override
    public void registerViewCallback(iSelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(iSelectedPageCallback callback) {
        this.mViewCallback = null;
    }
}