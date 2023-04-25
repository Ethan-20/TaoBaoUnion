package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.iOnSellPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.iOnSellPageCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;

public class OnSellPagePresenterImpl implements iOnSellPagePresenter {

    private static final int DEFAULT_PAGE = 1;
    private final Api mApi;

    private int mCurrentPage = DEFAULT_PAGE;
    private iOnSellPageCallback mOnSellPageCallback = null;

    //当前状态加载: 防止多次同时调用引起反复加载
    private boolean mIsLoading = false;

    public OnSellPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }
    @Override
    public void getOnSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //加载内容
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onLoading();
        }

        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    OnSellContent onSellContentResult = response.body();
                    onHandleSuccess(onSellContentResult);
                }
                else{
                    onHandleError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                    mIsLoading = false;
                    LogUtils.e(this,t.toString());
                    onHandleError();
            }
        });
    }

    private void onHandleSuccess(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            try {
                if (isDataEmpty(result)) {
                    onHandleEmpty();
                }
            }catch (Exception e){
                e.printStackTrace();
                onHandleEmpty();
            }

            mOnSellPageCallback.onContentLoadSuccess(result);
        }
    }

    private void onHandleEmpty(){
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }

    private void onHandleError() {
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onError();
        }
    }

    @Override
    public void reload() {
        //重新加载
        this.getOnSellContent();
    }

    @Override
    public void loadMore() {
        if (mIsLoading) {
            return;
        }
        //加载更多
        mCurrentPage++;
        //去加载更多内容
        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
                    @Override
                    public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                        mIsLoading = false;
                        int code = response.code();
                        if (code== HttpURLConnection.HTTP_OK) {
                            OnSellContent result = response.body();
                            onLoadMoreSuccess(result);
                        }
                        else{
                            onLoadMoreError();
                        }
                    }

                    @Override
                    public void onFailure(Call<OnSellContent> call, Throwable t) {
                        mIsLoading = false;
                        onLoadMoreError();
                    }
                });
            }


    private boolean isDataEmpty(OnSellContent result){
        int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size==0;

    }
    private void onLoadMoreError() {
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }

    private void onLoadMoreSuccess(OnSellContent result) {
        if (mOnSellPageCallback != null) {
            if (isDataEmpty(result)) {
                mCurrentPage--;
                mOnSellPageCallback.onMoreLoadedEmpty();
            }
            else{
                mOnSellPageCallback.onMoreLoaded(result);
            }
        }
    }


    @Override
    public void registerViewCallback(iOnSellPageCallback callback) {
        this.mOnSellPageCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(iOnSellPageCallback callback) {
        this.mOnSellPageCallback = null;
    }
}
