package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.iSelectedPagePresenter;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaounion.ui.adapter.SelectedPageRightAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.view.iSelectedPageCallback;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectedFragment extends BaseFragment implements iSelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectedPageRightAdapter.OnSelectedPageContentItemClickListener {

    private iSelectedPagePresenter mSelectedPagePresenter;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    private SelectedPageLeftAdapter mSelectedPageLeftAdapter;
    private SelectedPageRightAdapter mSelectedPageRightAdapter;


    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();

    }

    @Override
    protected void release() {
        if (mSelectedPagePresenter != null) {
        mSelectedPagePresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        barTitleTv.setText("精选宝贝");
        setupState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSelectedPageLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mSelectedPageLeftAdapter);
        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSelectedPageRightAdapter = new SelectedPageRightAdapter();
        rightContentList.setAdapter(mSelectedPageLeftAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 6);
                outRect.top =topAndBottom;
                outRect.bottom = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSelectedPageLeftAdapter.setOnLeftItemClickListener(this);
        mSelectedPageRightAdapter.SetOnSelectedPageContentItemClickListener(this);
    }


    @Override
    protected void onRetryClick() {
        //重新加载
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
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
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setupState(State.SUCCESS);
        mSelectedPageLeftAdapter.setData(categories);
        //分类
        //根据当前选中的分类获取内容
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mSelectedPageRightAdapter.setData(content);
        //每次切换分类都滑动到顶部
        rightContentList.scrollToPosition(0);

    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        //左边的分类点击了
        LogUtils.d(this, "onLeftItemClick...." + item.getFavorites_title());
        mSelectedPagePresenter.getContentByCategory(item);

    }

    @Override
    public void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
        //内容被点击了
        //todo 处理跳转
        String title = item.getTitle();
        //这个是详情url不是领券url
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            //防止商品没有优惠券的情况
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        //拿到ticketPresenter去加载数据
        iTicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
