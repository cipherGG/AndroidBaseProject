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

import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity<TaskActivity> {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, TaskActivity.class);
        StackUtils.changeTask(intent);
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected int initObj(Bundle savedInstanceState) {
        return R.layout.activity_task;
    }

    @Override
    protected void initView() {
        int taskId = getTaskId();
        boolean taskRoot = StackUtils.isTaskRoot(this);
//        Stack<StackUtils.Task> tasks = StackUtils.get();
        String logTag = "";
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                TopActivity.goActivityNew(mActivity);
                break;
            case R.id.btn2:

                break;
        }
    }

}
