package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.iCategoryPagerCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 有多个分类页面
 * 每次都创建新的分类页面浪费资源
 * 因此使用单例模式
 */
public class CategoryPagePresenterImpl implements iCategoryPagerPresenter {
    //有多个分类页面,用map来保存分类页面浏览到第几页了
    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    //有多个分类页面,通过一个单例类来管理这一个页面数组
    private ArrayList<iCategoryPagerCallback> callbackList = new ArrayList<>();
    public static final int DEFAULT_PAGE = 1;
    private static iCategoryPagerPresenter ourInstance = null;

    public static iCategoryPagerPresenter getInstance() {

        if (ourInstance == null)
            ourInstance = new CategoryPagePresenterImpl();

        return ourInstance;
    }

    private CategoryPagePresenterImpl() {

    }


    /**
     * 根据分类id去加载内容
     *
     * @param categoryId
     */
    @Override
    public void getContentByCategoryId(int categoryId) {
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        //内容由网络提供 ,先去网络层,拿到数据先 -> model->Api
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        //通过pageInfo来控制申请第几页的内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(CategoryPagePresenterImpl.this, "homePagerUrl--->" + homePagerUrl);
        Call<HomePageContent> task = api.getHomePageContent(homePagerUrl);
        task.enqueue(new Callback<HomePageContent>() {
            @Override
            public void onResponse(Call<HomePageContent> call, Response<HomePageContent> response) {
                int code = response.code();
                //LogUtils.d(CategoryPagePresenterImpl.this,"code==="+code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePageContent pageContent = response.body();
                    //LogUtils.d(this,pageContent.toString());
                    //把数据给UI更新
                    handleHomePageContentResult(pageContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePageContent> call, Throwable t) {
                LogUtils.d(this, "onFailure===" + t);
                handleNetworkError(categoryId);
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePageContent pageContent, int categoryId) {
        //通知UI层更新页面
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {

                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    callback.onContentLoad(pageContent.getData());
                }
            }
        }
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void registerViewCallback(iCategoryPagerCallback callback) {
        if (!callbackList.contains(callback)) {
            callbackList.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(iCategoryPagerCallback callback) {
        callbackList.remove(callback);
    }
}
