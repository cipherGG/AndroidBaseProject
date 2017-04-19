package com.android.base.utils.view;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.base.base.BaseApp;
import com.android.base.utils.comp.ContextUtils;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe toast工具类
 */
public class ToastUtils {

    private static Toast toast;

    public static void show(final CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        BaseApp.get().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    createToast();
                }
                toast.setText(message);
                toast.show();
            }
        });
    }

    public static void show(int resId) {
        if (resId == 0) return;
        String toast = ContextUtils.get().getString(resId);
        show(toast);
    }

    public static void cancel() {
        BaseApp.get().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) return;
                toast.cancel();
            }
        });
    }

    /* 自定义Toast */
    private static void createToast() {
        if (toast != null) return;
        toast = Toast.makeText(ContextUtils.get(), "", Toast.LENGTH_SHORT);
    }

}