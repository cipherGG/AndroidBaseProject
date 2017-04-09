package com.android.base.utils.comp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;
import android.view.WindowManager;

import com.android.base.utils.view.ScreenUtils;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity管理工具类
 */
public class ActivityUtils {

    private static boolean anim = true; // 跳转动画开关
    private static final int animIn = android.R.anim.fade_in; // 4.4下的进场效果
    private static final int animOut = android.R.anim.fade_out; // 4.4下的退场效果

    public static void initBeforeSuperCreate(Activity activity) {
        Window window = activity.getWindow(); // 软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 键盘不会遮挡输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 不自动弹键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // 总是隐藏键盘
        ScreenUtils.requestPortrait(activity); // 竖屏
        if (activity instanceof AppCompatActivity) { // titleBar
            ScreenUtils.requestNoTitle((AppCompatActivity) activity);
        }
        // 专门的跳转方式才会有过场效果
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade()); // 下一个activity进场
            window.setExitTransition(new Fade()); //  当前activity向后时退场
            // window.setReenterTransition(slideIn); // 上一个activity进场
            // window.setReturnTransition(slideOut); // 当前activity向前时退场
        }
    }

    /**
     * ***********************************跳转***********************************
     * Context启动activity
     */
    public static void startActivity(Context from, Intent intent) {
        if (from == null || intent == null) return;
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try { // 有些机型会报错
                    activity.startActivity(intent, ActivityOptions
                            .makeSceneTransitionAnimation(activity).toBundle());
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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            from.startActivity(intent);
        }
    }

    /**
     * fragment启动activity
     */
    public static void startActivity(Fragment from, Intent intent) {
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            try { // 有些机型会报错
                from.startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation(activity).toBundle());
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
        if (from == null || intent == null) return;
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                from.startActivityForResult(intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(from).toBundle());
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
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            try { // 有些机型会报错
                from.startActivityForResult(intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivityForResult(intent, requestCode);
            }
        } else {
            from.startActivityForResult(intent, requestCode);
            if (anim && activity != null) { // 4.4跳转效果
                activity.overridePendingTransition(animIn, animOut);
            }
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startActivityForFragment(Fragment from, Intent intent, int requestCode) {
        if (from == null || intent == null) return;
        FragmentActivity activity = from.getActivity();
        if (activity == null) return;
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.startActivityFromFragment(from, intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
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
