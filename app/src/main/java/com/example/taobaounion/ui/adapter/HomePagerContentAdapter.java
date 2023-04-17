package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.HomePageContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {
    List<HomePageContent.DataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);

        return new InnerHolder(itemView);

    }

    /**
     * 设置控件数据
     */
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePageContent.DataBean dataBean = mData.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<HomePageContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePageContent.DataBean> contents) {
        //添加之前拿到原来的size
        int oldSize = mData.size();
        mData.addAll(contents);
        //notifyItemRangeChanged()方法可以更新局部item,第一个参数是开始改变的下标,第二个参数是改变的数量
        notifyItemRangeChanged(oldSize, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_price)
        public TextView offPriceTv;

        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;

        @BindView(R.id.goods_origin_price)
        public TextView originPriceTv;

        @BindView(R.id.goods_sells_count)
        public TextView sellsCountTv;

        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);//把itemView绑定到InnerHolder这个类上
        }

        public void setData(HomePageContent.DataBean dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
//            LogUtils.d(this,"url--->"+dataBean.getPict_url());
            /**
             * //gw.alicdn.com/bao/uploaded/i1/2793447635/O1CN01EkSJXR26GsmzXs0Wx_!!0-item_pic.jpg
             *  返回值如上，没有协议开头，需要手动添加协议
             */
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize = (width > height ? width : height)/2;
/*            LogUtils.d(this, "width----->" + width);
            LogUtils.d(this, "height----->" + height);*/
            String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url(), coverSize);
            LogUtils.d(this,"coverPath---->"+coverPath);
            Glide.with(context).load(coverPath).into(cover);
            long couponAmount = dataBean.getCoupon_amount();//折扣
            String originPrice = dataBean.getZk_final_price();//原价
            //LogUtils.d(this,"originPrice---->"+originPrice);
            float resultPrice = Float.parseFloat(originPrice) - couponAmount;//券后价
            //LogUtils.d(this,"resultPrice---->"+resultPrice);
            finalPriceTv.setText(String.format("%.2f", resultPrice));
            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price), couponAmount));
            originPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originPriceTv.setText(String.format(context.getString(R.string.text_goods_origin_price), originPrice));
            //LogUtils.d(this,"getVolume---->"+dataBean.getVolume());
            sellsCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));
        }
    }
}
