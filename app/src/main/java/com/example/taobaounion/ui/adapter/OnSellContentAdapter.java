package com.example.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.text.Layout;
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
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellPageItemClickListener mOnSellPageItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OnSellContentAdapter.InnerHolder holder, int position) {
        //TODO:绑定数据
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean = mData.get(position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSellPageItemClickListener != null) {
                    mOnSellPageItemClickListener.onSellItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        mData.clear();
        mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多
     * @param result
     */
    public void onMoreLoaded(OnSellContent result) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> map_data = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //原数据长度
        int oldSize = mData.size();
        mData.addAll(map_data);
        //通知尾部改
        notifyItemRangeChanged(oldSize-1,map_data.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_title)
        public TextView titleTv;

        @BindView(R.id.on_sell_cover)
        public ImageView cover;

        @BindView(R.id.on_sell_original_price_tv)
        public TextView originalPriceTv;

        @BindView(R.id.on_sell_off_price_tv)
        public TextView offPriceTv;



        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data) {
            titleTv.setText(data.getTitle());
            String coverPath = UrlUtils.getCoverPath(data.getPict_url());
//            LogUtils.d(this,"data.getPict_url()---->"+coverPath);
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            String originalPrice = data.getZk_final_price();
            originalPriceTv.setText("￥"+originalPrice+" ");
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            float coupon = data.getCoupon_amount();
            float originalPriceFloat = Float.parseFloat(originalPrice);
            float offPrice = originalPriceFloat - coupon;
            offPriceTv.setText("券后价："+String.format("%.2f",offPrice));

        }
    }

    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener) {
        this.mOnSellPageItemClickListener = listener;
    }

    public interface OnSellPageItemClickListener{
        void onSellItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean);
    }
}
