package com.android.depend.base;

import android.os.Handler;
import android.os.Looper;

import com.android.base.base.JApp;
import com.android.depend.utils.AnalyUtils;
import com.android.depend.utils.LogUtils;
import com.android.depend.utils.RubbishUtils;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class ZApp extends JApp {

    protected Handler mainHandler; // 主线程handler
    protected ExecutorService threadPool; // 缓冲线程池
    protected Timer timer; // timer

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true); // 注解
        LogUtils.initApp(); // 打印
        AnalyUtils.initApp(); // 统计
        RubbishUtils.initApp(this); // 垃圾管理
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
