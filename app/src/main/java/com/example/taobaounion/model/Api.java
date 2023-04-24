package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePageContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);


    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET()
    Call<SelectedContent> getSelectedPageContent(@Url String url);


}
