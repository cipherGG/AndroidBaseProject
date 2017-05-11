package com.android.base.component.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.base.component.activity.ActivityLifecycle;
import com.android.base.file.CleanUtils;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class AppNative extends MultiDexApplication {

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