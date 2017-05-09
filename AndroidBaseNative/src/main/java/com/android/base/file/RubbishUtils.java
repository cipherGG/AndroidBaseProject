package com.android.base.file;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.text.format.Formatter;

import com.android.base.component.ContextUtils;
import com.android.base.func.AppUtils;
import com.android.base.other.ConvertUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gg on 2017/4/9.
 * 垃圾管理类
 */
public class RubbishUtils {

    public static void initApp(final Application app) {
        // 监听当前app的内存 和 配置,可撤销
        app.registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
//                LogUtils.d("杀死一个进程以求更多内存(level) ---> " + level);
            }

            @Override
            public void onLowMemory() {
//                LogUtils.e("内存不足,清理内存以获取更多内存");
                RubbishUtils.clearMemory();
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                StringBuilder status = new StringBuilder();
                status.append("配置发生变化").append("\n");
                Configuration cfg = app.getResources().getConfiguration();
                status.append("fontScale:").append(cfg.fontScale).append("\n");
                status.append("hardKeyboardHidden:").append(cfg.hardKeyboardHidden).append("\n");
                status.append("keyboard:").append(cfg.keyboard).append("\n");
                status.append("keyboardHidden:").append(cfg.keyboardHidden).append("\n");
                status.append("locale:").append(cfg.locale).append("\n");
                status.append("mcc:").append(cfg.mcc).append("\n");
                status.append("mnc:").append(cfg.mnc).append("\n");
                status.append("navigation:").append(cfg.navigation).append("\n");
                status.append("navigationHidden:").append(cfg.navigationHidden).append("\n");
                status.append("orientation:").append(cfg.orientation).append("\n");
                status.append("screenHeightDp:").append(cfg.screenHeightDp).append("\n");
                status.append("screenWidthDp:").append(cfg.screenWidthDp).append("\n");
                status.append("screenLayout:").append(cfg.screenLayout).append("\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("densityDpi:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("smallestScreenWidthDp:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("touchscreen:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("uiMode:").append(cfg.densityDpi).append("\n");
                }
//                LogUtils.d(status.toString());
            }
        });
    }

    /**
     * **********************************缓存**********************************
     * 获取具有缓存的文件夹
     */
    public static List<String> getCacheFiles() {
        String filesDir = AppUtils.get().getFilesDir("");
        String cacheDir = AppUtils.get().getCacheDir();
        File internalFilesDir = ContextUtils.get().getFilesDir();
        File internalCacheDir = ContextUtils.get().getCacheDir();
//        File cacheFile = GlideUtils.getCacheFile();

        List<String> filesList = new ArrayList<>();
        filesList.add(filesDir);
        filesList.add(cacheDir);
        filesList.add(internalFilesDir.getAbsolutePath());
        filesList.add(internalCacheDir.getAbsolutePath());
//        filesList.add(cacheFile.getAbsolutePath());
        return filesList;
    }

    /**
     * 获取具有缓存的文件大小
     */
    public static long getCacheLength() {
        List<String> cacheFiles = getCacheFiles();
        long size = 0;
        for (String cache : cacheFiles) {
            size += new File(cache).length();
        }
        return size;
    }

    /**
     * 获取具有缓存的文件大小
     */
    public static String getCacheSize() {
        long cacheLength = getCacheLength();
        return ConvertUtils.byte2FitSize(cacheLength);
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        List<String> cacheFiles = getCacheFiles();
        for (String cache : cacheFiles) {
            FileUtils.deleteFilesAndDirInDir(new File(cache));
        }
//        GlideUtils.clearCache();
    }

    /**
     * **********************************资源**********************************
     * 获取资源文件夹
     */
    public static String getResFile() {
        return AppUtils.get().getResDir();
    }

    /**
     * 获取具有缓存的文件大小
     */
    public static long getResLength() {
        File resDir = new File(getResFile());
        return resDir.length();
    }

    /**
     * 获取具有缓存的文件大小
     */
    public static String getResSize() {
        long resLength = getResLength();
        return ConvertUtils.byte2FitSize(resLength);
    }

    /**
     * 清除所有资源
     */
    public static void clearRes() {
        String resDir = getResFile();
        FileUtils.deleteFilesAndDirInDir(resDir);
    }

    /**
     * **********************************外存**********************************
     * 外存总共空间
     */
    public static String getExternalTotal() {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
        return FileUtils.getFileSize(totalSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable() {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
        return FileUtils.getFileSize(usableSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree() {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        return FileUtils.getFileSize(freeSpace);
    }

    /**
     * **********************************内存**********************************
     * 清理内存
     */
    public static void clearMemory() {
//        GlideUtils.clearMemory();
        System.gc();
        // 也可以清理掉一些优先级低的进程
    }

    /**
     * 获取手机内存信息
     */
    private static ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ContextUtils.getActivityManager().getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取总共运存
     */
    public static String getTotalMem(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Formatter.formatFileSize(context, getMemoryInfo().totalMem);
        }
        return "";
    }

    /**
     * 获取可用运存
     */
    public static String getAvailMem(Context context) {
        return Formatter.formatFileSize(context, getMemoryInfo().availMem);
    }

    /**
     * 系统内存不足的阀值，即临界值
     */
    public static String getThreshold(Context context) {
        return Formatter.formatFileSize(context, getMemoryInfo().threshold);
    }

    /**
     * 如果当前可用内存 <= threshold，该值为真
     */
    public static boolean isLowMemory(Context context) {
        return getMemoryInfo().availMem <= getMemoryInfo().threshold;
    }

}
