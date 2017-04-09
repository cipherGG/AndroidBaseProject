package com.jiangzg.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.base.utils.view.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jiangzg.project.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by JiangZhiGuo on 2017-1-15.
 * describe 二维码扫描
 */
public class ScanUtils {

    public interface OnScanResultListener<T> {
        void onSuccess(T t);

        void onFail();
    }

    public static void initApp(Context context) {
        ZXingLibrary.initDisplayOpinion(context);
    }

    /* 开始扫描 */
    public static void scanStart(Activity from) {
        Intent intent = new Intent(from, CaptureActivity.class);
        from.startActivityForResult(intent, MyUtils.REQUEST_SCAN);
    }

    /* 扫描结果（string） */
    public static void scanResult(Intent data, OnScanResultListener<String> listener) {
        if (null != data && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                if (listener != null) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    listener.onSuccess(result);
                }
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                ToastUtils.get().show(R.string.code_2_scan_error);
                if (listener != null) {
                    listener.onFail();
                }
            }
        }
    }

    /* 扫描结果（class）*/
    public static <T> void scanResult(Class<T> t, Intent data, OnScanResultListener<T> listener) {
        if (null != data && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                if (listener != null) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    try {
                        T t1 = new Gson().fromJson(result, t);
                        listener.onSuccess(t1);
                    } catch (JsonSyntaxException e) {
                        ToastUtils.get().show(R.string.please_scan_real_code_2);
                        listener.onFail();
                    }
                }
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                ToastUtils.get().show(R.string.code_2_scan_error);
                if (listener != null) {
                    listener.onFail();
                }
            }
        }
    }
}
