package com.android.base.utils.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by gg on 2017/4/3.
 * 换肤管理类
 */
public class SkinUtils {
//
//    /**
//     * 这里只有着色模式，全屏模式只是status的透明设置，其他控件要在xml里设置好透明，不好封装
//     */
//    private static final String KEY_SKIN = "skin_type";
//    // 主题样式和状态栏颜色 ,每个position的颜色要对应，不可错乱
//    private static int[] THEMES = new int[]{-1};
//    private static int[] COLOR = new int[]{get0xColor(-1)};
//
//    /**
//     * BaseActivity初始化调用
//     */
//    public static void initTheme(Activity activity) {
//        activity.setTheme(getTheme(activity));
//
//        setStatusColor(activity, getColor(activity));
//    }

    /**
     * 获取当前主题的资源id
     */
//    private static int getTheme(Context context) {
//        int getSkinLen = getThemePre(context);
//
//        if (getSkinLen >= THEMES.length) {
//            getSkinLen = 0;
//        }
//        return THEMES[getSkinLen];
//    }
//
//    /**
//     * 获取statusColor
//     */
//    public static int getColor(Context context) {
//        int skinPreferences = getThemePre(context);
//
//        return COLOR[skinPreferences];
//    }
//
//    /**
//     * 切换下一个主题
//     */
//    public static void toggleTheme(Activity activity) {
//        int skinType = getThemePre(activity);
//
//        if (skinType == THEMES.length - 1)
//            skinType = 0;
//        else
//            skinType++;
//
//        setTheme(activity, skinType);
//    }

//    /**
//     * 切换指定id的主题 , 切记setTheme之后要在activity重新create
//     * 重新实例化控件和加载监听器， 这也就是为什么不再create里初始化数据的原因了
//     * 记住，create之后attach上的view都不会保留
//     */
//    public static void setTheme(Activity activity, int id) {
//        setThemePre(activity, id);
//
//        initTheme(activity);
//    }

//    /**
//     * 把当前样式id写到Preferences , 就是list的position
//     */
//    private static void setThemePre(Context context, int id) {
//
//        PreferencesUtils.putInt(context, KEY_SKIN, id);
//    }
//
//    /**
//     * 获取当前程序的样式id
//     */
//    private static int getThemePre(Context context) {
//
//        return PreferencesUtils.getInt(context, KEY_SKIN, 0);
//    }
//
//    /**
//     * setStatusColor(int)的参数不是资源文件的索引，所以要转换
//     */
//    private static int get0xColor(int colorID) {
//
//        return ContextCompat.getColor(MyApp.getInstance().getBaseContext(), colorID);
//    }

    /**
     * 着色模式: 为status着色 必须是16进制 ,Status底部为白色,所以这个不能全屏模式
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusColor(Activity activity, int statusColor) {
        // 清除Status透明的状态
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 添加Status可以着色的状态
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 开始着色Status
        activity.getWindow().setStatusBarColor(statusColor);
    }

    /**
     * 全屏模式：这里只负责status的透明 ,并且最顶部view要设置 fitsSystemWindows="true"
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusTrans(Activity activity) {
        Window window = activity.getWindow();
        // 清除Status和navigation透明的状态
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 让DecorView填充Status和Navigation，这样他们的底色就不是白色，而是我们的Layout的背景色
        // setSystemUiVisibility就是用来操作Status的方法
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        // 添加Status可以着色的状态
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 开始着色Status
        window.setStatusBarColor(Color.TRANSPARENT);
        // 开始着色Navigation
        window.setNavigationBarColor(Color.TRANSPARENT);
    }
}
