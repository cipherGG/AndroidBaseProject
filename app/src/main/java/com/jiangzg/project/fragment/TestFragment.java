package com.jiangzg.project.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.base.BaseFragment;
import com.jiangzg.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment<TestFragment> {

    @Override
    protected int initObj(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return 0;
    }

    @Override
    protected void initView(View view, @Nullable Bundle state) {

    }

    @Override
    protected void initData(Bundle state) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

}
