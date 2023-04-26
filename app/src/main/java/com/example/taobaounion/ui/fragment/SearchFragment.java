package com.example.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Override
    protected void initPresenter() {

    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
