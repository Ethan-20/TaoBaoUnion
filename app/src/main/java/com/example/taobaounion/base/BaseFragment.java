package com.example.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.taobaounion.R;

public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }
    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.base_fragment_layout, container, false);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;

    }

    /**
     * 加载各种状态的view
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        //成功的view
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        //loading的view
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        //错误页面的view
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        //空页面的view
        mEmptyView = loadEmpty(inflater, container);
        mBaseContainer.addView(mEmptyView);
        setupState(State.NONE);

    }
    /**
     * 子类通过状态来切换页面状态
     * @param state
     */
    public void setupState(State state){
        this.currentState = state;
        mSuccessView.setVisibility(currentState==State.SUCCESS?View.VISIBLE: View.GONE);
        mLoadingView.setVisibility(currentState==State.LOADING?View.VISIBLE: View.GONE);
        mErrorView.setVisibility(currentState==State.ERROR?View.VISIBLE: View.GONE);
        mEmptyView.setVisibility(currentState==State.EMPTY?View.VISIBLE: View.GONE);

    }
    private View loadEmpty(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container,false);
    }
    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container,false);
    }

    private View loadLoadingView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.fragment_loading, container,false);

    }
    protected  View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resID = getRootViewResId();
        return inflater.inflate(resID,container,false);
    }




    protected void initView(View rootView) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
        release();

    }

    private void release() {
        //释放资源
    }

    //创建presenter
    protected void initPresenter() {

    }

    //加载数据
    protected void loadData() {

    }


    protected abstract int getRootViewResId();


}
