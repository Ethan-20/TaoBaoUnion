package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.TicketParams;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.iTicketPageCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;

public class TicketPagePresenterImpl implements iTicketPresenter {

    private iTicketPageCallback mViewCallback = null;
    private String mCover;
    private TicketResult mTicketResult = null;

    enum LoadState{
         LOADING,SUCCESS,ERROR,NONE
     }

     private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title,String url, String cover) {
//        LogUtils.d(this," url  ="+url);
        this.mCover = cover;
        onTicketLoading();
        String ticketUrl = UrlUtils.getTicketUrl(url);
        //TODO: 获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(ticketUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPagePresenterImpl.this, "result code == "+code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mTicketResult = response.body();
                    LogUtils.d(TicketPagePresenterImpl.this, "result  == "+ mTicketResult);
                    //通知ui更新
                   onTicketLoadedSuccess();
                }else{
                    onTicketLoadedError();
                    LogUtils.i(TicketPagePresenterImpl.this, "failed  == "+code);
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {

                onTicketLoadedError();
            }
        });
    }


    @Override
    public void registerViewCallback(iTicketPageCallback callback) {
        if (mCurrentState!= LoadState.NONE) {
            //说明状态已经改变
            //更新UI
            if (mCurrentState== LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onTicketLoadedError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
        this.mViewCallback = callback;
    }

    private void onTicketLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }else{
            mCurrentState = LoadState.ERROR;
        }
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }else{
            mCurrentState = LoadState.LOADING;
        }
    }

    private void onTicketLoadedSuccess() {

        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover,mTicketResult);
        }else{
            mCurrentState = LoadState.SUCCESS;

        }

    }

    @Override
    public void unRegisterViewCallback(iTicketPageCallback callback) {
        this.mViewCallback = null;
    }

}
