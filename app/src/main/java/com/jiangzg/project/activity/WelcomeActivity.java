package com.jiangzg.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.android.base.view.BarUtils;
import com.android.depend.base.ZActivity;
import com.jiangzg.project.R;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 启动界面
 */
public class WelcomeActivity extends ZActivity<WelcomeActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected int initObj(Intent intent) {
        BarUtils.hideStatusBar(this);
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        goHome();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /* 跳转主页 */
    private void goHome() {
//        MyApp.get().getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                HomeActivity.goActivity(mActivity);
//            }
//        }, 1000);
    }

}
