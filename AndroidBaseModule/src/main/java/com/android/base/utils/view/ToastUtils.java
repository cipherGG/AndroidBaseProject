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
    private static ToastUtils instance;
    private static Toast toast;

    /* Toast 单例 可覆盖 */
    public static ToastUtils get() {
        if (instance == null) {
            synchronized (ToastUtils.class) {
                if (instance == null) {
                    instance = new ToastUtils();
                }
            }
        }
        return instance;
    }

    /* 自定义Toast */
    private ToastUtils() {
        if (toast != null) return;
        toast = Toast.makeText(ContextUtils.get(), "", Toast.LENGTH_SHORT);
    }

    public void show(final CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        BaseApp.get().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    new ToastUtils();
                }
                toast.setText(message);
                toast.show();
            }
        });
    }

    public void show(int resId) {
        if (resId == 0) return;
        String toast = ContextUtils.get().getString(resId);
        show(toast);
    }
}