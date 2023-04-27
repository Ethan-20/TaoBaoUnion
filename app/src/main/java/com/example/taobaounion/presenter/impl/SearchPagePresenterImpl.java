package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.iSearchPagePresenter;
import com.example.taobaounion.utils.JsonCacheUtils;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.iSearchPageCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;

public class SearchPagePresenterImpl implements iSearchPagePresenter {

    private final Api mApi;
    private final JsonCacheUtils mJsonHelper;
    private iSearchPageCallback mSearchPageCallback = null;

    private static final int DEFAULT_PAGE = 0;
    private int  mCurrentPage  = DEFAULT_PAGE;

    public static final String KEY_HISTORIES = "key_histories";

    private static final int DEFAULT_HISTORIES_SIZE = 10;
    private static final int historyMaxSize = DEFAULT_HISTORIES_SIZE;
    private String mCurrentKeyWord = null;
    public SearchPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonHelper = JsonCacheUtils.getInstance();
    }

    @Override
    public void registerViewCallback(iSearchPageCallback callback) {
        this.mSearchPageCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(iSearchPageCallback callback) {
        //todo 这里是不是应该改成 callback = null
        this.mSearchPageCallback = null;
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonHelper.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchPageCallback!=null) {
            mSearchPageCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonHelper.delCache(KEY_HISTORIES);
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesDeleted();
        }
    }



    //添加历史记录，这里的history是单个搜索内容
    private void saveHistories(String history) {

        //如果说已经有了，就覆盖，让它呈现在最后面
        //对个数进行限制
        //拿到搜索历史
        Histories histories = mJsonHelper.getValue(KEY_HISTORIES, Histories.class);
        //判断是否已经保存过且有内容
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new LinkedList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        //对个数进行限制（这里的逻辑有问题，应该是删除掉最早加入的记录）
        if (historiesList.size()==historyMaxSize) {
            historiesList = historiesList.subList(0, historyMaxSize-1);
        }
        //添加记录
        historiesList.add(history);
        histories.setHistories(historiesList);
        mJsonHelper.saveCache(KEY_HISTORIES,histories);
    }

    @Override
    public void doSearch(String keyWord) {
        if (mCurrentKeyWord ==null||!mCurrentKeyWord.equals(keyWord)) {
            this.saveHistories(keyWord);
            this.mCurrentKeyWord = keyWord;
        }
        //更新UI
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyWord);
        //发起异步请求
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "doSearch(String keyWord)+code == " + code);
                if (code== HttpURLConnection.HTTP_OK) {
                    //处理结果
                    handleSearchResult(response.body());
                }
                else{
                    //处理出错
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                    //处理出错
                t.printStackTrace();
                handleError();
            }
        });

    }

    private void handleError() {
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchPageCallback.onEmpty();
            }
            else{
                mSearchPageCallback.onSearchSuccess(result);
            }
        }

    }

    private boolean isResultEmpty(SearchResult result){
        try {
            return (result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0);
        } catch (Exception e) {
            return true;
        }
    }

    //这里是重新加载
    @Override
    public void reSearch() {
        if (mCurrentKeyWord == null) {
            if (mSearchPageCallback != null) {
                mSearchPageCallback.onEmpty();
            }
        }
        else{
            //可以重新搜索
            this.doSearch(mCurrentKeyWord);
        }
    }

    //加页
    @Override
    public void loadMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyWord != null) {
            mSearchPageCallback.onEmpty();
        }
        else{
            //做搜索的事情
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "doSearchMore + code == " + code);
                if (code== HttpURLConnection.HTTP_OK) {
                    //处理加载更多的结果
                    handleSearchMoreResult(response.body());
                }
                else{
                    //处理出错
                    handleLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                //处理出错
                t.printStackTrace();
                handleLoadMoreError();
            }
        });
    }

    //处理加载更多的结果
    private void handleSearchMoreResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isResultEmpty(result)) {
                mCurrentPage--;
                //数据为空
                mSearchPageCallback.onMoreLoadedEmpty();
            }
            else{
                mSearchPageCallback.onMoreLoaded(result);
            }
        }
    }

    //加载更多失败
    private void handleLoadMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(this, "getRecommendWords code====" + code);
                if (code== HttpURLConnection.HTTP_OK) {
                //处理结果 这里如果没有返回结果，就不用管了捏
                    if (mSearchPageCallback != null) {
                        mSearchPageCallback.onRecommendWordLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
