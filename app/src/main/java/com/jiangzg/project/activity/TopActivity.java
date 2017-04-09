package com.jiangzg.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.base.base.BaseActivity;
import com.android.base.utils.comp.ActivityUtils;
import com.android.base.utils.comp.StackUtils;
import com.jiangzg.project.R;

import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

public class TopActivity extends BaseActivity<TopActivity> {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, TopActivity.class);
        intent.putExtra("test", "test");
        ActivityUtils.startActivity(from, intent);
    }

    public static void goActivityNew(Activity from) {
        Intent intent = new Intent(from, TopActivity.class);
        StackUtils.changeTask(intent);
        intent.putExtra("test", "test");
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected int initObj(Bundle savedInstanceState) {
        String test = mIntent.getStringExtra("test");
        int length = test.length();
        return R.layout.activity_test;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String test = intent.getStringExtra("test");
        int taskId = getTaskId();
        boolean taskRoot = StackUtils.isTaskRoot(this);
//        List<StackUtils.Task> tasks = StackUtils.get();
        String logTag = "";
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
        boolean taskRoot = StackUtils.isTaskRoot(this);
//        Stack<StackUtils.Task> tasks = StackUtils.get();
        String logTag = "";
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                TopActivity.goActivity(mActivity);
                break;
            case R.id.btn2:
                StackUtils.finishTask(getTaskId());
                break;
        }
    }

}
