package com.android.base.utils.comp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import com.android.base.base.BaseApp;

/**
 * Created by gg on 2017/4/4.
 * 上下文管理类
 */
public class ContextUtils {

    public static Application get() {
        return BaseApp.get();
    }

    public static PackageManager getPackageManager() {
        return get().getPackageManager();
    }

    public static ActivityManager getActivityManager() {
        return (ActivityManager) get().getSystemService(Context.ACTIVITY_SERVICE);
    }


    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) get().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static LocationManager getLocationManager() {
        return (LocationManager) get().getSystemService(Context.LOCATION_SERVICE);
    }

    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) get().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static InputMethodManager getInputManager() {
        return (InputMethodManager) get().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static ClipboardManager getClipboardManager() {
        return (ClipboardManager) get().getSystemService(Context.CLIPBOARD_SERVICE);
    }

}
