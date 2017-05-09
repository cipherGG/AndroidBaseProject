package com.android.base.component.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gg on 2017/5/9.
 * Activity生命周期
 */
public class ActivityLifecycle {

    private static HashMap<String, LifecycleListener> listeners = new HashMap<>();

    public interface LifecycleListener {
        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle outState);

        void onActivityDestroyed(Activity activity);
    }

    /**
     * 添加生命周期监听器
     */
    public static void addLifecycleListener(String tag, LifecycleListener lifecycleListener) {
        if (tag == null || lifecycleListener == null) return;
        listeners.put(tag, lifecycleListener);
    }

    /**
     * 移除生命周期监听器
     */
    public static void removeLifecycleListener(String tag) {
        listeners.remove(tag);
    }

    /* 监听所有activity的生命周期 */
    public static void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStack.getStack().add(activity);
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityCreated(activity, savedInstanceState);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityStarted(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityResumed(activity);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityPaused(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityStopped(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivitySaveInstanceState(activity, outState);
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStack.getStack().remove(activity);
                for (Map.Entry<String, LifecycleListener> entry : listeners.entrySet()) {
                    entry.getValue().onActivityDestroyed(activity);
                }
            }
        });
    }

}
