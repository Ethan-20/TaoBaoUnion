package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.iOnSellPagePresenter;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.OnSellContentAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtils;
import com.example.taobaounion.view.iOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import org.jetbrains.annotations.NotNull;

public class OnSellFragment extends BaseFragment implements iOnSellPageCallback, OnSellContentAdapter.OnSellPageItemClickListener {


    private iOnSellPagePresenter mOnSellPagePresenter;

    public static final int DEFAULT_SPAN_COUNT = 2;
    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentRv;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;
    private OnSellContentAdapter mOnSellContentAdapter;



    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }


    @Override
    protected void initView(View rootView) {
        mOnSellContentAdapter = new OnSellContentAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        mContentRv.setLayoutManager(gridLayoutManager);
        mContentRv.setAdapter(mOnSellContentAdapter);
        mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });

        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    protected void initListener() {
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //刷新加载更多
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });

        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
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
    setupState(State.LOADING);
    }

    @Override
    public void onContentLoadSuccess(OnSellContent result) {
        //数据回来,更新UI
        setupState(State.SUCCESS);
        mOnSellContentAdapter.setData(result);
    }


    @Override
    public void onMoreLoaded(OnSellContent result) {
        mTwinklingRefreshLayout.finishLoadmore();
        int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了"+size+"件宝贝");
        //添加内容到适配器里
        mOnSellContentAdapter.onMoreLoaded(result);
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常请稍后重试");

    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多内容了，哥们..");
    }
    @Override
    protected void release() {
        mOnSellPagePresenter.unRegisterViewCallback(this);
    }

    @Override
    public void onSellItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {
        //特惠列表内容被点击
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