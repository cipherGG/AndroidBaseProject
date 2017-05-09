package com.android.base.component;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.android.base.component.activity.ActivityStack;

import java.util.List;

/**
 * Created by gg on 2017/4/3.
 * 进程管理类
 */
public class RunningUtils {

    /**
     * 获取top的Activity的ComponentName
     */
    public static ComponentName getTopActivityName() {
        ActivityManager localActivityManager = ContextUtils.getActivityManager();
        if (localActivityManager != null) {
            List<ActivityManager.RunningTaskInfo> localList =
                    localActivityManager.getRunningTasks(1);
            if (localList != null && localList.size() > 0) {
                return localList.get(0).topActivity;
            }
        }
        return null;
    }

    /**
     * 判断是否存在Activity
     *
     * @param packageName 项目包名
     * @param className   activity全路径类名
     */
    public static boolean isActivityRegister(String packageName, String className) {
        PackageManager packageManager = ContextUtils.getPackageManager();
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);
        ComponentName componentName = intent.resolveActivity(packageManager);
        int size = packageManager.queryIntentActivities(intent, 0).size();
        return !(resolveInfo == null || componentName == null || size == 0);
    }

    /**
     * 判断服务是否运行
     *
     * @param serviceName 全路径类名 class.getName
     */
    public static boolean isServiceWork(String serviceName) {
        List<ActivityManager.RunningServiceInfo> myList = ContextUtils
                .getActivityManager().getRunningServices(Integer.MAX_VALUE);
        if (myList == null || myList.size() < 1) return false;
        for (ActivityManager.RunningServiceInfo serviceInfo : myList) {
            if (serviceInfo.service.getClassName().equals(serviceName)) return true;
        }
        return false;
    }

    /**
     * 判断App是否在前台运行
     *
     * @param packageName 项目包名 context.getPackageName
     */
    public static boolean isAppForeground(String packageName) {
        ActivityManager activityManager = ContextUtils.getActivityManager();
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.processName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            ActivityStack.finishAll();
            ActivityManager activityManager = ContextUtils.getActivityManager();
            activityManager.killBackgroundProcesses(ContextUtils.get().getPackageName());
            System.exit(0);
        } catch (Exception e) {
            // 退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(-1);
        }
    }

}
