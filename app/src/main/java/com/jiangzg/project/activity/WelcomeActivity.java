package com.jiangzg.project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.base.base.BaseActivity;
import com.android.base.utils.view.BarUtils;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 启动界面
 */
public class WelcomeActivity extends BaseActivity<WelcomeActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.hideStatusBar(this);
        setContentView(R.layout.activity_welcome);
        goHome();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /* 跳转主页 */
    private void goHome() {
        MyApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1000);
    }

}
