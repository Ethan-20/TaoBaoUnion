package com.example.taobaounion.ui.adapter;

import android.text.TextUtils;
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
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.utils.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageRightAdapter extends RecyclerView.Adapter<SelectedPageRightAdapter.InnerHolder> {

    List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectedPageRightAdapter.InnerHolder holder, int position) {
        //绑定数据
        SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemBean = mData.get(position);
        holder.setData(itemBean);
        holder.itemView.setOnClickListener(v -> {
            if (mContentItemClickListener != null) {
                mContentItemClickListener.onContentItemClick(itemBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode()== Constants.SUCCESS_CODE) {
            mData.clear();
            List<SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> uatm_tbk_item = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
            mData.addAll(uatm_tbk_item);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover)
        public ImageView cover;



        @BindView(R.id.selected_off_price)
        public TextView offPrice;


        @BindView(R.id.selected_title)
        public TextView title;


        @BindView(R.id.selected_buy_btn)
        public TextView buyBtn;


        @BindView(R.id.selected_original_price)
        public TextView originalPriceTv;



        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

        public void setData(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemData) {
            title.setText(itemData.getTitle());
            String pict_url = itemData.getPict_url();
            Glide.with(itemView.getContext()).load(pict_url).into(cover);
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originalPriceTv.setText("晚了,没有优惠券了");
                buyBtn.setVisibility(View.GONE);
            }else{
                originalPriceTv.setText("原价"+itemData.getZk_final_price()+"元");
             }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPrice.setVisibility(View.GONE);
            }else{
                offPrice.setText(itemData.getCoupon_info());
            }



        }

    }

    public void SetOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener){
         this.mContentItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener{
        void onContentItemClick(SelectedContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item);
    }
}
