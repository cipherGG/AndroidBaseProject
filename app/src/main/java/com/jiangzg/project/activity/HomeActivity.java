package com.jiangzg.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.android.base.base.BaseActivity;
import com.android.base.domain.RxEvent;
import com.android.base.domain.Version;
import com.android.base.utils.ActivityUtils;
import com.android.base.utils.RxUtils;
import com.android.base.utils.ToastUtils;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;
import com.jiangzg.project.utils.ViewUtils;

import java.util.Date;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 主界面
 */
public class HomeActivity extends BaseActivity<HomeActivity> {

    private Observable<Version> observable;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        ViewUtils.initTop(mActivity, "主页面");
    }

    @Override
    protected void initData() {
        loading.show();
//        observable = RxUtils.get().register(1, new Action1<Version>() {
//            @Override
//            public void call(Version version) {
//                ToastUtils.get().show(version.getChangeLog());
//            }
//        });
//        Handler handler = MyApp.get().getHandler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Version version = new Version();
//                version.setChangeLog("sasasasasa");
//                RxEvent<Version> event = new RxEvent<>(1, version);
//                RxUtils.get().post(event);
//            }
//        }, 3000);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                RxUtils.get().unregister(1, observable);
//                RxUtils.get().unregister(12, observable);
//            }
//        }, 5000);


    }

    private Long lastExitTime = 0L; //最后一次退出时间

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Long nowTime = new Date().getTime();

            if (nowTime - lastExitTime > 2000) { // 第一次按
                ToastUtils.get().show(R.string.press_again_exit);
            } else { // 返回键连按两次
                System.exit(0); // 真正退出程序
            }
            lastExitTime = nowTime;
        }
        return true;
    }

}
