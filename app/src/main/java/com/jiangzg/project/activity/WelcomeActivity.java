package com.jiangzg.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.base.view.BarUtils;
import com.android.depend.base.JActivity;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 启动界面
 */
public class WelcomeActivity extends JActivity<WelcomeActivity> {

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
                HomeActivity.goActivity(mActivity);
//            }
//        }, 1000);
    }

}
