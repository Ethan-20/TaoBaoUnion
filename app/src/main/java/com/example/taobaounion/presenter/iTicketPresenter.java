package com.example.taobaounion.presenter;

import com.example.taobaounion.base.iBasePresenter;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.view.iTicketPageCallback;

public interface iTicketPresenter extends iBasePresenter<iTicketPageCallback> {

    /**
     * 生成淘口令
     * @param url
     * @param cover
     */
    void getTicket(String title,String url, String cover);
}
