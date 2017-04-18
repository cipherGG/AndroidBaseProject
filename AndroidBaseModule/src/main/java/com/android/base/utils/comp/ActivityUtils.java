package com.android.base.utils.comp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity管理工具类
 */
public class ActivityUtils {

    private static int animIn = android.R.anim.fade_in; // 4.4下的进场效果
    private static int animOut = android.R.anim.fade_out; // 4.4下的退场效果

    /**
     * Context启动activity
     */
    public static void startActivity(Context from, Intent intent) {
        startActivity(from, intent, true);
    }

    public static void startActivity(Context from, Intent intent, boolean anim) {
        if (from == null || intent == null) return;
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try { // 有些机型会报错
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(activity, null, null);
                    activity.startActivity(intent, options.toBundle());
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.startActivity(intent);
                }
            } else {
                activity.startActivity(intent);
                if (anim) { // 4.4跳转效果
                    activity.overridePendingTransition(animIn, animOut);
                }
            }
        } else {
            StackUtils.changeTask(intent);
            from.startActivity(intent);
        }
    }

    /**
     * fragment启动activity
     */
    public static void startActivity(Fragment from, Intent intent) {
        startActivity(from, intent, true);
    }

    public static void startActivity(Fragment from, Intent intent, boolean anim) {
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (anim && activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, null, null);
                from.startActivity(intent, options.toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivity(intent);
            }
        } else {
            from.startActivity(intent);
        }
    }

    /**
     * 启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Activity from, Intent intent, int requestCode) {
        startActivity(from, intent, requestCode, true);
    }

    public static void startActivity(Activity from, Intent intent, int requestCode, boolean anim) {
        if (from == null || intent == null) return;
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(from, null, null);
                from.startActivityForResult(intent, requestCode, options.toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivityForResult(intent, requestCode);
            }
        } else {
            from.startActivityForResult(intent, requestCode);
            if (anim) {  // 4.4跳转效果
                from.overridePendingTransition(animIn, animOut);
            }
        }
    }

    /**
     * Fragment启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Fragment from, Intent intent, int requestCode) {
        startActivity(from, intent, requestCode, true);
    }

    public static void startActivity(Fragment from, Intent intent, int requestCode, boolean anim) {
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (activity == null) return;
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, null, null);
                from.startActivityForResult(intent, requestCode, options.toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivityForResult(intent, requestCode);
            }
        } else {
            from.startActivityForResult(intent, requestCode);
            if (anim) { // 4.4跳转效果
                activity.overridePendingTransition(animIn, animOut);
            }
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startActivityForFragment(Fragment from, Intent intent, int requestCode) {
        startActivityForFragment(from, intent, requestCode, true);
    }

    public static void startActivityForFragment(Fragment from, Intent intent, int requestCode, boolean anim) {
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (activity == null) return;
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, null, null);
                activity.startActivityFromFragment(from, intent, requestCode, options.toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                activity.startActivityFromFragment(from, intent, requestCode);
            }
        } else {
            activity.startActivityFromFragment(from, intent, requestCode);
            if (anim) { // 4.4跳转效果
                activity.overridePendingTransition(animIn, animOut);
            }
        }
    }

}
