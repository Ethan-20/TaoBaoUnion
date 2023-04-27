package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {


    //整页的行数
    private List<List<View>> lines = new LinkedList<>();

    private static final int DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace;
    private float mItemVerticalSpace;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener;

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    private List<String> mTextList = new LinkedList<>();

    public TextFlowLayout(Context context) {
        this(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
        LogUtils.d(this,"mItemHorizontalSpace---->"+mItemHorizontalSpace);
        LogUtils.d(this,"mItemVerticalSpace---->"+mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        this.mTextList = textList;
        //添加内容
        for (String text : mTextList) {
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, true);等价于下面两条语句
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onFlowItemClick(text);
                }
            });
            addView(item);
        }
    }


    /**
     * 总结
     * 对于不满意父容器测量的子 View, 父容器将会依据 子 View 的测量结果来开始第二次测量。
     * 因此,onMeasure()有可能被反复调用多次
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        LogUtils.d(this,"onMeasure.......");
        //单行(每一行的view)
        List<View> line = null;
        lines.clear();
        //减去padding值才行
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
//        LogUtils.d(this,"mSelfWidth====>"+mSelfWidth);
        //测量
//        LogUtils.d(this,"onMeasure---->"+getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要测量
                continue;
            }
            //测量前
//            LogUtils.d(this,"测量前height--->"+itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //测量后
//            LogUtils.d(this,"测量后height--->"+itemView.getMeasuredHeight());
            if (line==null) {
                //说明当前行为空
                line = createNewLine(itemView);
            }
            else{
                //判断是否可以再继续添加
                if (canBeAdd(itemView, line)) {
                    //可以添加,添加到line
                    line.add(itemView);
                }else{
                    //不能添加,新建一行
                    line = createNewLine(itemView);
                }
            }
        }

        mItemHeight = getChildAt(0).getMeasuredHeight();
        //整个布局的高度是 lines的大小*个体的高度+lines+1 * 间距
        int selfHeight = (int)(lines.size()* mItemHeight +mItemVerticalSpace*(lines.size()+1)+0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth,selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new LinkedList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 测量当前行是否可以再添加
     * @param itemView
     * @param line
     * @return
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //宽度= 子view的宽度+间距(line.size()+1)*mItemHorizontalSpace)+itemView.getMeasureWidth()
        //条件:宽度<当前控件的宽度,可以添加, 否则不添加
        int totalWidth = itemView.getMeasuredWidth();
        for(View view:line){
            //叠加所有已经添加的控件的高度
            totalWidth+=view.getMeasuredWidth();
        }
        //水平间距
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        LogUtils.d(this, "totalWidth===" + totalWidth);
        return totalWidth <= mSelfWidth;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        LogUtils.d(this,"onLayout---->"+getChildCount());
        int topOffSet =(int) mItemVerticalSpace;
        for (List<View> views : lines) {
            //views是每行
            int leftOffSet = (int)mItemHorizontalSpace ;
            for (View view : views) {
                //每行里的每个item
                view.layout(leftOffSet,topOffSet,leftOffSet+view.getMeasuredWidth(),topOffSet+view.getMeasuredHeight());
                //设置左开始的位置
                leftOffSet+=view.getMeasuredWidth()+mItemHorizontalSpace;
            }
            topOffSet+=mItemHeight+mItemVerticalSpace;
        }

    }

    public void setItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
