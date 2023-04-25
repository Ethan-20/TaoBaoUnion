package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.*;
import com.example.taobaounion.presenter.impl.*;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager() ;
    private final iHomePresenter mHomePresenter;

    private final iCategoryPagerPresenter mCategoryPagePresenter ;
    private final iTicketPresenter mTicketPresenter;
    private final iSelectedPagePresenter mSelectedPagePresenter;
    private final iOnSellPagePresenter mOnSellPagePresenter;

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

    public iSelectedPagePresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public iOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
    }


}
