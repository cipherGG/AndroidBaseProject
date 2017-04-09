package com.jiangzg.project.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.android.base.utils.comp.ActivityUtils;
import com.android.base.utils.view.DialogUtils;
import com.android.base.utils.func.FileUtils;
import com.android.base.utils.comp.IntentUtils;
import com.android.base.utils.net.RetrofitUtils;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;
import com.jiangzg.project.domain.Version;
import com.jiangzg.project.utils.MyUtils;
import com.jiangzg.project.utils.ResUtils;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UpdateService extends Service {

    public static void goService(Context from) {
        Intent intent = new Intent(from, UpdateService.class);
        from.startService(intent);
    }

    public UpdateService() {
    }

    @Override
    public void onCreate() {
        checkUpdate();
    }

    /* startService才走这个 不走下面的 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /* bindService不走上面的 走这个 */
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    private void checkUpdate() {
//        final int code = AppUtils.get().getVersionCode();
//        Call<Version> call = RetrofitUtils.call(RetrofitUtils.Head.common, RetrofitUtils.Factory.gson)
//                .checkUpdate(code);
//        RetrofitUtils.enqueue(call, new RetrofitUtils.CallBack<Version>() {
//            @Override
//            public void onSuccess(Version result) {
//                if (result == null) {
//                    stopSelf(); // 停止服务
//                    return;
//                }
//                if (code < result.getVersionCode()) { // 小于 有新版本
//                    showNoticeDialog(result); //  提示对话框
//                } else {
//                    stopSelf(); // 停止服务
//                }
//            }
//
//            @Override
//            public void onFailure(int httpCode, String errorMessage) {
//                MyUtils.httpFailure(httpCode, errorMessage);
//            }
//        });
    }

    /* 提示更新 */
    private void showNoticeDialog(final Version version) {
        String title = String.format(getString(R.string.find_new_version), version.getVersionName());
        String message = version.getChangeLog();
        String positive = getString(R.string.update_now);
        String negative = getString(R.string.update_delay);
        AlertDialog dialog = DialogUtils.createAlert(this, title, message, positive, negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newThreadDown(version);
                    }
                });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    /* 子线程下载 */
    private void newThreadDown(final Version version) {
        MyApp.get().getThread().execute(new Runnable() {
            @Override
            public void run() {
                downloadApk(version);
            }
        });
    }

    /* 下载apk */
    private void downloadApk(final Version version) {
        Call<ResponseBody> call = RetrofitUtils.call(RetrofitUtils.Head.empty, RetrofitUtils.Factory.empty)
                .downloadLargeFile(version.getUpdateUrl());
        RetrofitUtils.enqueue(call, new RetrofitUtils.CallBack<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody body) { // 回调也是子线程
                if (body == null || body.byteStream() == null) return;
                MyApp.get().getThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        File apkFile = ResUtils.createAPKInRes(version.getVersionName());
                        FileUtils.writeFileFromIS(apkFile, body.byteStream(), false);
                        // 启动安装
                        Intent installIntent = IntentUtils.getInstallIntent(apkFile);
                        ActivityUtils.startActivity(UpdateService.this, installIntent);
                    }
                });
            }

            @Override
            public void onFailure(int httpCode, String errorMessage) {
                MyUtils.httpFailure(httpCode, errorMessage);
            }
        });
    }

}
