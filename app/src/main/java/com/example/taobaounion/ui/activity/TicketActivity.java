package com.example.taobaounion.ui.activity;

import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.ToastUtils;
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

    @BindView(R.id.ticket_cover_loading)
    public View mCoverLoading;

    @BindView(R.id.ticket_load_retry)
    public TextView mLoadRetry;

    private boolean mHasTaobaoApp = false;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        //判断是否有淘宝
        /**
         act=android.intent.action.MAIN
         cat=[android.intent.category.LAUNCHER]
         cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
         cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity
         *
         */
        //包名 com.taobao.taobao
        //检查这个包名是否安装
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = packageInfo !=null;
        } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
            mHasTaobaoApp = false;
        }

        LogUtils.d(this,"mHastaobao---->"+mHasTaobaoApp);

        //根据这个值修改UI
        mOpenOrCopyBtn.setText(mHasTaobaoApp?"打开淘宝领券":"复制tao口令");
    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(v -> finish());

        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到淘口令
                //拿到内容
                String ticketCode = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.this, "ticketCode----->" + ticketCode);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);
                //复制淘口令之后,判断有没有淘宝
                //如果有则打开,没有则提示复制成功
                if (mHasTaobaoApp) {
                    Intent taobaoIntent = new Intent();
/*                    taobaoIntent.setAction("android.intent.action.MAIN");
                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");*/
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }
                else{
                    ToastUtils.showToast("内容已复制到粘贴板");
                }

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
        if (mLoadRetry != null) {
            mLoadRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (mLoadRetry != null) {
            mLoadRetry.setVisibility(View.GONE);
        }
        if (mCoverLoading != null) {
            mCoverLoading.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        mCoverLoading.setVisibility(View.GONE);
        if (mCover != null&& !TextUtils.isEmpty(cover)) {
            cover = UrlUtils.getCoverPath(cover,300);
            Glide.with(this).load(cover).into(mCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String model = result.getData().getTbk_tpwd_create_response().getData().getModel();
            mTicketCode.setText(model);
        }
    }
}
