package com.android.depend.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.base.R;
import com.android.base.base.JActivity;
import com.android.base.comp.ActivityTrans;
import com.android.base.view.DialogUtils;
import com.android.depend.utils.AnalyUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    /**
     * @return 获取当前类(影响性能, 所以需要被动获取)
     */
    @SuppressWarnings("unchecked")
    public Class<T> getCls() {
        Type type = this.getClass().getGenericSuperclass();
        return (Class<T>) (((ParameterizedType) (type)).getActualTypeArguments()[0]);
    }

    /* 初始layout(setContent之前调用) */
    protected abstract int initObj(Intent intent);

    /* 实例化View */
    protected abstract void initView(Bundle savedInstanceState);

    /* 初始Data */
    protected abstract void initData(Bundle savedInstanceState);

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
