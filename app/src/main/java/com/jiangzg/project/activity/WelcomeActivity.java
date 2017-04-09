package com.jiangzg.project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.base.base.BaseActivity;
import com.android.base.utils.comp.StackUtils;
import com.android.base.utils.view.ScreenUtils;
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
    protected int initObj(Bundle savedInstanceState) {
        ScreenUtils.hideStatusBar(mActivity);
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        int taskId = getTaskId();
        boolean taskRoot = StackUtils.isTaskRoot(mActivity);
//        Stack<StackUtils.Task> tasks = StackUtils.get();
        logTag = "";
        goHome();
    }

    /* 跳转主页 */
    private void goHome() {
        MyApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1000);
//         立刻关闭当前页面会出现空白缝隙
//        MyApp.get().getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mActivity.finish();
//            }
//        }, 3000);
    }

}
