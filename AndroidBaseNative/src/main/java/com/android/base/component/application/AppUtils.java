package com.android.base.component.application;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.android.base.component.activity.ActivityStack;
import com.android.base.file.FileUtils;
import com.android.base.str.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/12.
 * describe  App相关工具类
 */
public class AppUtils {

    private static AppUtils instance;

    private String name; // APP名称
    private Drawable icon; // 图标
    private String packageName; // 包名
    private String packagePath; // 包路径
    private String versionName; // 版本
    private int versionCode; // 版本
    private Signature[] signature; // 签名
    private boolean isSystem; // 是否是用户级别
    private String resDir; // SDCard/包名/
    private String logDir; // SDCard/包名/log/
    private String filesDir; // SDCard/Android/data/包名/files/ 或者是sys的
    private String cacheDir; // SDCard/Android/data/包名/cache/ 或者是sys的

    /* 获取当前App信息(Permission) */
    public static AppUtils get() {
        if (instance != null) return instance;
        instance = new AppUtils();
        PackageManager pm = ContextUtils.get().getPackageManager();
        try { // packageName可换成其他的app包名
            PackageInfo pi = pm.getPackageInfo(ContextUtils.get().getApplicationContext().getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            Signature[] signatures = pi.signatures;
            boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags)
                    == ApplicationInfo.FLAG_SYSTEM;
            // 赋初始值(不需要权限)
            instance.setName(ai.loadLabel(pm).toString());
            instance.setIcon(ai.loadIcon(pm));
            instance.setPackageName(pi.packageName);
            instance.setPackagePath(ai.sourceDir);
            instance.setVersionCode(pi.versionCode);
            instance.setVersionName(pi.versionName);
            instance.setSignature(signatures);
            instance.setSystem(isSystem);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 自定义资源路径(部分手机有差别)
     */
    public String getResDir() {
        if (!StringUtils.isEmpty(resDir)) return resDir;
        String dir;
        if (isSDCardEnable()) {
            dir = getSDCardPath() + packageName + File.separator;
        } else {
            dir = getRootPath() + packageName + File.separator;
        }
        resDir = dir;
        FileUtils.createOrExistsDir(resDir); // 并创建
        return resDir;
    }

    /**
     * 自定义Log路径
     */
    public String getLogDir() {
        if (!StringUtils.isEmpty(logDir)) return logDir;
        logDir = getResDir() + "log" + File.separator;
        FileUtils.createOrExistsDir(logDir); // 并创建
        return logDir;
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用的包名/files/
     * 如果不存在，则获取 /data/data/<application package>/files
     */
    public String getFilesDir(String dirName) {
        File dir;
        if (isSDCardEnable()) {
            dir = ContextUtils.get().getExternalFilesDir(dirName);
        } else {
            dir = ContextUtils.get().getFilesDir();
        }
        if (dir != null) {
            filesDir = dir.getAbsolutePath();
        }
        return filesDir;
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用包名/cache/
     * 如果不存在，则获取 /data/data/<application package>/cache
     */
    public String getCacheDir() {
        if (!StringUtils.isEmpty(cacheDir)) return cacheDir;
        File dir;
        if (isSDCardEnable()) {
            dir = ContextUtils.get().getExternalCacheDir();
        } else {
            dir = ContextUtils.get().getCacheDir();
        }
        if (dir != null) {
            cacheDir = dir.getAbsolutePath();
        }
        return cacheDir;
    }

    public Signature[] getSignature() {
        return signature;
    }

    public void setSignature(Signature[] signature) {
        this.signature = signature;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 获取根目录
     */
    public String getRootPath() {
        return Environment.getRootDirectory() + File.separator;
    }

    /**
     * 获取SD卡路径 一般是/storage/emulated/0/
     */
    public String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     */
    public boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
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
