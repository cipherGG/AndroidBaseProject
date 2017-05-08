package com.android.base.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.base.utils.comp.StackUtils;
import com.android.base.utils.file.RubbishUtils;
import com.android.base.utils.other.AnalyUtils;
import com.android.base.utils.other.LogUtils;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class JApp extends MultiDexApplication {

    protected static JApp instance;  // MyApp实例
    protected Handler mainHandler; // 主线程handler
    protected ExecutorService threadPool; // 缓冲线程池
    protected Timer timer; // timer

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 大项目需要分包
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ButterKnife.setDebug(true); // 注解
        LogUtils.initApp(); // 打印
        AnalyUtils.initApp(); // 统计
        StackUtils.initApp(this); // activity管理
        RubbishUtils.initApp(this); // 垃圾管理
    }

    public static JApp get() {
        return instance;
    }

    public Handler getHandler() {
        if (null == mainHandler) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public ExecutorService getThread() {
        if (null == threadPool) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public Timer getTimer() {
        if (null == timer) {
            timer = new Timer();
        }
        return timer;
    }

}
