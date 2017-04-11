package com.android.base.utils.func;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;

import java.io.File;
import java.util.List;

/**
 * Created by gg on 2017/4/3.
 */

public class PackageUtils {
//    public static PackageManager getPackageManager(Context context) {
//        return context.getPackageManager();
//    }
//
//    public static List<PackageInfo> getPackageInfos(PackageManager packageManager) {
//        return packageManager.getInstalledPackages(0);
//    }
//
//    public static List<PackageInfo> getPackageInfos(Context context) {
//        PackageManager packageManager = getPackageManager(context);
//
//        return packageManager.getInstalledPackages(0);
//    }
//
//    public static PackageInfo getPackageInfo(PackageManager packageManager, String packageName) {
//        try {
//            return packageManager.getPackageInfo(packageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static PackageInfo getPackageInfo(Context context, String name) {
//        try {
//            return getPackageManager(context).getPackageInfo(name, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static PackageInfo getPackageInfo(Context context) {
//        try {
//            return getPackageManager(context).getPackageInfo(context.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static Drawable getPackageIcon(PackageInfo packageInfo, Context context) {
//        return packageInfo.applicationInfo.loadIcon(getPackageManager(context));
//    }
//
//    public static Drawable getPackageIcon(PackageInfo packageInfo, PackageManager packageManager) {
//        return packageInfo.applicationInfo.loadIcon(packageManager);
//    }
//
//    public static String getApkName(PackageInfo packageInfo, Context context) {
//        return packageInfo.applicationInfo.loadLabel(getPackageManager(context)).toString();
//    }
//
//    public static String getApkName(PackageInfo packageInfo, PackageManager packageManager) {
//        return packageInfo.applicationInfo.loadLabel(packageManager).toString();
//    }
//
//    public static String getPackageName(PackageInfo packageInfo) {
//        return packageInfo.packageName;
//    }
//
//    public static long getPackageSize(PackageInfo packageInfo) {
//        // packageInfo.applicationInfo.sourceDir.length(); 行不行
//        String sourceDir = packageInfo.applicationInfo.sourceDir;
//
//        return new File(sourceDir).length();
//    }
//
//    public static int getApkRunMemory(ActivityManager activityManager, ActivityManager.RunningAppProcessInfo processInfo) {
//        Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
//
//        return memoryInfo[0].getTotalPrivateDirty() * 1024;
//    }
//
//    public static boolean isUser(PackageInfo packageInfo) {
//        int flags = packageInfo.applicationInfo.flags;
//
//        return (flags & ApplicationInfo.FLAG_SYSTEM) == 0;
//    }
//
//    public static boolean isRom(PackageInfo packageInfo) {
//        int flags = packageInfo.applicationInfo.flags;
//
//        return (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0;
//    }
//
//    /**
//     * 获取指定包名的Context对象
//     */
//    public static Context creatPackageContext(Context context, String packageName) {
//        try {
//            return context.createPackageContext(packageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取context所在app的包名
//     */
//    public static String getPackageName(Context context) {
//
//        return context.getPackageName();
//    }
//
//    /**
//     * 获取context所在app的路径
//     */
//    public static String getPackagePath(Context context) {
//
//        return context.getPackageResourcePath();
//    }

    /**
     * 应用包，应用程序，<<对应manifests文件>>
     */
    public static PackageManager getPackageManager(Context context) {

        return context.getPackageManager();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>manifest<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回PackageInfo对象, 相当于Manifest里的manifest节点, 一般都是在用这个对象
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return getPackageManager(context).getPackageInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回所有PackageInfo
     */
    public static List<PackageInfo> getInstalledPackages(Context context) {

        return getPackageManager(context).getInstalledPackages(0);// 0为所有的
    }

    /**
     * 获取应用名称
     */
    public static String getApplicationLabel(Context context, PackageInfo packageInfo) {

        return packageInfo.applicationInfo.loadLabel(getPackageManager(context)).toString();
    }

    /**
     * 通过packageInfo查找Icon
     */
    public static Drawable getApplicationIcon(Context context, PackageInfo packageInfo) {

        return packageInfo.applicationInfo.loadIcon(getPackageManager(context));
    }

    /**
     * 获取应用大小，单位已经换算好
     */
    public static String getApplicationSize(Context context, PackageInfo packageInfo) {

        long length = new File(packageInfo.applicationInfo.sourceDir).length();

        return Formatter.formatFileSize(context, length);
    }

    /**
     * 一般用来获取其他app的包名
     */
    public static String getPackageName(PackageInfo packageInfo) {

        return packageInfo.packageName;
    }

    /**
     * getInstalledApplications(ApplicationInfo.FLAG_SYSTEM);效果一样
     */
    public static boolean isUser(PackageInfo packageInfo) {
        int flags = packageInfo.applicationInfo.flags;

        return (flags & ApplicationInfo.FLAG_SYSTEM) == 0;
    }

    /**
     * getInstalledApplications(ApplicationInfo.FLAG_EXTERNAL_STORAGE);效果一样
     */
    public static boolean isRom(PackageInfo packageInfo) {
        int flags = packageInfo.applicationInfo.flags;

        return (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0;
    }

    /**
     * 是versionName，不是versionCode
     */
    public static String getAppVersion(PackageInfo packageInfo) {

        return packageInfo.versionName;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Application<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回ApplicationInfo对象, 相当于Manifest里的Application节点
     */
    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        try {
            return getPackageManager(context).getApplicationInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回所有ApplicationInfo
     */
    public static List<ApplicationInfo> getInstalledApplications(Context context) {

        return getPackageManager(context).getInstalledApplications(0);
    }

    /**
     * 通过applicationInfo查找Icon
     */
    public static Drawable getApplicationIcon(Context context, ApplicationInfo applicationInfo) {

        return getPackageManager(context).getApplicationIcon(applicationInfo);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Activity<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回所有ResolveInfo对象(本质上是Activity)
     *
     * @param intent intent intent 查寻条件，Activity所配置的action和category
     *               以下是启动应用
     *               Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
     *               mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
     * @param flags  flags  MATCH_DEFAULT_ONLY  ：Category必须带有CATEGORY_DEFAULT的Activity，才匹配
     *               GET_INTENT_FILTERS  ：匹配Intent条件即可
     *               GET_RESOLVED_FILTER ：匹配Intent条件即可
     */
    public static List<ResolveInfo> queryIntentActivities(Context context,
                                                          Intent intent, int flags) {

        return getPackageManager(context).queryIntentActivities(intent, flags);
    }

    public static ResolveInfo resolveActivity(Context context, Intent intent, int flags) {

        return getPackageManager(context).resolveActivity(intent, flags);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Service<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回所有ResolveInfo对象(本质上是Service)
     *
     * @param intent intent 查寻条件，Service所配置的action和category
     * @param flags  MATCH_DEFAULT_ONLY  ：Category必须带有CATEGORY_DEFAULT的Activity，才匹配
     *               GET_INTENT_FILTERS  ：匹配Intent条件即可
     *               GET_RESOLVED_FILTER ：匹配Intent条件即可
     */
    public static List<ResolveInfo> queryIntentServices(Context context, Intent intent, int flags) {

        return getPackageManager(context).queryIntentServices(intent, flags);
    }

    public static ResolveInfo resolveService(Context context, Intent intent, int flags) {

        return getPackageManager(context).resolveService(intent, flags);
    }
}
