package com.android.base.component.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class AppNative extends MultiDexApplication {

    public static final String LOG_TAG = "AndroidBaseNative";

    private static AppNative instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 大项目需要分包
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static AppNative get() {
        return instance;
    }
}
