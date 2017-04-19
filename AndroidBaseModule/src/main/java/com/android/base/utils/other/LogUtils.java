package com.android.base.utils.other;

import android.text.TextUtils;

import com.android.base.R;
import com.android.base.utils.comp.ContextUtils;
import com.android.base.utils.file.FileUtils;
import com.android.base.utils.func.AppUtils;
import com.android.base.utils.time.TimeUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Jiang on 2016/10/08
 * <p/>
 * 日志管理工具类
 */
public class LogUtils {

    public static void initApp() {
        String logTag = ContextUtils.get().getString(R.string.app_name);
        Logger.init(logTag) // 打印tag
                .setMethodCount(3)// 3以上才能显示调用方法
                .hideThreadInfo() // 隐藏线程显示
                .setLogLevel(LogLevel.FULL); // 打印开关
    }

    /**
     * 调试
     */
    public static void d(String print) {
        Logger.d(print);
    }

    public static void d(String tag, String print) {
        Logger.d(tag, print);
    }

    public static void d(String print, int methodCount) {
        Logger.d(print, methodCount);
    }

    /**
     * 警告
     */
    public static void e(String print) {
        Logger.e(print);
    }

    public static void e(String tag, String print) {
        Logger.e(tag, print);
    }

    public static void e(String print, int methodCount) {
        Logger.e(print, methodCount);
    }

    /**
     * 异常
     */
    public static void e(Exception e) {
        Logger.e(e);
    }

    public static void e(Throwable ex) {
        Logger.e(ex.toString());
    }

    public static void e(String tag, Exception e) {
        Logger.e(tag, e);
    }

    public static void e(String tag, Throwable ex) {
        Logger.e(tag, ex.toString());
    }

    /**
     * 实体类
     */
    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.json(tag, json);
    }

    public static void json(String json, int methodCount) {
        Logger.json(json, methodCount);
    }

    /**
     * 记录日志
     */
    public static void writeLog2File(String content) {
        if (TextUtils.isEmpty(content)) return;
        String logDir = AppUtils.get().getLogDir();
        String logFileName = TimeUtils.genBillTime() + ".txt";
        File logFile = new File(logDir, logFileName);
        FileUtils.createFileByDeleteOldFile(logFile);
        FileUtils.writeFileFromString(logFile, content, true);
    }

}
