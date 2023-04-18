package com.example.taobaounion.ui.fragment;

import android.view.View;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;

public class SelectedFragment extends BaseFragment {


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
