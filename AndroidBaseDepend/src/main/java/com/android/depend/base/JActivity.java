package com.android.depend.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.base.R;
import com.android.base.comp.ActivityUtils;
import com.android.base.view.BarUtils;
import com.android.base.view.DialogUtils;
import com.android.base.view.ScreenUtils;
import com.android.depend.utils.AnalyUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class JActivity<T> extends AppCompatActivity {

    public JActivity mActivity;
    public FragmentManager mFragmentManager;
    public View rootView;
    private ProgressDialog loading;
    private ProgressDialog progress;
    private Unbinder unbinder;

    /* activity跳转demo */
    private static void goActivity(Activity from) {
        Intent intent = new Intent(from, JActivity.class);
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

    /* 初始layout(setContent之前调用) */
    protected abstract int initObj(Intent intent);

    /* 实例化View */
    protected abstract void initView(Bundle savedInstanceState);

    /* 初始Data */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        initBeforeSuperCreate(mActivity);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
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

    /*
     * DecorView在这里才会有params,viewGroup在这里才能add
     * Window是WindowManager最顶层的视图，PhoneWindow是Window的唯一实现类
     * DecorView是window下的子视图,是所有应用窗口(Activity界面)的根View
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 控制DecorView的大小来控制activity的大小，可做窗口activity
        rootView = getWindow().getDecorView();
        // setFinishOnTouchOutside(true);
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
            BarUtils.requestNoTitle((AppCompatActivity) activity);
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
