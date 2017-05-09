package com.android.depend.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.base.component.activity.ActivityTrans;
import com.android.base.func.InputUtils;
import com.android.base.view.BarUtils;
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
    private Unbinder unbinder;

    /* 初始layout(setContent之前调用) */
    protected abstract int initObj(Intent intent);

    /* 实例化View */
    protected abstract void initView(Bundle state);

    /* 初始Data */
    protected abstract void initData(Bundle state);

    /**
     * @return 获取当前类
     */
    @SuppressWarnings("unchecked")
    public Class<T> getCls() {
        Type type = this.getClass().getGenericSuperclass();
        return (Class<T>) (((ParameterizedType) (type)).getActualTypeArguments()[0]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        InputUtils.initActivity(this); // 软键盘
        ScreenUtils.requestPortrait(this); // 竖屏
        BarUtils.requestNoTitle(this); // noTitle
        ActivityTrans.initActivity(this); //  过渡动画
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(initObj(getIntent()));
        // 每次setContentView之后都要bind一下
        unbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        initData(savedInstanceState);
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
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /* 触摸事件 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View focus = this.getCurrentFocus();
        if (null != focus) { // 点击屏幕空白区域隐藏软键盘
            return InputUtils.hideSoftInput(focus);
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

}
