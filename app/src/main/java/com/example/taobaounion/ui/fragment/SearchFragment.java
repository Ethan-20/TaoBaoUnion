package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.*;
import com.example.taobaounion.presenter.iSearchPagePresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.*;
import com.example.taobaounion.view.iSearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends BaseFragment implements iSearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoryView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_history_container)
    public View mHistoryContainer;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_delete)
    public ImageView mDeleteBtn;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchResultList;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mInputCleanBtn;

    @BindView(R.id.search_edit_box)
    public EditText mSearchInputBox;


    private iSearchPagePresenter mSearchPagePresenter;
    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initListener() {
        //todo 清空历史
        mDeleteBtn.setOnClickListener(v -> {
            mSearchPagePresenter.delHistories();
        });
        //刷新更多
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPagePresenter != null) {
                    mSearchPagePresenter.loadMore();
                }
            }
        });
        //搜索列表内容被点击了
        mSearchResultAdapter.setOnListenItemClickListener(item -> {
            TicketUtils.toTicketPage(getContext(),item);
        });
        //监听输入框搜索按钮
        mSearchInputBox.setOnEditorActionListener((v, actionId, event) -> {
//                LogUtils.d(this,"actionId=====>"+actionId);
            if (actionId== EditorInfo.IME_ACTION_SEARCH&&mSearchPagePresenter!=null) {
                {
                    //判断拿到的内容 由editText获取
                    //LogUtils.d(SearchFragment.this,"input == "+v.getText());
                    //发起请求
                    String keyword = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
//                        mSearchPagePresenter.doSearch(keyword);
                        toSearch(keyword);
                    }

                }
            }
            return false;
        });
        //输入框清除按钮
        mInputCleanBtn.setOnClickListener(v -> {
            mSearchInputBox.setText("");
        });
        //监听输入框
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化的时候通知
                LogUtils.d(SearchFragment.this,"input ===> "+s.toString().trim());
                //如果长度不为0,显示删除按钮
                mInputCleanBtn.setVisibility(hasInput(true)?View.VISIBLE:View.GONE);
                mSearchBtn.setText(hasInput(false)?"搜索":"取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //发起搜索
        mSearchBtn.setOnClickListener(v -> {
            //如果无内容则取消，有内容则搜索
            //不包含空格且有内容则搜索内容部分
            if (hasInput(false)&&mSearchPagePresenter!=null) {
//                mSearchPagePresenter.doSearch(mSearchInputBox.getText().toString().trim());
                toSearch(mSearchInputBox.getText().toString().trim());
                KeyBoardUtils.hide(getContext(),v);
            }
            else{
                //TODO 这里是点击了取消 回到历史界面
                KeyBoardUtils.hide(getContext(),v);
                switch2HistoryPage();
            }
        });

        mHistoryView.setItemClickListener(this);
        mRecommendView.setItemClickListener(this);
    }

    //切换回历史和推荐页面
    private void switch2HistoryPage() {
        mSearchPagePresenter.getHistories();
       mHistoryContainer.setVisibility(View.VISIBLE);
       mRecommendContainer.setVisibility(View.VISIBLE);
        //隐藏之前的搜索内容
        mRefreshLayout.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace){
        if(containSpace){
            return mSearchInputBox.getText().toString().trim().length() > 0;
        }
        else{
            return mSearchInputBox.getText().toString().length() > 0;
        }
    }

    @Override
    protected void initPresenter() {
        mSearchPagePresenter = PresenterManager.getInstance().getSearchPagePresenter();
        mSearchPagePresenter.registerViewCallback(this);
//        setupState(State.SUCCESS);
        mSearchPagePresenter.getRecommendWords();
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
        //设置刷新控件
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableOverScroll(true);
        mSearchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);;
            }
        });
    }

    //重新加载
    @Override
    protected void onRetryClick() {
        if (mSearchPagePresenter != null) {
        mSearchPagePresenter.reSearch();
        }
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        setupState(State.SUCCESS);
//        mSearchPagePresenter.getHistories();
        //显示历史记录
        LogUtils.d(this,"onHistoriesLoaded--->"+histories);
        if (histories==null||histories.getHistories().size() == 0) {
            mHistoryView.setVisibility(View.GONE);
        }
        else{
            mHistoryView.setTextList(histories.getHistories());
            mHistoryView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWords) {
//        LogUtils.d(this,"onRecommendWordList--->"+recommendWords.size());
        if (recommendWords==null||recommendWords.size()==0) {
            mRecommendContainer.setVisibility(View.GONE);
        }else{
            mRecommendContainer.setVisibility(View.VISIBLE);
            List<String> list = new LinkedList<>();
            for (SearchRecommend.DataBean recommendKeyWord : recommendWords) {
                list.add(recommendKeyWord.getKeyword());
            }
            mRecommendView.setTextList(list);
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
        mRefreshLayout.setVisibility(View.VISIBLE);
        //设置数据
        mSearchResultAdapter.setData(result.getData().
                getTbk_dg_material_optional_response().
                getResult_list().
                getMap_data());



    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        //加载更多内容回到这
        //拿到结果,添加到适配器的尾部
        mRefreshLayout.finishLoadmore();
        List<? extends iLinearItemInfo> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(map_data);
        ToastUtils.showToast("加载了"+map_data.size()+"件商品");
    }

    @Override
    public void onMoreLoadedError() {
        mRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常,请稍后重试!");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshLayout.finishLoadmore();
        ToastUtils.showToast("已无更多宝贝哦!");
    }




    @Override
    public void onFlowItemClick(String text) {
        if (mSearchPagePresenter != null) {
            toSearch(text);
        }
    }

    private void toSearch(String text) {
        mSearchResultList.scrollToPosition(0);
        mSearchInputBox.setText(text);
        mSearchPagePresenter.doSearch(text);
    }
}
