package com.android.base.component.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.android.base.component.ContextUtils;

import java.util.List;

/**
 * Created by gg on 2017/5/9.
 * Activity管理类
 */
public class ActivityUtils {

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
