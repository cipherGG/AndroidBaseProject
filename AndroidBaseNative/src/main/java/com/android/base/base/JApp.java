package com.android.base.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.base.component.activity.ActivityStack;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class JApp extends MultiDexApplication {

    protected static JApp instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 大项目需要分包
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActivityStack.initApp(this);
    }

    public static JApp get() {
        return instance;
    }
}
