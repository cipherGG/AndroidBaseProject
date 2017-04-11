package com.jiangzg.project;

import com.android.base.base.BaseApp;

public class MyApp extends BaseApp {
    public static boolean DEBUG = true; // 测试模式(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();

//        PushUtils.initAPP(); // 推送
//        ShareUtils.initApp(this); // 分享/授权
//        ScanUtils.initApp(this);
    }

}
