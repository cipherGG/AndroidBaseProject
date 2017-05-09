package com.android.base.component.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.Stack;

/**
 * Created by gg on 2017/4/6.
 * 任务栈管理类
 */
public class ActivityStack {

    /**
     * 转变前台的activity栈(开启和改变栈都需要调用)
     * 1.FLAG_ACTIVITY_NEW_TASK 决定是否需要开启新的任务栈
     * 2.taskAffinity 决定是否可以开启新的任务栈 每个activity都声明 不写或一样则不开启新的
     * 3.launchMode 决定新的任务栈的启动模式 根activity需要
     * 4.FLAG_ACTIVITY_NO_ANIMATION rootActivity不能有切换动画
     */
    public static void changeTask(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    // 监听所有activity的生命周期
    public static void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStack.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStack.removeActivity(activity);
            }
        });
    }

    private static Stack<Activity> STACK; // 任务栈

    public static Stack<Activity> getStack() {
        if (STACK == null) {
            STACK = new Stack<>();
        }
        return STACK;
    }

    public static boolean addActivity(Activity activity) {
        return getStack().add(activity);
    }

    public static boolean removeActivity(Activity activity) {
        return getStack().remove(activity);
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return getStack().size();
    }

    /**
     * 获取Activity
     */
    public Activity getActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : getStack()) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 获取前台的activity
     */
    public static Activity getTop() {
        Stack<Activity> stack = getStack();
        if (stack.isEmpty()) return null;
        return stack.lastElement();
    }

    /**
     * 获取底部的activity
     */
    public static Activity getBottom() {
        Stack<Activity> stack = getStack();
        if (stack.isEmpty()) return null;
        return stack.firstElement();
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : getStack()) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     */
    public void finishOthersActivity(Class<?> cls) {
        for (Activity activity : getStack()) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭前台的activity
     */
    public static void finishTop() {
        Stack<Activity> stack = getStack();
        if (stack.isEmpty()) return;
        Activity activity = stack.lastElement();
        finishActivity(activity);
    }

    /**
     * 关闭最底下的activity
     */
    public static void finishBottom() {
        Stack<Activity> stack = getStack();
        if (stack.isEmpty()) return;
        Activity activity = stack.firstElement();
        finishActivity(activity);
    }

    /**
     * 关闭task底部activity
     */
    public static void finishTask(int taskId) {
        for (Activity activity : getStack()) {
            int id = activity.getTaskId();
            if (taskId != id) continue;
            finishActivity(activity);
        }
    }

    /**
     * 关闭所有activity
     */
    public static void finishAll() {
        for (Activity activity : getStack()) {
            finishActivity(activity);
        }
        getStack().clear();
    }

    private static void finishActivity(Activity activity) {
        if (activity == null) return;
        removeActivity(activity);
        activity.finish();
    }

    /**
     * 关闭activity
     */
    public static void finish(Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAndRemoveTask();
        } else {
            activity.finish();
        }
    }

}
