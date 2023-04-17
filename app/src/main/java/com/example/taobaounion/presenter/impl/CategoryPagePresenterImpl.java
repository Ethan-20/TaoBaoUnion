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
import java.util.List;
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
    private Integer mCurrentPage;

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
        //通过pageInfo来控制申请第几页的内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePageContent> task = createTask(categoryId, targetPage);
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

    private  Call<HomePageContent> createTask(int categoryId, Integer targetPage) {
        //内容由网络提供 ,先去网络层,拿到数据先 -> model->Api
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(CategoryPagePresenterImpl.this, "homePagerUrl--->" + homePagerUrl);
        Call<HomePageContent> task = api.getHomePageContent(homePagerUrl);
        return task;
    }

    private void handleNetworkError(int categoryId) {
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePageContent pageContent, int categoryId) {
        List<HomePageContent.DataBean> data = pageContent.getData();
        //通知UI层更新页面
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {

                if (pageContent == null || pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePageContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoad(data);
                }
            }
        }
    }


    //加载更多内容
    @Override
    public void loadMore(int categoryId) {
        //1 拿到当前页面

        mCurrentPage = pagesInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        }
        //2 页码++
        mCurrentPage++;
        //3 加载数据
        Call<HomePageContent> task = createTask(categoryId, mCurrentPage);
        //4 处理结果
        task.enqueue(new Callback<HomePageContent>() {
            @Override
            public void onResponse(Call<HomePageContent> call, Response<HomePageContent> response) {
                //成功
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "code====>" + code);
                if (code== HttpURLConnection.HTTP_OK) {
                    //请求成功
                    HomePageContent result = response.body();
//                    LogUtils.d(CategoryPagePresenterImpl.this,"result---->"+result);
                    handleLoadMoreResult(result,categoryId);
                }
                else {
                    //请求失败
                    handleLoadMoreError(categoryId);
                }

            }

            @Override
            public void onFailure(Call<HomePageContent> call, Throwable t) {
                //出错
                LogUtils.d(this, "onFailure===" + t);
                handleLoadMoreError(categoryId);
            }
        });
        pagesInfo.put(categoryId, mCurrentPage);
    }

    //处理加载更多的结果
    private void handleLoadMoreResult(HomePageContent result, int categoryId) {
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId()==categoryId) {
                if (result==null||result.getData().size()==0) {
                    callback.onLoadMoreEmpty();
                }
                else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }
        }
    }

    /**
     * 加载更多时出错处理
     * 失败了要把页面-- 回到之前的状态,并放入到map中
     */
    private void handleLoadMoreError(int categoryId) {
        mCurrentPage--;
        for (iCategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId()==categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

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
