package com.jiangzg.project;

import com.android.base.base.BaseApp;
import com.android.base.utils.net.RetrofitUtils;
import com.jiangzg.project.utils.API;
import com.jiangzg.project.utils.SPUtils;

public class MyApp extends BaseApp {
    public static boolean DEBUG = true; // 测试模式(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();

//        PushUtils.initAPP(); // 推送
//        ShareUtils.initApp(this); // 分享/授权
//        ScanUtils.initApp(this);
        RetrofitUtils.initApp(new RetrofitUtils.InitListener() {
            @Override
            public String getUserToken() {
                return SPUtils.getUser().getUserToken();
            }

            @Override
            public String getBaseURL() {
                return API.BASE_URL;
            }

            @Override
            public String getApiKey() {
                return "";
            }
        });
    }

}
