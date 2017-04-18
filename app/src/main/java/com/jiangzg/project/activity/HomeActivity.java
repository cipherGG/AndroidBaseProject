package com.jiangzg.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.base.base.BaseActivity;
import com.android.base.utils.comp.ActivityUtils;
import com.android.base.utils.comp.StackUtils;
import com.android.base.utils.func.BatteryUtils;
import com.android.base.utils.func.LocationUtils;
import com.android.base.utils.view.QuickUtils;
import com.jiangzg.project.R;
import com.jiangzg.project.domain.Version;
import com.jiangzg.project.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 主界面
 */
public class HomeActivity extends BaseActivity<HomeActivity> {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    private Observable<Version> observable;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        ViewUtils.initTop(mActivity, "主页面");
        QuickUtils manager = new QuickUtils(null)
                .initRecycler(null);

        manager.dataRefresh();
    }

    protected void initData() {
//        loading.show();
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

//        List<String> bankNameList = new ArrayList<>();
//        bankNameList.add("中国工商银行");
//        bankNameList.add("中国建设银行");
//        bankNameList.add("中国农业银行");
//        bankNameList.add("中国银行");
//        bankNameList.add("交通银行");
//        bankNameList.add("招商银行");
//        String[] names = new String[bankNameList.size()];
//        String[] objects = bankNameList.toArray(names);
//
//        AlertDialog aaa = DialogUtils.createSingle(mActivity, "111", objects, 1, "aaa", null, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        aaa.show();

        BatteryUtils.get().registerReceiver(mActivity, new BatteryUtils.BatteryListener() {
            @Override
            public void percent(int percent) {

            }

            @Override
            public void up() {

            }

            @Override
            public void down() {

            }

            @Override
            public void full() {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int taskId = getTaskId();
//        Stack<StackUtils.Task> tasks = StackUtils.get();
        LocationUtils info = LocationUtils.getInfo();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
//                TopActivity.goActivityNew(mActivity);
                break;
            case R.id.btn2:
//                TaskActivity.goActivity(mActivity);
                break;
            case R.id.btn3:
//                StackUtils.finishTask(getTaskId());
                break;
        }
    }

//    private Long lastExitTime = 0L; //最后一次退出时间
//
//    /* 手机返回键 */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Long nowTime = new Date().getTime();
//
//            if (nowTime - lastExitTime > 2000) { // 第一次按
//                ToastUtils.get().show(R.string.press_again_exit);
//            } else { // 返回键连按两次
//                System.exit(0); // 真正退出程序
//            }
//            lastExitTime = nowTime;
//        }
//        return true;
//    }

}
