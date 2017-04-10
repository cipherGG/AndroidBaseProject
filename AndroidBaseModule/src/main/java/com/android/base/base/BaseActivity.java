package com.android.base.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.base.R;
import com.android.base.utils.comp.ActivityUtils;
import com.android.base.utils.func.AnalyUtils;
import com.android.base.utils.net.NetUtils;
import com.android.base.utils.view.DialogUtils;
import com.android.base.utils.view.ScreenUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    public BaseActivity mActivity;
    public FragmentManager mFragmentManager;
    private ProgressDialog loading;
    private ProgressDialog progress;
    private Unbinder unbinder;

    /* activity跳转demo */
    private static void goActivity(Activity from) {
        Intent intent = new Intent(from, BaseActivity.class);
        // intent.putExtra();
        ActivityUtils.startActivity(from, intent);
    }

    /* activity跳转demo */
    private static void goActivity(Fragment from) {
        Intent intent = new Intent(from.getActivity(), BaseActivity.class);
        // intent.putExtra();
        ActivityUtils.startActivity(from, intent);
    }

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

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        // 需要重新initView()
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        // 需要重新initView()
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        unbinder = ButterKnife.bind(this);
        // 需要重新initView()
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
        unbinder = ButterKnife.bind(this);
        // 需要重新initView()
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        initBeforeSuperCreate(mActivity);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
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

    /* activity初始化 */
    private void initBeforeSuperCreate(Activity activity) {
        Window window = activity.getWindow();
        // 键盘不会遮挡输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 不自动弹键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 总是隐藏键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 竖屏
        ScreenUtils.requestPortrait(activity);
        // titleBar
        if (activity instanceof AppCompatActivity) {
            ScreenUtils.requestNoTitle((AppCompatActivity) activity);
        }
        // 专门的跳转方式才会有过场效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade()); // 下一个activity进场
            window.setExitTransition(new Fade()); //  当前activity向后时退场
            // window.setReenterTransition(slideIn); // 上一个activity进场
            // window.setReturnTransition(slideOut); // 当前activity向前时退场
        }
    }

}
