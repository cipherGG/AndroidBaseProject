package com.android.base.utils.sys;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;

import com.android.base.utils.sys.ContextUtils;

/**
 * Created by gg on 2017/4/4.
 * 运存管理类
 */
public class MemoryUtils {

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>运存<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 获取手机内存信息
     */
    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ContextUtils.getActivityManager().getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取总共运存
     */
    public static String getTotalMem(Context context) {

        return Formatter.formatFileSize(context, getMemoryInfo(context).totalMem);
    }

    /**
     * 获取可用运存
     */
    public static String getAvailMem(Context context) {

        return Formatter.formatFileSize(context, getMemoryInfo(context).availMem);
    }

    /**
     * 系统内存不足的阀值，即临界值
     */
    public static String getThreshold(Context context) {

        return Formatter.formatFileSize(context, getMemoryInfo(context).threshold);
    }

    /**
     * 如果当前可用内存 <= threshold，该值为真
     */
    public static boolean isLowMemory(Context context) {

        return getMemoryInfo(context).availMem <= getMemoryInfo(context).threshold;
    }


}
