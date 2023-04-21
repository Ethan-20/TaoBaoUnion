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

public class TicketPresenterImpl implements iTicketPresenter {

    @Override
    public void getTicket(String title,String url, String cover) {
        LogUtils.d(this," url  ="+url);
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
                LogUtils.d(TicketPresenterImpl.this, "result code == "+code);
                if (code == HttpURLConnection.HTTP_OK) {
                    TicketResult ticketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this, "result  == "+ ticketResult);
                }else{
                    LogUtils.i(TicketPresenterImpl.this, "failed  == "+code);
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {

            }
        });
    }
    @Override
    public void registerViewCallback(iTicketPageCallback callback) {

    }

    @Override
    public void unRegisterViewCallback(iTicketPageCallback callback) {

    }

}
