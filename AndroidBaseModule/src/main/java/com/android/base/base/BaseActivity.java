package com.android.base.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.android.base.R;
import com.android.base.utils.comp.ActivityUtils;
import com.android.base.utils.func.AnalyUtils;
import com.android.base.utils.view.DialogUtils;
import com.android.base.utils.net.NetUtils;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    /* 子类复制类似方法 实现跳转 */
    private static void goActivity(Activity from) {
        Intent intent = new Intent(from, BaseActivity.class);
        // intent.putExtra();
        ActivityUtils.startActivity(from, intent);
    }

    /* 子类复制类似方法 实现跳转 */
    private static void goActivity(Fragment from) {
        Intent intent = new Intent(from.getActivity(), BaseActivity.class);
        // intent.putExtra();
        ActivityUtils.startActivity(from, intent);
    }

    public String logTag = "BaseActivity";
    public BaseActivity mActivity;
    public FragmentManager mFragmentManager;
    public ProgressDialog loading;
    public ProgressDialog progress;
    public Intent mIntent;
    private Unbinder unbinder;

    /* 初始layout(setContent之前调用) */
    protected abstract int initObj(Bundle savedInstanceState);

    /* 实例化View */
    protected abstract void initView();

    /* 初始Data */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logTag = getCls();
        mActivity = this;
        ActivityUtils.initBeforeSuperCreate(mActivity);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        loading = DialogUtils.createLoading(mActivity,
                0, "", getString(R.string.wait), true);
        progress = DialogUtils.createProgress(mActivity,
                0, getString(R.string.wait), true, 100, 0, null);
        mIntent = getIntent();
        setContentView(initObj(savedInstanceState)); // 这之后 页面才会加载出来
    }

    /* setContentView()或addContentView()后调用,view只是加载出来，没有实例化.
     * 为了页面的加载速度，不要在onContentChanged前做过多的操作 */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this); // 每次setContentView之后都要bind一下
        initView(); // 二次setContentView之后控件是以前view的，所以要重新实例化一次
        initData(); // 必须保证initView执行完，onStart不行
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetUtils.get().isAvailable();
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

    /* 触摸事件 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) { // 点击屏幕空白区域隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow
                    (this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return true;
    }

    /* 菜单事件 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 返回键
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* 获取当前类的 */
    @SuppressWarnings("unchecked")
    private String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

}
