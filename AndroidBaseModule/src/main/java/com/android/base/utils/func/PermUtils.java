package com.android.base.utils.func;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.android.base.base.BaseApp;
import com.android.base.utils.other.LogUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Jiangzg on 2016/11/18.
 * 权限验证框架管理
 */
public class PermUtils {

    public interface PermissionListener {
        /* 同意使用权限 */
        void onAgree();
    }

    /**
     * 请求权限,返回结果一起处理
     */
    public static void request(Context context, final PermissionListener listener,
                               final String... permissions) {
        if (isPermissionOK(context, permissions)) return;
        RxPermissions rxPermissions = RxPermissions.getInstance(context);
        Observable<Boolean> request = rxPermissions.request(permissions);
        request.subscribe(new Action1<Boolean>() {
            @Override
            public void call(final Boolean aBoolean) {
                BaseApp.get().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (aBoolean) { // 同意使用权限
                            if (listener != null) {
                                listener.onAgree();
                            }
                        } else {
                            LogUtils.e("拒绝使用权限");
                        }
                    }
                });
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtils.e("请求权限抛出异常");
            }
        }, new Action0() {
            @Override
            public void call() {
                LogUtils.d("请求权限完成");
            }
        });
    }

    /* 是否允许 */
    private static boolean isPermissionOK(Context context, String... permissions) {
        boolean okAll = true;
        for (String permission : permissions) {
            boolean okThis = ActivityCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
            okAll = okAll && okThis;
        }
        return okAll;
    }

    /**
     * appUtils
     */
    public static void requestApp(Context context, PermissionListener listener) {
        String[] permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        } else {
            permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        }
        request(context, listener, permission);
    }

    /**
     * DeviceUtils
     */
    public static void requestDevice(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 拍照
     */
    public static void requestCamera(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.CAMERA);
    }

    /**
     * 地图
     */
    public static void requestMap(Context context, PermUtils.PermissionListener listener) {
        PermUtils.request(context, listener, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 分享(瞎TM搞)
     */
    public static void requestShare(Context context, PermUtils.PermissionListener listener) {
        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.READ_LOGS, Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        PermUtils.request(context, listener, mPermissionList);
    }

}
