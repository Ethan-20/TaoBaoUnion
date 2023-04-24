package com.example.taobaounion.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataBean> mData = new ArrayList<>();

    //当前选中位置
    private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @NotNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectedPageLeftAdapter.InnerHolder holder, int position) {
        SelectedPageCategory.DataBean dataBean = mData.get(position);
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPosition==position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorEEEEE,null));
        }
        else{
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorWhite,null));
        }
        itemTv.setText(dataBean.getFavorites_title());
        itemTv.setOnClickListener(v -> {
            if (mItemClickListener!=null&&mCurrentSelectedPosition!=position) {
                //修改当前选中位置
                mCurrentSelectedPosition = position;
                mItemClickListener.onLeftItemClick(dataBean);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataBean> data = categories.getData();
        if (categories.getData() != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener){

        this.mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener{
        void onLeftItemClick(SelectedPageCategory.DataBean item);
    }
}
