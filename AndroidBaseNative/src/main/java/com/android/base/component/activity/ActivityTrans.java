package com.android.base.component.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity跳转工具类
 */
public class ActivityTrans {

    /* activity跳转demo */
    public static void goActivity(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        // intent.putExtra();
        ActivityTrans.start(from, intent);
    }

    private static int animIn, animOut; // 4.4跳转效果

    /**
     * activity过渡动画初始化, 要在setContentView之前调用
     */
    public static void initActivity(Activity activity) {
        Window window = activity.getWindow();
        // 专门的跳转方式才会有过场效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade()); // 下一个activity进场
            window.setExitTransition(new Fade()); //  当前activity向后时退场
            // window.setReenterTransition(new Fade()); // 上一个activity进场
            // window.setReturnTransition(new Fade()); // 当前activity向前时退场
        }
        animIn = android.R.anim.fade_in;
        animOut = android.R.anim.fade_out;
    }

    /**
     * 分享元素过渡动画，一般在目标activity里的create中调用
     */
    public static void setShareElement(View share, String tag) {
        if (share == null || tag == null) return;
        ViewCompat.setTransitionName(share, tag);
    }

    /**
     * 获取分享元素单位
     */
    public static Pair<View, String> getPair(View share, String tag) {
        return Pair.create(share, tag);
    }

    /**
     * Context启动activity
     */
    public static void start(Context from, Intent intent) {
        start(from, intent, true);
    }

    public static void start(Context from, Intent intent, boolean anim) {
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startShareElement(activity, intent);
            } else {
                activity.startActivity(intent);
                appendAnim(activity, anim);
            }
        } else {
            startFromContext(from, intent);
        }
    }

    public static void start(Context from, Intent intent, Pair<View, String>... sharedElements) {
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startShareElement(activity, intent, sharedElements);
            } else {
                activity.startActivity(intent);
                appendAnim(activity, true);
            }
        } else {
            startFromContext(from, intent);
        }
    }

    /**
     * fragment启动activity
     */
    public static void start(Fragment from, Intent intent) {
        start(from, intent, true);
    }

    public static void start(Fragment from, Intent intent, boolean anim) {
        FragmentActivity activity = from.getActivity();
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent);
        } else {
            from.startActivity(intent);
            appendAnim(activity, anim);
        }
    }

    public static void start(Fragment from, Intent intent, Pair<View, String>... sharedElements) {
        FragmentActivity activity = from.getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent, sharedElements);
        } else {
            from.startActivity(intent);
            appendAnim(activity, true);
        }
    }

    /**
     * activity启动activity，setResult设置回传的resultCode和intent
     */
    public static void startRequest(Activity from, Intent intent, int requestCode) {
        startRequest(from, intent, requestCode, true);
    }

    public static void startRequest(Activity from, Intent intent, int requestCode, boolean anim) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(from, intent, requestCode);
        } else {
            from.startActivityForResult(intent, requestCode);
            appendAnim(from, anim);
        }
    }

    public static void startRequest(Activity from, Intent intent, int requestCode,
                                    Pair<View, String>... sharedElements) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(from, intent, requestCode, sharedElements);
        } else {
            from.startActivityForResult(intent, requestCode);
            appendAnim(from, true);
        }
    }

    /**
     * Fragment启动activity，setResult设置回传的resultCode和intent
     */
    public static void startRequest(Fragment from, Intent intent, int requestCode) {
        startRequest(from, intent, requestCode, true);
    }

    public static void startRequest(Fragment from, Intent intent, int requestCode, boolean anim) {
        FragmentActivity activity = from.getActivity();
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent, requestCode);
        } else {
            from.startActivityForResult(intent, requestCode);
            appendAnim(activity, anim);
        }
    }

    public static void startRequest(Fragment from, Intent intent, int requestCode,
                                    Pair<View, String>... sharedElements) {
        FragmentActivity activity = from.getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent, requestCode, sharedElements);
        } else {
            from.startActivityForResult(intent, requestCode);
            appendAnim(activity, true);
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startRequestFromFragment(Fragment from, Intent intent, int requestCode) {
        startRequestFromFragment(from, intent, requestCode, true);
    }

    public static void startRequestFromFragment(Fragment from, Intent intent, int requestCode,
                                                boolean anim) {
        FragmentActivity activity = from.getActivity();
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent, requestCode);
        } else {
            activity.startActivityFromFragment(from, intent, requestCode);
            appendAnim(activity, anim);
        }
    }

    public static void startRequestFromFragment(Fragment from, Intent intent, int requestCode,
                                                Pair<View, String>... sharedElements) {
        FragmentActivity activity = from.getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startShareElement(activity, intent, requestCode, sharedElements);
        } else {
            activity.startActivityFromFragment(from, intent, requestCode);
            appendAnim(activity, true);
        }
    }

    /* context启动activity */
    private static void startFromContext(Context from, Intent intent) {
        if (from == null || intent == null) return;
        ActivityStack.changeTask(intent);
        from.startActivity(intent);
    }

    /* 4.4跳转效果 */
    private static void appendAnim(Activity from, boolean anim) {
        if (from != null && anim) {
            from.overridePendingTransition(animIn, animOut);
        }
    }

    /* 5.0过渡跳转(没有分享元素) */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void startShareElement(Activity from, Intent intent) {
        if (from == null || intent == null) return;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(from);
        from.startActivity(intent, options.toBundle());
    }

    /* 5.0过渡跳转(有分享元素) */
    private static void startShareElement(Activity from, Intent intent,
                                          Pair<View, String>... sharedElements) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(from, sharedElements);
        ActivityCompat.startActivity(from, intent, options.toBundle());
    }

    /* 5.0过渡跳转(没有分享元素) */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void startShareElement(Activity from, Intent intent, int requestCode) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(from);
        from.startActivityForResult(intent, requestCode, options.toBundle());
    }

    /* 5.0过渡跳转(有分享元素) */
    private static void startShareElement(Activity from, Intent intent, int requestCode,
                                          Pair<View, String>... sharedElements) {
        if (from == null || intent == null) return;
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(from, sharedElements);
        ActivityCompat.startActivityForResult(from, intent, requestCode, options.toBundle());
    }

}
