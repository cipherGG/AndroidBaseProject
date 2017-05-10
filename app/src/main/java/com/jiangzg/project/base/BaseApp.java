package com.jiangzg.project.base;

import android.os.Handler;
import android.os.Looper;

import com.android.base.component.activity.ActivityLifecycle;
import com.android.base.component.application.AppNative;
import com.android.base.file.CleanUtils;
import com.android.depend.utils.AnalyUtils;
import com.android.depend.utils.LogUtils;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Application的基类
 */
public class BaseApp extends AppNative {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true);
        LogUtils.initApp();
        AnalyUtils.initApp();
//        ScanUtils.initApp(this);
    }

    private Handler mainHandler; // 主线程handler
    private ExecutorService threadPool; // 缓冲线程池
    private Timer timer; // timer

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
