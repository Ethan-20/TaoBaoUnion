package com.example.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.iBaseInfo;
import com.example.taobaounion.presenter.iTicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;

import static androidx.core.content.ContextCompat.startActivity;
import static com.tamsiree.rxkit.RxTool.getContext;

public class TicketUtils {
    public static void toTicketPage(Context context, iBaseInfo baseInfo){
        //特惠列表内容被点击
        //todo 处理跳转
        String title = baseInfo.getTitle();
        //这个是详情url不是领券url
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            //防止商品没有优惠券的情况
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        //拿到ticketPresenter去加载数据
        iTicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
