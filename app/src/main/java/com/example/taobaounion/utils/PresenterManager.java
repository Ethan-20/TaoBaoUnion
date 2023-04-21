package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.presenter.iHomePresenter;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager() ;
    private final iHomePresenter mHomePresenter;

    private final iCategoryPagerPresenter mCategoryPagePresenter ;
    private final iTicketPresenter mTicketPresenter;

    public iHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public iCategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }


    public static PresenterManager getInstance() {
        return ourInstance;
    }

    public iTicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
    }


}
