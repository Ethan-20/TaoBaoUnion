package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.iSearchPagePresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.view.iSearchPageCallback;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends BaseFragment implements iSearchPageCallback {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mTextFlowHistory;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mTextFlowRecommend;

    @BindView(R.id.search_history_container)
    public LinearLayout mHistoryContainer;

    @BindView(R.id.search_recommend_container)
    public LinearLayout mRecommendContainer;

    @BindView(R.id.search_history_delete)
    public ImageView mDeleteBtn;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchResultList;


    private iSearchPagePresenter mSearchPagePresenter;
    private LinearItemContentAdapter mSearchResultAdapter;


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initListener() {
        mDeleteBtn.setOnClickListener(v -> {
            //todo 清空历史
            mSearchPagePresenter.delHistories();
        });
    }

    @Override
    protected void initPresenter() {
        mSearchPagePresenter = PresenterManager.getInstance().getSearchPagePresenter();
        mSearchPagePresenter.registerViewCallback(this);
        //获取推荐词
        mSearchPagePresenter.getRecommendWords();
        mSearchPagePresenter.doSearch("鼠标");
        mSearchPagePresenter.getHistories();

    }

    @Override
    protected void release() {
        if (mSearchPagePresenter != null) {
            mSearchPagePresenter.unRegisterViewCallback(this);
        }
    }



    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {

        mSearchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchResultList.setAdapter(mSearchResultAdapter);


    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
    //显示历史记录
        LogUtils.d(this,"onHistoriesLoaded--->"+histories);
        if (histories==null||histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        }
        else{
            mHistoryContainer.setVisibility(View.VISIBLE);
            mTextFlowHistory.setTextList(histories.getHistories());
        }


    }

    @Override
    public void onHistoriesDeleted() {
        //更新历史记录
        if (mSearchPagePresenter != null) {
            mSearchPagePresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        LogUtils.d(this,"onSearchSuccess--->"+result);
        setupState(State.SUCCESS);
        //隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //显示搜索结果
        mSearchResultList.setVisibility(View.VISIBLE);
        //设置数据
        mSearchResultAdapter.setData(result.getData().
                getTbk_dg_material_optional_response().
                getResult_list().
                getMap_data());
        mSearchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);;
            }
        });


    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(this,"onRecommendWordList--->"+recommendWords.size());
        if (recommendWords==null||recommendWords.size()==0) {
            mRecommendContainer.setVisibility(View.GONE);
        }else{
            mRecommendContainer.setVisibility(View.VISIBLE);
            List<String> list = new LinkedList<>();
            for (SearchRecommend.DataBean recommendKeyWord : recommendWords) {
                list.add(recommendKeyWord.getKeyword());
            }
            mTextFlowRecommend.setTextList(list);
        }
    }
}
