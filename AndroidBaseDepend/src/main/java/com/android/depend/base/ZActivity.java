package com.android.depend.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.base.R;
import com.android.base.base.JActivity;
import com.android.base.view.DialogUtils;
import com.android.depend.utils.AnalyUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class ZActivity<T> extends JActivity<T> {

    public ZActivity mActivity;
    private ProgressDialog loading;
    private ProgressDialog progress;
    private Unbinder unbinder;

    /**
     * @return 对话框(静态会混乱)
     */
    public ProgressDialog getLoading() {
        if (loading != null) return loading;
        loading = DialogUtils.createLoading(mActivity,
                0, "", getString(R.string.wait), true);
        return loading;
    }

    /**
     * @return 进度框(静态会混乱)
     */
    public ProgressDialog getProgress() {
        if (progress != null) return progress;
        progress = DialogUtils.createProgress(mActivity,
                0, getString(R.string.wait), true, 100, 0, null);
        return progress;
    }

    /* 初始layout(setContent之前调用) */
    protected abstract int initObj(Intent intent);

    /* 实例化View */
    protected abstract void initView(Bundle state);

    /* 初始Data */
    protected abstract void initData(Bundle state);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(initObj(getIntent()));
        // 每次setContentView之后都要bind一下
        unbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyUtils.analysisOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyUtils.analysisOnPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
