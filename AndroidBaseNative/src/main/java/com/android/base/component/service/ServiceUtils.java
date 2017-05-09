package com.android.base.component.service;

import android.app.ActivityManager;

import com.android.base.component.application.AppContext;

import java.util.List;

/**
 * Created by gg on 2017/5/9.
 * Service工具类
 */
public class ServiceUtils {

    /**
     * 判断服务是否运行
     *
     * @param serviceName 全路径类名 class.getName
     */
    public static boolean isServiceWork(String serviceName) {
        List<ActivityManager.RunningServiceInfo> myList = AppContext
                .getActivityManager().getRunningServices(Integer.MAX_VALUE);
        if (myList == null || myList.size() < 1) return false;
        for (ActivityManager.RunningServiceInfo serviceInfo : myList) {
            if (serviceInfo.service.getClassName().equals(serviceName)) return true;
        }
        return false;
    }

}
