package com.android.base.component.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity跳转工具类
 */
public class ActivityTrans {

    /**
     * 分享元素过渡动画，一般在目标activity里的setContentView后调用
     */
    public static void setShareElement(View share, String tag) {
        if (share == null || tag == null) return;
        ViewCompat.setTransitionName(share, tag);
    }

    /**
     * Context启动activity
     */
    @SafeVarargs
    public static void start(Context from, Intent intent, Pair<View, String>... sharedElements) {
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            try {
                if (sharedElements == null || sharedElements.length < 1) {
                    startShareElement(activity, intent);
                } else {
                    startShareElement(activity, intent, sharedElements);
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.startActivity(intent);
            }
        } else {
            startFromContext(from, intent);
        }
    }

    /**
     * fragment启动activity
     */
    @SafeVarargs
    public static void start(Fragment from, Intent intent, Pair<View, String>... sharedElements) {
        try {
            FragmentActivity activity = from.getActivity();
            if (sharedElements == null || sharedElements.length < 1) {
                startShareElement(activity, intent);
            } else {
                startShareElement(activity, intent, sharedElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
            from.startActivity(intent);
        }
    }

    /**
     * activity启动activity，setResult设置回传的resultCode和intent
     */
    @SafeVarargs
    public static void startResult(Activity from, Intent intent, int requestCode,
                                   Pair<View, String>... sharedElements) {
        try {
            if (sharedElements == null || sharedElements.length < 1) {
                startShareElement(from, intent, requestCode);
            } else {
                startShareElement(from, intent, requestCode, sharedElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
            from.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Fragment启动activity，setResult设置回传的resultCode和intent
     */
    @SafeVarargs
    public static void startResult(Fragment from, Intent intent, int requestCode,
                                   Pair<View, String>... sharedElements) {
        try {
            FragmentActivity activity = from.getActivity();
            if (sharedElements == null || sharedElements.length < 1) {
                startShareElement(activity, intent, requestCode);
            } else {
                startShareElement(activity, intent, requestCode, sharedElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
            from.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    @SafeVarargs
    public static void startResultFragment(Fragment from, Intent intent, int requestCode,
                                           Pair<View, String>... sharedElements) {
        FragmentActivity activity = from.getActivity();
        try {
            if (sharedElements == null || sharedElements.length < 1) {
                startShareElement(activity, intent, requestCode);
            } else {
                startShareElement(activity, intent, requestCode, sharedElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.startActivityFromFragment(from, intent, requestCode);
        }
    }

    /* context启动activity */
    private static void startFromContext(Context from, Intent intent) {
        if (from == null || intent == null) return;
        ActivityStack.changeTask(intent);
        from.startActivity(intent);
    }

    /* 5.0过渡跳转(没有分享元素) */
    @SuppressWarnings("unchecked")
    private static void startShareElement(Activity from, Intent intent) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(from);
        from.startActivity(intent, options.toBundle());
    }

    /* 5.0过渡跳转(有分享元素) */
    @SafeVarargs
    private static void startShareElement(Activity from, Intent intent,
                                          Pair<View, String>... sharedElements) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(from, sharedElements);
        from.startActivity(intent, options.toBundle());
    }

    /* 5.0过渡跳转(没有分享元素) */
    @SuppressWarnings("unchecked")
    private static void startShareElement(Activity from, Intent intent, int requestCode) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(from);
        from.startActivityForResult(intent, requestCode, options.toBundle());
    }

    /* 5.0过渡跳转(有分享元素) */
    @SafeVarargs
    private static void startShareElement(Activity from, Intent intent, int requestCode,
                                          Pair<View, String>... sharedElements) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(from, sharedElements);
        ActivityCompat.startActivityForResult(from, intent, requestCode, options.toBundle());
    }

}
