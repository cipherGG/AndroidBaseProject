package com.android.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class BaseApp extends MultiDexApplication {

    protected static BaseApp instance;

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
    }

    public static BaseApp get() {
        return instance;
    }
}
