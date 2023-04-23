package com.example.taobaounion.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.presenter.impl.TicketPresenterImpl;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.iTicketPageCallback;

public class TicketActivity extends BaseActivity implements iTicketPageCallback {


    private iTicketPresenter mTicketPresenter;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_press_back)
    public View backPress;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
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
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mCover != null&& !TextUtils.isEmpty(cover)) {
            cover = UrlUtils.getCoverPath(cover, 300);
            Glide.with(this).load(cover).into(mCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String model = result.getData().getTbk_tpwd_create_response().getData().getModel();
            mTicketCode.setText(model);
        }
    }
}
