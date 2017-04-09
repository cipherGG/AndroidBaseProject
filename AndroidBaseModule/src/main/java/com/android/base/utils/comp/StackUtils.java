package com.android.base.utils.comp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import java.util.Stack;

/**
 * Created by gg on 2017/4/6.
 * 任务栈管理类
 */
public class StackUtils {

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

    /* 任务栈 */
    private static final Stack<Activity> STACK = new Stack<>();

    public static Stack<Activity> get() {
        return STACK;
    }

    public static boolean addActivity(Activity activity) {
        return STACK.add(activity);
    }

    public static boolean removeActivity(Activity activity) {
        return STACK.remove(activity);
    }

    /* 关闭前台的activity */
    public static void finishTopActivity() {
        Activity activity = STACK.lastElement();
        if (activity == null) return;
        removeActivity(activity);
        activity.finish();
    }

    /* 关闭最底下的activity */
    public static void finishRootActivity() {
        Activity activity = STACK.firstElement();
        if (activity == null) return;
        removeActivity(activity);
        activity.finish();
    }

    /*关闭task底部activity */
    public static void finishTask(int taskId) {
        for (Activity activity : STACK) {
            int id = activity.getTaskId();
            if (taskId != id) continue;
            removeActivity(activity);
            activity.finish();
        }
    }

    /* 关闭所有activity */
    public static void finishAll() {
        for (Activity activity : STACK) {
            removeActivity(activity);
            activity.finish();
        }
        STACK.clear();
    }

    public static void finish(Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && activity.isTaskRoot()) {
            activity.finishAndRemoveTask();
        } else {
            activity.finish();
        }
    }

}
