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
                .methodCount(0)// 3以上才能显示调用方法
                .hideThreadInfo() // 隐藏线程显示
                .logLevel(LogLevel.FULL);// 打印开关
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
     * 实体类
     */
    public static void json(String json) {
        Logger.json(json);
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
