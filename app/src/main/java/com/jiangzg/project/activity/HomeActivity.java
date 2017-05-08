package com.jiangzg.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.base.comp.ActivityTrans;
import com.android.base.comp.IntentUtils;
import com.android.base.media.ImgMediaUtils;
import com.android.base.other.ConvertUtils;
import com.android.base.time.TimeUtils;
import com.android.base.view.ToastUtils;
import com.android.depend.base.ZActivity;
import com.android.depend.utils.GlideUtils;
import com.android.depend.utils.LogUtils;
import com.android.depend.utils.LuBanUtils;
import com.android.depend.utils.RxPermUtils;
import com.jiangzg.project.R;
import com.jiangzg.project.utils.ResUtils;
import com.jiangzg.project.utils.ViewUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.OnCompressListener;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 主界面
 */
public class HomeActivity extends ZActivity<HomeActivity> {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.ivMain)
    ImageView ivMain;

    private File jpgInRes;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityTrans.start(from, intent);
    }

    @Override
    protected int initObj(Intent intent) {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.initTop(mActivity, "主页面");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        LogUtils.e("----------------");
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                RxPermUtils.requestCamera(new RxPermUtils.PermissionListener() {
                    @Override
                    public void onAgree() {
                        jpgInRes = ResUtils.createJPGInRes();
                        Intent camera = IntentUtils.getCamera(jpgInRes);
                        ActivityTrans.startRequest(mActivity, camera, 11);
                    }
                });
                break;
            case R.id.btn2:
                Intent picture = IntentUtils.getPicture();
                ActivityTrans.startRequest(mActivity, picture, 22);
                break;
            case R.id.btn3:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 11:
                compress();
                break;
            case 22:
                Uri pictureUri = ImgMediaUtils.getPictureUri(data);
                jpgInRes = ConvertUtils.URI2File(pictureUri);
                compress();
                break;
        }
    }

    private void compress() {
        LuBanUtils.compress(mActivity, jpgInRes, new OnCompressListener() {
            @Override
            public void onStart() {
                getLoading().show();
            }

            @Override
            public void onSuccess(File file) {
                getLoading().dismiss();
                ToastUtils.show(file.getAbsolutePath());
                GlideUtils.load(mActivity, file, ivMain);
            }

            @Override
            public void onError(Throwable e) {
                getLoading().dismiss();
            }
        });
    }

    private Long lastExitTime = 0L; //最后一次退出时间

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Long nowTime = TimeUtils.getCurrentLong();
            if (nowTime - lastExitTime > 2000) { // 第一次按
                ToastUtils.show(R.string.press_again_exit);
            } else { // 返回键连按两次
                System.exit(0); // 真正退出程序
            }
            lastExitTime = nowTime;
        }
        return true;
    }

}
