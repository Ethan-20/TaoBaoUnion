package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.presenter.iCategoryPagerPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.view.iCategoryPagerCallback;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomePagerFragment extends BaseFragment implements iCategoryPagerCallback {

    private iCategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    private HomePagerContentAdapter mContentAdapter;

    //在adapter中需要根据category来生成HomePagerFragment,所以在这里创建一个方法来返回HomePagerFragment
    public static HomePagerFragment newInstance(Categories.DataBean category){

        HomePagerFragment homePagerFragment = new HomePagerFragment();
        //通过bundle来保存要传输的数据
        Bundle bundle = new Bundle();
        //为bundle设置category的属性
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        //为homePagerFragment绑定bundle
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagePresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO:加载数据
        LogUtils.d(this,"title---->>>>"+title);
        LogUtils.d(this,"materialId---->>>>"+ mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        //设置recyclerView的适配器
        mContentList.setAdapter(mContentAdapter);
        //
    }

    @Override
    public void onContentLoad(List<HomePageContent.DataBean> contents) {
        //数据加载到了
        //TODO :更新UI
        mContentAdapter.setData(contents);
        setupState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }


    /**
     * 网络错误
     */
    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLoadMoreLoaded(List<HomePageContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePageContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unRegisterViewCallback(this);
        }
    }
}